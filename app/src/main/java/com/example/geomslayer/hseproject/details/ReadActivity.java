package com.example.geomslayer.hseproject.details;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.data.NewsContract.NewsEntry;
import com.example.geomslayer.hseproject.data.NewsContract.OptionEntry;
import com.example.geomslayer.hseproject.data.NewsContract.TopicEntry;
import com.example.geomslayer.hseproject.stats.StatsManager;

public class ReadActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ReadActivity";

    private long id;
    private StatsManager statsManager;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_read;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statsManager = StatsManager.getSavedManager(
                getSharedPreferences(StatsManager.PREF_NAME, MODE_PRIVATE));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getLongExtra(NewsEntry._ID, 0L);

        if (id == 0) {
            throw new NullPointerException("Got default parameter in extras!");
        }

        Uri newsUri = NewsEntry.buildFullNewsUri(id);
        Cursor newsCursor = getContentResolver().query(newsUri, null, null, null, null);
        displayNews(newsCursor);

        Uri optionsUri = OptionEntry.buildUriWithNewsId(id);
        final String SQL_ORDER = "RANDOM()";
        Cursor optionsCursor = getContentResolver().query(optionsUri, null, null, null, SQL_ORDER);
        displayOptions(optionsCursor);

        long topicId = newsCursor.getLong(newsCursor.getColumnIndexOrThrow(NewsEntry._ID));
        statsManager.readNews(topicId);
    }

    @Override
    protected void onPause() {
        StatsManager.saveManager(
                getSharedPreferences(StatsManager.PREF_NAME, MODE_PRIVATE), statsManager);
        super.onPause();
    }

    private void displayNews(Cursor cursor) {
        cursor.moveToFirst();
        ((TextView) findViewById(R.id.txt_topic)).setText(
                cursor.getString(cursor.getColumnIndexOrThrow(TopicEntry.COLUMN_BODY)));
        ((TextView) findViewById(R.id.txt_title)).setText(
                cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_TITLE)));
        ((TextView) findViewById(R.id.txt_content)).setText(
                cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_CONTENT)));
        ((TextView) findViewById(R.id.txt_date)).setText(
                cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_DATE)));
        ((TextView) findViewById(R.id.txt_question)).setText(
                cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_QUESTION)));
    }

    private void displayOptions(Cursor cursor) {
        if (cursor.moveToFirst()) {
            LinearLayout layoutOptions = (LinearLayout) findViewById(R.id.option_list);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int IS_ANSWER_INDEX = cursor.getColumnIndexOrThrow(OptionEntry.COLUMN_IS_ANSWER);
            final int BODY_INDEX = cursor.getColumnIndexOrThrow(OptionEntry.COLUMN_BODY);
            do {
                Button option = new Button(this);
                option.setTag(cursor.getInt(IS_ANSWER_INDEX));
                option.setText(cursor.getString(BODY_INDEX));
                option.setOnClickListener(this);
                layoutOptions.addView(option, layoutParams);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onClick(View view) {
        int tag = (int) view.getTag();
        if (tag != 0) {
            Snackbar.make(findViewById(R.id.activity_read), R.string.right, Snackbar.LENGTH_SHORT).show();
            statsManager.answer(true);
        } else {
            Snackbar.make(findViewById(R.id.activity_read), R.string.wrong, Snackbar.LENGTH_SHORT).show();
            statsManager.answer(false);
        }
    }
}
