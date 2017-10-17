var _ = require( 'lodash' ),
    heads = require( 'robohydra' ).heads,
    RoboHydraHead = heads.RoboHydraHead,
    RoboHydraHeadStatic = heads.RoboHydraHeadStatic,
    logger = require( '../log4js-config'),
    jfile = require( 'jsonfile' );

logger.getLogger( 'SAT_PLUGIN' );
logger.info('flow_context init');
    
exports.getBodyParts = function ( conf ) {
    var devices_data = {};

    return {
        heads: [
            new RoboHydraHead( {
                path: "/sat/1.0.0/.*",
                handler: function ( req, res ) {
                	   logger.debug( "request arrived: " +  req.method + "prama"+ req.queryParams );
                        if (req.method === 'DELETE') {    
                            if (req.queryParams.profileId === 'UNKNOWN_PROFILE') {
                                res.statusCode = 404;
                                res.send("Profile not found");
                            } else if (req.queryParams.profileId === 'ERROR_PROFILE') {
                                res.statusCode = 500;
                                res.send("General Server error");
                            } else {
                                res.statusCode = 204;
                                res.send();
                            }
                        } else {
                            res.statusCode = 500;
                            res.send("Unknown method");
                        }
                    }
            })
            ]
            };
};
