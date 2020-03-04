package com.example.starter_proj;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {
    /**
     * This method just exists here to override as final so that we can be sure we handle this case.
     */
    @Override
    protected final void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
