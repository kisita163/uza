package com.kisita.uza.activities;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;

import com.kisita.uza.BuildConfig;
import com.lukekorth.mailable_log.MailableLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/*
 * Created by HuguesKi on 10-04-18.
 */

public class BikekoApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static String TAG = "### BikekoApplication";
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    @Override
    public void onCreate() {
        Log.i(TAG,"onCreate");
        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectActivityLeaks()
                    .detectLeakedClosableObjects()
                    .detectLeakedRegistrationObjects()
                    .detectLeakedSqlLiteObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/

        super.onCreate();
        MailableLog.init(this, BuildConfig.DEBUG);
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Logger logger = LoggerFactory.getLogger("Exception");

        logger.error("thread.toString(): " + thread.toString());
        logger.error("Exception: " + ex.toString());
        logger.error("Exception stacktrace:");
        for (StackTraceElement trace : ex.getStackTrace()) {
            logger.error(trace.toString());
        }

        logger.error("");

        logger.error("cause.toString(): " + ex.getCause().toString());
        logger.error("Cause: " + ex.getCause().toString());
        logger.error("Cause stacktrace:");
        for (StackTraceElement trace : ex.getCause().getStackTrace()) {
            logger.error(trace.toString());
        }
        mDefaultExceptionHandler.uncaughtException(thread, ex);
    }
}
