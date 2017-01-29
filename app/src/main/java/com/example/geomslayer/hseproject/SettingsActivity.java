package com.example.geomslayer.hseproject;

import android.os.Bundle;

public class SettingsActivity extends BaseActivity {

    @Override
    int getLayoutResource() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
