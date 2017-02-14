package com.example.geomslayer.hseproject.base;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import okhttp3.OkHttpClient;

public class BaseApp extends Application {

    private static OkHttpClient client;

    public static OkHttpClient getHttpClient() {
        return client;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(new FlowConfig.Builder(this).build());
        client = new OkHttpClient();
//        deleteDatabase(AppDatabase.NAME + ".db");
//        StatsManager.saveManager(getSharedPreferences(StatsManager.PREF_NAME, 0), null);

    }
}
