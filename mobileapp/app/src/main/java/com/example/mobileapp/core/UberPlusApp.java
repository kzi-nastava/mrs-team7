package com.example.mobileapp.core;

import android.app.Application;

import com.example.mobileapp.core.network.ApiClient;

public class UberPlusApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ApiClient.initialize(this);
    }
}
