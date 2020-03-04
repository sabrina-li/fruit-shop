package com.example.starter_proj;

import android.util.Log;

import com.fullstory.FS;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.HashMap;
import java.util.Map;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultHandler;

    UncaughtExceptionHandler() {
        // capture the existing default uncaught exception handler
        this.defaultHandler = (Thread.getDefaultUncaughtExceptionHandler());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            Exception e = FirebaseCrashlytics.getInstance().checkForUnsentReports().getException();
            Map<String,String> crashEvent = new HashMap<>();
            crashEvent.put("issueList","https://console.firebase.google.com/u/0/project/fs-crashlytics/crashlytics/app/android:com.example.helloworld_java/issues");
            FS.event("Crashed", crashEvent);
            Log.e("fullstory","crashed");
        } finally {
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
            }
        }
    }
}
