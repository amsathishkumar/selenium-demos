var services = require( './services' ),
    assert = require( 'assert' ),
    async = require('async'),
    request = require( 'request' ),
    logger = require( './log4js-config' ).logging.getLogger( 'Mocha-Test' ),
    _ = require( 'lodash' );
var chai = require( 'chai' );
var should = chai.should();
chai.use( require( 'chai-things' ) );


describe( 'VFPS Server Test Suite', function () {
    describe( 'VFPS Server test', function () {
        it( 'should test GPV for resetChannelList', function ( done ) {
          services.launch_services();
          // console.log(services.get_service('robohydra').url)
           });

            
        });
    });


