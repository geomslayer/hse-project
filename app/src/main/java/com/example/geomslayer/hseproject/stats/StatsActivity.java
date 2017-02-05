package com.example.geomslayer.hseproject.stats;

import android.os.Bundle;
import android.widget.TextView;

import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.storage.Topic;
import com.example.geomslayer.hseproject.storage.Topic_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

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
        Topic topicModel = SQLite.select()
                .from(Topic.class)
                .where(Topic_Table.id.eq(statsManager.getPopularTopicId()))
                .querySingle();
        String topicText = (topicModel == null ? "Undefined" : topicModel.text);
        String text = getResources().getString(R.string.all_read) + ": " + statsManager.getAllRead() + "\n" +
                getResources().getString(R.string.all_answered) + ": " + statsManager.getAllAnswered() + "\n" +
                getResources().getString(R.string.answered_right) + ": " + statsManager.getAnsweredRight() + "\n" +
                getResources().getString(R.string.favorit_topic) + ": " + topicText;
        ((TextView) findViewById(R.id.txt_stats)).setText(text);
    }
}
