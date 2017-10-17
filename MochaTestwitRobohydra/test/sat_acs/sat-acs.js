var express = require( 'express' ),
    jf = require( 'jsonfile' ),
    xmlParser = require( 'express-xml-bodyparser' ),
    cookieParser = require( 'cookie-parser' ),
    _ = require( 'lodash' ),
    jade = require( 'jade' ),
    fs = require( 'fs' ),
    request = require( 'request' ),
    async = require( 'async' ),
    assert = require( 'assert' ),
    bodyParser = require( 'body-parser' ),
    logger = require( '../robo/log4js-config')
//    argv = require( 'minimist' )( process.argv.slice( 2 ), {
//        alias: {
//            port: 'p',
//            zmqPort: 'z',
//            cnrUrl: 'c'
//        },
//
//        default: {
//            port: 10002,
//            zmpPort: 3000,
//            cnrUrl: "http://dmm:8097"
//        }
//    } );


// ZMQ  publisher
var zmq = require( 'zmq' );
var sock = zmq.socket( 'pub' );
sock.bindSync( 'tcp://*: 9000' );


var app = express();
var sessions = {};
var session_id = 1;
var templates = {};


var DEBUG_ACS = true;

var pending_actions = [];
var pending_listeners = [];
// list of ACS Methods
var acs_methods = [ 'Inform', 'TransferComplete' ];


function ns_remover( name ) {
    return _.last( name.split( ':' ), 1 );
}

var SESSION_STATUS = {};
_.forEach( [ 'INFORM', 'PEND_CPE', 'HOLD_ON' ], function ( status ) {
    SESSION_STATUS[ status ] = status;
} );

function Session( fc_logger ) {
    this.id = session_id;
    session_id++;
    sessions[ this.id ] = this;
    this.hold_for = 80; // Should be enough - while can use the CNR if needed
    this.status = SESSION_STATUS.INFORM;
    if (DEBUG_ACS){
        logger.debug( "Open new session=%d, status=%s", this.id, this.status );
    }
    this.inner_process = this.server_mode;
    this.fc_logger = fc_logger;
}

function device_timeout( session ) {
    var fc_logger = session.fc_logger;
    logger.error( 'session %d device timeout', session.id );
    session.term();
}

Session.prototype.server_mode = function ( body ) {

    var fc_logger = this.fc_logger;

    if ( body ) {
        var req_type = _.find( acs_methods, function ( method ) {
            return method in body;
        } );
        assert( req_type );
        var req_body = body[ req_type ];

        this[ req_type ] = req_body;

        var device_sn = this.Inform.DeviceId.SerialNumber;

        var to_trigger = _.filter( pending_listeners, {
            action: {
                device_sn: device_sn,
                type: req_type
            }
        } );
        if (DEBUG_ACS){
            logger.debug( 'SERVER MODE, request is ' + req_type + ', CPE is ' + this.Inform.DeviceId.SerialNumber + ', num listeners:', to_trigger.length );
        }
        _.forEach( to_trigger, function ( pl ) {
            if ( this.hold_for !== pl.action.hold_for ) {
                 if (DEBUG_ACS){
                    logger.debug( "req_type=", req_type, "Reset hold_for=", pl.action.hold_for );
                    logger.trace( "req_type=", req_type, "to_trigger=", to_trigger, "Reset hold_for=", pl.action.hold_for );
                 }
            }
            this.hold_for = Math.max( this.hold_for, pl.action.hold_for );
            pl.response( this[ req_type ] );
        }, this );
        if (DEBUG_ACS){
            logger.debug( "SND TR69Event" );
        }
        sock.send( [ 'TR69Event', JSON.stringify( {
            eventType: req_type,
            deviceSN: device_sn,
            body: req_body,
            headers: logger.add_header()
        } ) ] );

        this.next_action( new nbi_action( {
            type: req_type + 'Response',
            hold_for: this.hold_for
        }, null, fc_logger ) );
    } else {
        if (DEBUG_ACS){
            var fc_logger = fc_factory.create( );
            this.fc_logger = fc_logger;
            logger.debug( 'Switching to CLIENT MODE CPE is', this.Inform.DeviceId.SerialNumber );
        }
        this.current_action = null;
        this.inner_process = this.client_mode;
        this.inner_process( body );
    }
};

