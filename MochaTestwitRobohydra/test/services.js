var fork = require( 'child_process' ).fork,
    fs = require( 'fs' ),
    _ = require( 'lodash' ),
    net = require( 'net' ),
    async = require( 'async' ),
    logger = require( './log4js-config' ).logging.getLogger( 'Mocha-Test' ),
    request = require( 'request' ),
    os = require( 'os' ),
    util = require( 'util' ),
    jfile = require( 'jsonfile' ),
    mongo_client = require( 'mongodb' ).MongoClient;
//var zmq = require( 'zmq' );
//var sock = zmq.socket( 'sub' );

var EventEmitter = require( 'events' ).EventEmitter;


var GoldenAcsEvents = function () {

};

util.inherits( GoldenAcsEvents, EventEmitter );

var goldenAcsEvents = new GoldenAcsEvents();


var service_table = {};
var rv = {};

// only one services required for everyone...
var usingTestCount = 0;
// UPM model file pre-loading
//var file_path = './test/datasources/UPM.json';
//var upm_obj = JSON.parse(fs.readFileSync(file_path, 'utf8')) ;

// Clone Obj
function clone(a) {
    return JSON.parse(JSON.stringify(a));
}


// configures DMM to use golden-acs's URL
function configure_acs_url( done ) {
    logger.info( 'configuring ACS URL @', service_table.dmm.url + '/config/cwmp/acs_url' );
    var options = {
        json: {
            value: service_table[ 'golden-acs' ].url + '/cwmp'
        }
    };
    //var options = {json: {value:"http://10.210.15.231:8080/dps/TR069"}};
    request.put( service_table.dmm.url + '/config/cwmp/acs_url', options, function ( error ) {
        if ( error ) {
            logger.error( 'error in configure_acs_url', error );
        }
        done();
    } );
}

// configures DMM to use data_source_sim's URL
function configure_dataSource_url( done ) {
    logger.debug( 'configuring robohydra URL @', service_table.dmm.url + '/config/cwmp/dataSource_url' );
    var options = {
        json: {
            value: service_table.robohydra.url
        }
    };
    request.put( service_table.dmm.url + '/config/cwmp/dataSource_url', options, function ( error ) {
        if ( error ) {
            logger.error( 'error in configure_dataSource_url', error );
        }
        done();
    } );
}

function configure_robohydra_urls(done){
    var r = request.defaults({
        baseUrl: service_table.dmm.url + '/config/',
        json: true
    });
    async.each([ 'pps', 'upm', 'cmdc', 'ccpm', 'ri', 'sm', 'bmi', 'crm', 'lcs', 'vfps'],
            function(head, cb){
                logger.debug('configuring head:', head, '@', service_table.dmm.url + '/config/' + head + '_url');
                r.put(head + '_url', { body: { value: service_table.robohydra.url + '/' + head } }, function(err){
                    if (err) {
                        logger.error('failed head:', head);
                    }
                    cb(err);
                });
            },
            done);
}

// configures dmm url and port in golden_acs for CNR purpose
function configure_dmm_url( done ) {
    logger.debug( 'configuring dmm URL in golden acs @', service_table.dmm.url + '/config/dmm_url' );
    var options = {
        json: {
            value: service_table.dmm.url
        }
    };
    request.put( service_table['golden-acs'].url + '/config/dmm_url', options, function ( error ) {
        if ( error ) {
            logger.error( 'error in configure_dmm_url in golden acs', error );
        }
        done();
    } );
}

function create_mongo_client( done ) {
    logger.debug( 'creating test mongo client' );
    var options = {
        json: {}
    };
    request.get( service_table.dmm.url + '/config/dmm_db', options, function ( error, response, body ) {
        if ( error ) {
            logger.error( 'could not configure DB params; error:', error );
            done( error );
        } else {
            var mongoConnectionString = 'mongodb://' + body.mongo_url + ':' + body.mongo_port + '/' + body.mongo_name;
            logger.debug('connect to mongo at', mongoConnectionString);
            mongo_client.connect( mongoConnectionString, function ( err, db ) {
                if ( err ) {
                    logger.error( 'could not connect to mongo at', body.mongo_url, ":", body.mongo_port, 'error:', err );
                    done( err );
                    return;
                }

                // drop the db, so everything is clean for each suite
                db.dropDatabase( function ( err2 ) {
                    if ( err2 ) {
                        logger.error( 'could not drop DB; error:', err2 );
                        done( err2 );
                    } else {
                        exports.mongo_db = db;
                        exports.mongoConnectionString = mongoConnectionString;
                        done();
                    }
                } );
            } );
        }
    } );
}

