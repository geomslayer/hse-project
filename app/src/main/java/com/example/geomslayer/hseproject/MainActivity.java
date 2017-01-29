package com.example.geomslayer.hseproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.geomslayer.hseproject.data.NewsContract;
import com.example.geomslayer.hseproject.data.NewsContract.NewsEntry;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private ListView listViewNews;
    private Spinner spinnerTopics;

    @Override
    int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: activity created!");

        Toolbar toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        spinnerTopics = (Spinner) toolbar.findViewById(R.id.toolbar_spinner);

        listViewNews = (ListView) findViewById(R.id.list_news);
        listViewNews.setOnItemClickListener((parent, view, position, id) -> {
            Intent readIntent = new Intent(this, ReadActivity.class);
            readIntent.putExtra(NewsEntry._ID, id);
            startActivity(readIntent);
        });

        loadTopics();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: activity restarted!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: activity stopped!");
    }

    private void loadTopics() {
        // Load all topics
        Cursor topicsCursor = getContentResolver().query(
                NewsContract.TopicEntry.CONTENT_URI, null, null, null, null);
        SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, topicsCursor,
                new String[]{NewsContract.TopicEntry.COLUMN_BODY},
                new int[]{android.R.id.text1}, 0);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopics.setAdapter(spinnerAdapter);

        spinnerTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                loadNews(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Shows news on the main screen on a listView
    private void loadNews(long topicId) {
        Cursor cursor = getContentResolver().query(
                NewsEntry.buildSimpleNewsUri(), null,
                NewsEntry.COLUMN_TOPIC_ID + " = " + topicId,
                null, NewsEntry.COLUMN_DATE + " DESC");
        listViewNews.setAdapter(new NewsAdapter(this, cursor));
    }

}