Session.prototype.client_mode = function ( body ) {

    
    if ( this.current_action ) {
        if (this.current_action.action.hold_for) {
            this.hold_for = Math.max(this.hold_for, this.current_action.action.hold_for);
        }
        if (DEBUG_ACS){
            var fc_logger = this.fc_logger;
            logger.debug( 'Got response for deviceId=%s, action=%s', this.current_action.action.device_sn, this.current_action.action.type );
            logger.debug( "Set stat from: ", this.status, "to:", SESSION_STATUS.HOLD_ON );
        }
        this.status = SESSION_STATUS.HOLD_ON;
        this.current_action.response( body );
        this.current_action = null;
    }

    this.next_action();
};

Session.prototype.process = function ( req, res ) {
    
    clearTimeout( this.device_timeout );
    this.http = {
        req: req,
        res: res
    };
    var body = req.body.Envelope ? req.body.Envelope.Body : null;
    if (DEBUG_ACS){
        var fc_logger = this.fc_logger;
        logger.debug( "Set stat from:", this.status, "to:", SESSION_STATUS.HOLD_ON );
    }
    this.status = SESSION_STATUS.HOLD_ON;
    this.inner_process( body );
};


Session.prototype.next_action = function ( force_action ) {
    var fc_logger = this.fc_logger;
    if (DEBUG_ACS){
        logger.debug( "next_action, stat:", this.status );
        logger.trace( "next_action, stat:", this.status, "force_action:", force_action );
    }
    if ( this.status == SESSION_STATUS.HOLD_ON ) {
        var action = _.find( pending_actions, function ( action ) {
            return action.action.device_sn === this.Inform.DeviceId.SerialNumber;
        }, this );


        if ( force_action ) {
            action = force_action;
        }

        if ( action ) {
            clearTimeout( this.hold_timeout );
            this.current_action = action;
            if (DEBUG_ACS){
                logger.trace( 'SND res deviceId=%s, action=%s', action.action.device_sn, action.action.type, action.action.vars ? action.action.vars.paramItems : "" );
            }
            this.http.res.set( logger.add_header() );
            this.http.res.send( templates[ action.action.type ]( action.action.vars ) );
            if (DEBUG_ACS){
                logger.debug( "Set stat from: ", this.status, "to:", SESSION_STATUS.PEND_CPE );
            }
            this.status = SESSION_STATUS.PEND_CPE;
            this.device_timeout = setTimeout( device_timeout, 60000, this );
        } else {
             if (DEBUG_ACS){
                logger.debug( "session:", this.id, "hold timeout:", this.hold_for, "- expired" );
             }
            this.hold_timeout = setTimeout( function ( session ) {
             if (DEBUG_ACS){
                 logger.info( 'session %d will terminate', session.id );
             }
                session.term();
            }, this.hold_for, this );
        }
    }
};

Session.prototype.term = function () {
    if (DEBUG_ACS){
        var fc_logger = this.fc_logger;
        logger.debug( 'closing session', this.id );
    }
    if ( this.status == SESSION_STATUS.HOLD_ON ) {
        if (DEBUG_ACS){
            logger.debug( "SND res session stat:", SESSION_STATUS.HOLD_ON, ", sendEmptyEnvelope" );
        }
        this.http.res.send( 204 );
    }
    delete sessions[ this.id ];
};

app.post( '/cwmp',
    xmlParser( {
        tagNameProcessors: [ ns_remover ],
        attrNameProcessors: [ ns_remover ],
        explicitArray: false
    } ),
    cookieParser(),
    function ( req, res ) {
        var fc_logger = fc_factory.create( req.headers );
        try {
            var session = sessions[ req.cookies.session_id ];
            if (DEBUG_ACS){
                logger.debug( "RCV post cwmp msg" );
            }
            if ( !session ) {
                session = new Session( fc_logger );
                if (DEBUG_ACS){
                    logger.debug( 'creating new session id=%d', session.id );
                }
            }
            res.cookie( 'session_id', session.id, {
                httpOnly: true
            } );
            res.set( 'content-type', 'text/xml' );
            session.process( req, res );

        } catch ( e ) {
            //console.dir( e );
            //console.trace();
            logger.error( "exception is: ",e);
            logger.error( "SND res 500");
            res.send( 500 );
            //this.kill();
        }
    } );