function load_data_models( done ) {
    var dir = './test/datamodels';

    fs.readdir( dir, function ( err, files ) {
        if (err) {
            done(err);
        } else {
            var dm_files = _.filter(files, function (fn) {
                return fn.match(/.*json$/);
            });
            async.each(dm_files, function (fn, cb) {
                jfile.readFile([dir, fn].join('/'), function (err2, dm) {
                    if (dm) {
                        var dm_name = fn.split('.json', 1)[0];
                        logger.debug('registering DM=%s with dm_resolver', dm_name, "url:", service_table.dmm.url + '/dm_resolver/datamodels/' + dm_name);
                        request.put(service_table.dmm.url + '/dm_resolver/datamodels/' + dm_name, {
                                json: dm
                            },
                            function (error, response) {
                                if (error) {
                                    logger.error('error in pushing to:%s', response.url, error);
                                }
                                cb();
                            });
                    } else {
                        cb(err2);
                    }
                });
            }, function (err3) {
                if (err3) {
                    logger.fatal('failed loading test data models\n', err3);
                }
                done();
            });
        }
    } );
}

function configure_zmq_listen( done ) {
    sock.on( 'connect', function zmqConnectEvent() {
        logger.debug( "Connected to ZMQ" );
    } );
    logger.trace( util.inspect( service_table[ 'golden-acs' ] ) );
    sock.connect( 'tcp://127.0.0.1:' + service_table[ 'golden-acs' ].zmqPort );
    sock.subscribe( 'TR69Event' );
    sock.on( 'message', function handleTR69Events( topic, message ) {
        logger.debug( 'ZMQ  received a message related to:', topic.toString( 'utf8' ), ', (open trace for more info)' );
        logger.trace( 'ZMQ  received a message related containing message:', message.toString( 'utf8' ) );
        goldenAcsEvents.emit( topic.toString( 'utf8' ), JSON.parse( message.toString( 'utf8' ) ) );
    } );
    done();
}

function getFreePortDouble( cb ) {
    var server = net.createServer();
    server.listen( 0, function () {
        var port = this.address().port;
        var server1 = net.createServer();
        server1.listen(0, function() {
            var port1 = this.address().port;

            server.close( function () {
                server1.close( function () {
                    cb( port, port1 );
                });
            });
        });
    });
}

function getFreePort( cb ) {
    var server = net.createServer();
    server.listen( 0, function () {
        var port = this.address().port;
        server.close( function () {
            cb( port );
        } );
    } );
}

// fires up a node child process and assigns it a random port
function fire_up( srv_list, done ) {

    function srv_starter( srv_args, cb ) {
        var portsKey = [];

        for ( var key in srv_args ) {
            //	logger.trace("Found key")
            if ( key.search( /port$/gi ) !== -1 && srv_args[ key ] === 0 ) {
                portsKey.push( key );
            }
        }

        async.eachSeries( portsKey, function ( item, callback ) {
            getFreePort( function ( port ) {
                srv_args[ item ] = port;
                callback();
            } );
        }, function () {
            var cwd = srv_args.cwd || './';
            var args = srv_args.args || '';
            args = _.template( args, srv_args );
            logger.debug( 'firing up %s args:%s', srv_args.name, args );
            srv_args.url = 'http://localhost:' + srv_args.port;
            srv_args.proc = fork( fs.realpathSync( srv_args.path ), args.split( ' ' ), {
                cwd: cwd,
                stdio: 'inherit'
            } );
            logger.debug( 'fire up end' );
            cb( null, srv_args );
        } );
    }

    var gacs_up = false;

    async.map( srv_list, srv_starter, function ( err, results ) {
        if ( !err ) {
            _.forEach( results, function ( srv ) {
                rv[ srv.name ] = srv;
            } );
        }
        rv.dmm.proc.on('message', function(msg) {
            if (msg === 'running') {
                logger.debug( 'Got DMM RUNNING');
                if (gacs_up) {
                    done( null, rv );
                } else {
                    var retryCount = 50;
                    // If Golden-ACS is not up, then wait for some time for it to be up
                    async.retry( retryCount, function(callback) {
                            if (gacs_up) {
                                callback(null, null);
                            } else {
                                setTimeout( function() {
                                    callback('GOLDEN-ACS process is still not UP', null);
                                }, 200);
                            }
                        },
                        function( error ) {
                            if ( error ) {
                                logger.error( error);
                                done( error, rv);
                            } else {
                                done( null, rv );
                            }
                        });
                }
            }
        });
        rv['golden-acs'].proc.on('message', function(msg) {
            if (msg === 'running') {
                logger.debug( 'Got GOLDEN-ACS RUNNING');
                gacs_up = true;
            }
        });
    } );
}


