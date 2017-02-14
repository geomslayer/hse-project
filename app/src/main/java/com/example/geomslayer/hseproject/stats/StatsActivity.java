package com.example.geomslayer.hseproject.stats;

import android.os.Bundle;
import android.widget.TextView;

import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.base.BaseApp;
import com.example.geomslayer.hseproject.networking.Category;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class StatsActivity extends BaseActivity {

    private StatsManager statsManager;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_stats;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        statsManager = StatsManager.getSavedManager(
                getSharedPreferences(StatsManager.PREF_NAME, MODE_PRIVATE));
        showStats();
    }

    private void showStats() {
        final String text = getResources().getString(R.string.all_read) + ": " + statsManager.getAllRead() + "\n" +
                getResources().getString(R.string.all_answered) + ": " + statsManager.getAllAnswered() + "\n" +
                getResources().getString(R.string.answered_right) + ": " + statsManager.getAnsweredRight() + "\n" +
                getResources().getString(R.string.favorit_topic) + ": ";

        long catId = statsManager.getPopularTopicId();
        if (catId == -1) {
            ((TextView) findViewById(R.id.txt_stats)).setText(text + "Не определено");
        } else {

            Request request = new Request.Builder()
                    .url("http://52.36.210.200/categories/" + statsManager.getPopularTopicId())
                    .build();

            BaseApp.getHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(final Call call, Response response) throws IOException {
                    final Category cat = new Gson().fromJson(response.body().string(), Category.class);
                    StatsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) findViewById(R.id.txt_stats)).setText(text + cat.text);
                        }
                    });
                }
            });
        }
    }
}
