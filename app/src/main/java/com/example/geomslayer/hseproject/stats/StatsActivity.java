package com.example.geomslayer.hseproject.stats;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.data.NewsContract;

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
        String topic;
        if (statsManager.getPopularTopicId() == -1) {
            topic = "Undefined";
        } else {
            Cursor cursor = getContentResolver().query(NewsContract.TopicEntry.CONTENT_URI,
                    null, NewsContract.TopicEntry._ID + " = ?",
                    new String[]{String.valueOf(statsManager.getPopularTopicId())}, null);
            cursor.moveToNext();
            topic = cursor.getString(cursor.getColumnIndexOrThrow(NewsContract.TopicEntry.COLUMN_BODY));
        }
        String text = getResources().getString(R.string.all_read) + ": " + statsManager.getAllRead() + "\n" +
                getResources().getString(R.string.all_answered) + ": " + statsManager.getAllAnswered() + "\n" +
                getResources().getString(R.string.answered_right) + ": " + statsManager.getAnsweredRight() + "\n" +
                getResources().getString(R.string.favorit_topic) + ": " + topic;
        ((TextView) findViewById(R.id.txt_stats)).setText(text);
    }
}
