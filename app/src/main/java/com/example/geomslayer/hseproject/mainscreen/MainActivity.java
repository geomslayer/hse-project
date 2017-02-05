package com.example.geomslayer.hseproject.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.details.ReadActivity;
import com.example.geomslayer.hseproject.storage.News;
import com.example.geomslayer.hseproject.storage.News_Table;
import com.example.geomslayer.hseproject.storage.Topic;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView rvNews;
    private Spinner spinnerTopics;
    private ArrayList<News> newsList;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareView();
        loadTopics();
    }

    private void prepareView() {

        spinnerTopics = (Spinner) findViewById(R.id.sp_topics);
        spinnerTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                loadNews(((Topic) parent.getItemAtPosition(pos)).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        rvNews = (RecyclerView) findViewById(R.id.rv_news);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
    }

    // Load all topics
    private void loadTopics() {
        ArrayList<Topic> topics = new ArrayList<>(
                SQLite.select().from(Topic.class).queryList());
        spinnerTopics.setAdapter(new TopicAdapter(this, topics));
    }

    // Shows news on the main screen on a listView
    private void loadNews(long topicId) {
        newsList = new ArrayList<>(SQLite.select()
                .from(News.class)
                .where(News_Table.topic_id.eq(topicId))
                .queryList());
        NewsAdapter adapter = new NewsAdapter(this, newsList);
        adapter.setOnitemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent readIntent = new Intent(MainActivity.this, ReadActivity.class);
                readIntent.putExtra(ReadActivity.EXTRA_NEWS, newsList.get(position).id);
                startActivity(readIntent);
            }
        });
        rvNews.setAdapter(adapter);
    }

}
