package com.example.leaflet_android;

import android.app.Application;
import android.content.Context;

public class LeafletApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
