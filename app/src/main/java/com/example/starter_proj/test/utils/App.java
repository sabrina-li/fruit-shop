package com.example.starter_proj.test.utils;

import android.content.Context;

import androidx.lifecycle.LifecycleObserver;
import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication implements LifecycleObserver {
    /**
     * This method just exists here to override as final so that we can be sure we handle this case.
     */
    @Override
    protected final void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
