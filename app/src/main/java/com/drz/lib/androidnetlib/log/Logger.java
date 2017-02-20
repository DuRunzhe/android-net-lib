package com.drz.lib.androidnetlib.log;

import android.util.Log;


/**
 * @author 杜润哲{a12345570@163.com}
 * @date 2017/2/20 0020  22:14
 */
public class Logger {
    private static final String INFO = "info";
    private static final String DEBUG = "debug";
    private static final String ERROR = "error";

    private Logger() {
    }

    private static boolean logOutPut = true;

    /**
     * @param msg
     */
    public static void info(String msg) {
        if (logOutPut) {
            Log.i(INFO, msg);
        }
    }

    public static void debug(String msg) {
        if (logOutPut) {
            Log.d(DEBUG, msg);
        }
    }

    public static void error(String msg) {
        if (logOutPut) {
            Log.e(ERROR, msg);
        }
    }
//
//    private Logger getLogger() {
//        return LoggerHolder.logger;
//    }
//
//    static class LoggerHolder {
//        static final Logger logger = new Logger();
//    }
}