function action_timeout( action ) {
    logger.warn( 'got timeout' );
    action.action_timeout = null;
    action.response( {
        response: null
    } );
}

function nbi_action( action, res, fc_logger ) {
    this.action = action;
    this.action.device_sn = action.device_sn || '';
    this.action.device_sn = this.action.device_sn.toString();
    this.res = res;
    this.fc_logger = fc_logger;
    var interval = action.timeout || 30000;
    if (DEBUG_ACS){
        logger.debug( 'Set nbi_action timeout to=%d', interval );
    }
    this.action_timeout = setTimeout( action_timeout, interval, this );
}

nbi_action.prototype.push = function () {
    var fc_logger = this.fc_logger;
    if ( _.indexOf( acs_methods, this.action.type, this ) >= 0 ) {
        if (DEBUG_ACS){
            logger.debug( 'Device', this.action.device_sn, 'register listener for', this.action.type );
        }
        this.queue = pending_listeners;
        pending_listeners.push( this );
    } else {
        if (DEBUG_ACS){
            logger.debug( 'Device', this.action.device_sn, 'action type', this.action.type );
            logger.trace( "action:", JSON.stringify( this.action ) );
        }
        this.queue = pending_actions;
        this.fc_logger = fc_logger;
        pending_actions.push( this );
        _.forEach( sessions, function ( s ) {
            if ( this.action.device_sn === s.Inform.DeviceId.SerialNumber ) {
                s.next_action();
                return false;
            }
            return true;
        }, this );
    }
};

nbi_action.prototype.response = function ( resp ) {
    var fc_logger = this.fc_logger;
    clearTimeout( this.action_timeout );
    if ( this.res ) {
        if (DEBUG_ACS){
            logger.debug( "SND res" );
            logger.trace( "res:", resp );
        }
        this.res.json( resp );
        this.res = null;
    }
    _.pull( this.queue, this );
};

app.post( '/nbi/actions', bodyParser.json(), function ( req, res ) {
    var fc_logger = fc_factory.create( req.headers );
    if (DEBUG_ACS){
        logger.debug( 'RCV post nbi action', JSON.stringify( req.body ) );
    }
    var action = new nbi_action( req.body, res, fc_logger );
 
    var session = _.find( sessions, function( s ) {
        return ( s.Inform.DeviceId.SerialNumber === action.action.device_sn );
    } );
    if ( req.query.includeCNR && !session ) {
        // Create CNR
        if (DEBUG_ACS){
            logger.info( 'SND post CNR request', argv.cnrUrl );
        }
        request.post( argv.cnrUrl  + '/cnr/'+ action.action.device_sn, {headers : logger.add_header() }  );
    }else{
        if(session){
            session.fc_logger = fc_logger;
            if (DEBUG_ACS){
                logger.debug( "Found open session for deviceId=%s, no need for CNR", action.action.device_sn);
            }
        }else{
            logger.warn( "CNR will not done, while req.query.includeCNR is not set by the caller")
        }
    }
    action.push();
} );

app.put( '/config/*', bodyParser.json(), function ( req, res ) {
    var fc_logger = fc_factory.create( req.headers );
    if (DEBUG_ACS){
        logger.trace( 'RCV put config', JSON.stringify( req.body ) );
    }
    argv.cnrUrl = req.body.value;
    res.send( 204 );
} );

// init sequence
var jade_files = _.filter( fs.readdirSync( './' ), function ( fn ) {
    return fn.match( /.*jade$/ );
} );

_.forEach( jade_files, function ( fn ) {
    logger.info( 'loading template', fn );
    templates[ fn.split( '.jade', 1 ) ] = jade.compile( fs.readFileSync( fn, 'utf8' ), {
        filename: fn
    } );
} );

app.listen( argv.port, function () {
    if (DEBUG_ACS){
        logger.debug( 'listening on port', this.address().port );
    }
    if (process.send) {
        process.send('running');
    }
} );