var curMachineIp = '';
var ifaces = os.networkInterfaces();
function getMachineIp(details) {
    if ( details.family === 'IPv4' ) {
        logger.debug( 'DEV:', this.dev, 'Alias:', (this.alias ? ':' + this.alias : ''), 'ADDRESS:', details.address );
        if ( this.dev === 'Local Area Connection' || this.dev === 'bond0' ) {
            curMachineIp = details.address;
        }
        ++this.alias;
    }
}
for ( var dev in ifaces ) {
    if ( ifaces.hasOwnProperty( dev ) ) {
        ifaces[ dev ].forEach( getMachineIp, {dev: dev, alias: 0} );
    }
}

exports.launch_services = function ( done ) {
    usingTestCount++;
    logger.trace('launch_services: suites using services:', usingTestCount);
    if (usingTestCount > 1) {
        done();
    } else {
        getFreePortDouble(function(xmpp_client_port, xmpp_component_port) {
            launchServices(xmpp_client_port, xmpp_component_port, done);
        });
    }
};

function launchServices(xmpp_client_port, xmpp_component_port, done) {

    // var dmm_config = {
    //     DMM: {
    //         xmpp_url: 'localhost',
    //         xmpp_port: xmpp_component_port,
    //         dmm_host: curMachineIp || 'localhost',
    //         bmi_url: '',
    //         bmi_proxy: { host: null},
    //         dmm_lb: { hostname: null, port: null },
    //         cpe_logs: {
    //             file: './cpe_log.log'
    //         }
    //     }
    // };

    // try {
    //     fs.truncateSync('./src/cpe_log.log', 0);
    // } catch (err) {
    //     logger.debug('file ./src/cpe_log.log does not exist');
    // }

    var services = [
    //{
    //     name: 'dmm',
    //     path: './src/dmm.js',
    //     cwd: './src/',
    //     args: ' -p <%= port %> --NODE_CONFIG=' + JSON.stringify(dmm_config),
    //     port: 0
    // },
      {
        name: 'robohydra',
        path: './node_modules/robohydra/bin/robohydra.js',
        //args: '-n -I test/robohydra -P data_source_plugin,upm_plugin -p <%= port %>'
        cwd: './test/robo/',
        args: 'robohydra.conf -p <%= port %> -x ' + xmpp_client_port,
        port: 0
     }
     //{
    //     name: 'golden-acs',
    //     path: './test/golden_acs/golden-acs.js',
    //     cwd: './test/golden_acs',
    //     args: '-p <%= port %> -z <%= zmqPort %>',
    //     port: 0,
    //     zmqPort: 0
    // }
    ];

    logger.info("SERVICES started");
    logger.trace("INSIDE SERVICES printing args: ", util.inspect(services));

    // fire up child processes
    fire_up(services, function(error, srv_table) {
    	logger.info("SERVICES started srv_table" + srv_table);
        if ( error) {
            done(error);
        } else {
            // we reach this point after all child processes fired up
            // and dmm app is listening

            // export the service table
            service_table = srv_table;

            // call several env configurators
            async.parallel([configure_robohydra_urls],
                // async.parallel([load_ccpmDataSource, configure_acs_url, configure_dataSource_url,
                //     load_data_models, create_mongo_client, configure_zmq_listen, configure_dmm_url,
                //     configure_robohydra_urls
                // ]
                function(err) {
                    done(err);
                });
        }
    });
}

exports.get_service = function ( name ) {
    return service_table[ name ];
};

exports.service_url = function() {
    var parts = Array.prototype.slice.call(arguments);
    parts[0] = exports.get_service(parts[0]).url;
    return parts.join('/');
};

