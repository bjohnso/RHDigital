package com.rhdigital.rhclient;

import android.app.Application;

public class RHApplication extends Application {

    private volatile RHApplication INSTANCE;

    private RHApplication() {}

    public RHApplication getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RHApplication();
        }
        return INSTANCE;
    }
}
