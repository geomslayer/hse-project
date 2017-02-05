package com.example.geomslayer.hseproject.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.data.NewsContract.NewsEntry;
import com.example.geomslayer.hseproject.details.ReadActivity;
import com.example.geomslayer.hseproject.storage.Topic;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private ListView listViewNews;
    private Spinner spinnerTopics;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: activity created!");

        spinnerTopics = (Spinner) findViewById(R.id.toolbar_spinner);
        listViewNews = (ListView) findViewById(R.id.list_news);

        prepareView();

        loadTopics();
    }

    private void prepareView() {
        spinnerTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                loadNews(((Topic) parent.getItemAtPosition(pos)).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent readIntent = new Intent(MainActivity.this, ReadActivity.class);
                readIntent.putExtra(NewsEntry._ID, id);
                startActivity(readIntent);
            }
        });
    }

    private void loadTopics() {
        // Load all topics
//        Cursor topicsCursor = getContentResolver().query(
//                NewsContract.TopicEntry.CONTENT_URI, null, null, null, null);
//        SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(
//                this, android.R.layout.simple_spinner_item, topicsCursor,
//                new String[]{NewsContract.TopicEntry.COLUMN_BODY},
//                new int[]{android.R.id.text1}, 0);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTopics.setAdapter(spinnerAdapter);

        ArrayList<Topic> topics = new ArrayList<>(
                SQLite.select().from(Topic.class).queryList());
        spinnerTopics.setAdapter(new TopicAdapter(this, topics));
    }

    // Shows news on the main screen on a listView
    private void loadNews(long topicId) {
//        Cursor cursor = getContentResolver().query(
//                NewsEntry.buildSimpleNewsUri(), null,
//                NewsEntry.COLUMN_TOPIC_ID + " = " + topicId,
//                null, NewsEntry.COLUMN_DATE + " DESC");
//        listViewNews.setAdapter(new NewsAdapter(this, cursor));


    }

}