function collect_coverage(done) {
    var isCoverageEnabled = (process.env.COVERAGE === "true");
    if (isCoverageEnabled) {
        var downloadUrl = service_table.dmm.url + '/coverage/download';
        request(downloadUrl)
            .pipe(fs.createWriteStream('coverage.zip'))
                .on('close', function () {
                    logger.debug("coverage file written!");
                    done();
                });
    } else {
        done();
    }
}

exports.kill_services = function ( done ) {
    usingTestCount--;
    logger.trace('kill_services: suites using services:', usingTestCount);
    if (usingTestCount === 0) {
        collect_coverage( function () {
            _.forEach( rv, function ( srv ) {
                srv.proc.kill();
            });
            done();
        });
    } else {
        done();
    }
};

exports.load_dataSourceDevice = function ( deviceId, product_class, done ) {
    var dir = './test/datasources';
    var fn = product_class + '.json';
    jfile.readFile( [ dir, fn ].join( '/' ), function ( err, ds ) {
        if ( ds ) {
            logger.debug( 'registering data source with product class=%s with device Id=%s', product_class, deviceId );
            request.post( service_table.robohydra.url + '/loadDeviceData/' + deviceId, {
                    json: ds
                },
                function ( error, response ) {
                    if ( error ) {
                        logger.error( 'failed loading data source to robohydra sim', error );
                        done( error );
                    } else if (response.statusCode >= 400) {
                        logger.error('failed loading data source to robohydra sim, response:', response.statusCode);
                        done(new Error('load data source device returned ' + response.statusCode));
                    } else {
                        done();
                    }
                } );
        }
        if ( err ) {
            logger.error( 'failed to find data source file of ', product_class );
            done( err );
        }

    } );
};

exports.loadGroup_dataSourceDevice = function ( devices, product_class, done ) {
    var dir = './test/datasources';
    var fn = product_class + '.json';
    jfile.readFile( [ dir, fn ].join( '/' ), function ( err, ds ) {
        if ( err ) {
            logger.error( 'failed to find data source file of ', product_class );
            done( err );
        }
        if ( ds ) {
            async.each(devices, function(deviceId, callback) {
                logger.debug( 'registering data source with product class=%s with device Id=%s', product_class, deviceId );
                request.post( service_table.robohydra.url + '/loadDeviceData/' + deviceId, {
                    json: ds
                },
                function ( error, response ) {
                    if (error) {
                        logger.error( 'failed loading data source to robohydra sim', error );
                        callback(error);
                    } else if (response.statusCode >= 400) {
                        logger.error('failed loading data source to robohydra sim, response:', response.statusCode);
                        callback(new Error('load data source device returned ' + response.statusCode));
                    } else {
                        setTimeout( function () {
                            callback();
                        }, 20 ); // wait for CLIENT ONLINE - TODO need to change the code to send response only when online
                    }
                });
            },
            function(err2) {
                done(err2);
            });
        }
    });
};

exports.load_upmDataSource = function ( deviceId, householdId, done ) {
    if (typeof householdId === 'function') {
        done = householdId;
        householdId = null;
    }
    var ds = clone(upm_obj);
    if ( ds ) {
        ds.UPMData.householdId = householdId;
        ds.UPMData.deviceId = deviceId;
        logger.debug( 'registering upm data with device Id=%s', deviceId );
        request.post( service_table.robohydra.url + '/loadUpmData/' + deviceId, {
                json: ds
            },
            function ( error ) {
                if ( error ) {
                    logger.error( 'failed loading upm data to robohydra sim', error );
                    done( error );
                } else {
                    done();
                }
            } );
    } else {
        logger.error( 'failed to find upm data source file' );
        done(new Error('failed to find upm data source file '));
    }
};

exports.load_upmDataSourceCommunity = function ( deviceId, householdId, community, distribution, done) {
    if (typeof householdId === 'function') {
        done = householdId;
        householdId = null;
    }

    var ds = clone(upm_obj);
    if ( ds ) {
        ds.UPMData.householdId = householdId;
        ds.UPMData.deviceId = deviceId;
        ds.UPMData.community = community;
        ds.UPMData.ccDistributionVersion = distribution;
        logger.debug( 'registering upm data with device Id=%s and household=%s', deviceId, householdId );
        request.post( service_table.robohydra.url + '/loadUpmData/' + deviceId, {
                json: ds
            },
            function ( error ) {
                if ( error ) {
                    logger.error( 'failed loading upm data to robohydra sim', error );
                    done( error );
                } else {
                    done();
                }
            } );
    } else {
        logger.error( 'failed to find upm data source file' );
        done(new Error('failed to find upm data source file '));
    }
};

