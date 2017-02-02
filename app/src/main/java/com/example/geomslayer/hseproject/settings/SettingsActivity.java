package com.example.geomslayer.hseproject.settings;

import android.os.Bundle;

import com.example.geomslayer.hseproject.BaseActivity;
import com.example.geomslayer.hseproject.R;

public class SettingsActivity extends BaseActivity {

    @Override
    public int getLayoutResource() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
