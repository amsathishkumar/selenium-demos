var log4js = require( 'log4js' );


var log4js_config = {

    "appenders": [ {
        "type": "logLevelFilter",
        "level": "DEBUG",
        "appender": {
            "type": "console",
            "layout": {
                "type": "pattern",
                "pattern": "%[%d{MM-dd hh:mm:ss.SSS} [%x{pid}] %-5p %-15c %] %m",
                "tokens": {
                    pid: function () {
                        return process.pid;
                    }
                }
            }
        }
    }, {
        "type": "logLevelFilter",
        "level": "DEBUG",
        "appender": {
            "type": "file",
            "filename": "robohydra.log",
            "layout": {
                "type": "pattern",
                "pattern": "%d{MM-dd hh:mm:ss.SSS} [%x{pid}] %-5p %-15c %m",
                "tokens": {
                    pid: function () {
                        return process.pid;
                    }
                }
            }
        }
    } ],

    replaceConsole: true
};

log4js.configure( log4js_config, {} );
exports.logging = log4js;