exports.loadGroup_upmDataSource = function (devices, done ) {
    var ds = clone(upm_obj);
    if ( ds ) {
        async.each(devices, function(deviceId, callback) {
            logger.debug( 'registering upm data with device Id=%s', deviceId );
            request.post( service_table.robohydra.url + '/loadUpmData/' + deviceId, {
                json: ds
            },
            function ( error ) {
                if (error) {
                    logger.error( 'failed loading upm data to robohydra sim', error );
                    callback(error);
                } else {
                    callback();
                }
            });
        },
        function(err2) {
            done(err2);
        });
    } else {
        logger.error( 'failed to find upm data source file' );
        done(new Error('failed to find upm data source file '));
    }
};

exports.clobber_riDataSource = function ( deviceId, done ) {

    logger.debug( 'File refresh for RI');
    request.post( service_table.robohydra.url + '/ri/clobber', {
        body: '../../test/datasources/RI.json'
    },
    function ( error ) {
        if ( error ) {
            logger.error( 'failed loading pps data to robohydra sim', error );
            done( error );
        } else {
            done();
        }
    });
};

exports.sendScanInfo_riDataSource = function ( deviceId, done ) {

    logger.debug( 'File refresh for RI');
    request.post( service_table.robohydra.url + '/ri/scanInfo', {
        body: '../../test/datasources/RI.json',
        qs: { 'deviceId': deviceId }
    },
    function ( error ) {
        if ( error ) {
            logger.error( 'failed loading RI data to robohydra sim', error );
            done( error );
        } else {
            done();
        }
    });
};

exports.load_ppsDataSource = function ( deviceId, done ) {

    logger.debug( 'registering pps data with device Id=%s', deviceId );
    request.post( service_table.robohydra.url + '/loadPpsData/' + deviceId, {
        body: '../../test/datasources/PPS.json'
    },
        function ( error ) {
            if ( error ) {
                logger.error( 'failed loading pps data to robohydra sim', error );
                done( error );
            } else {
                done();
            }
        });
};

exports.load_cmdcDataSource = function ( done ) {

    logger.debug( 'registering cmdc data' );
    request.post( service_table.robohydra.url + '/loadCMDCData', {
        body: '../../test/datasources/CMDC.json'
    },
    function ( error ) {
        if ( error ) {
            logger.error( 'failed loading CMDC data to robohydra sim', error );
            done( error );
        } else {
            done();
        }
    });
};

exports.load_smDataSource = function ( done ) {

    logger.debug( 'registering sm data' );
    request.post( service_table.robohydra.url + '/loadSMData', {
        body: '../datasources/SM.json'
    },
    function ( error ) {
        if ( error ) {
            logger.error( 'failed loading SM data to robohydra sim', error );
            done( error );
        } else {
            done();
        }
    });
};

exports.load_lcsDataSource = function ( done ) {

    logger.debug( 'registering lcs data' );
    request.post( service_table.robohydra.url + '/loadLCSData', {
            body: '../datasources/LCS.json'
        },
        function ( error ) {
            if ( error ) {
                logger.error( 'failed loading LCS data to robohydra sim', error );
                done( error );
            } else {
                done();
            }
        });
};


function load_ccpmDataSource( done ) {
    var file_path = './test/datasources/CCPM.json';
    jfile.readFile( file_path, function ( err, ds ) {
        if ( ds ) {
            request.post( service_table.robohydra.url + '/loadCcpmData', {
                    json: {
                        ds: ds,
                        cdn_prefix: service_table.robohydra.url
                    }
                },
                function ( error ) {
                    if ( error ) {
                        logger.error( 'failed loading ccpm data to robohydra sim', error );
                        done( error );
                    } else {
                        done();
                    }
                } );
        }
        if ( err ) {
            logger.error( 'failed to find ccpm data source file' );
            done( err );
        }

    } );
}

exports.goldenAcsEvents = goldenAcsEvents;
exports.productClass = 'IH_PEARL';
