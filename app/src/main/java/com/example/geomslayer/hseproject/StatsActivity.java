package com.example.geomslayer.hseproject;

import android.os.Bundle;

public class StatsActivity extends BaseActivity {

    @Override
    int getLayoutResource() {
        return R.layout.activity_stats;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
