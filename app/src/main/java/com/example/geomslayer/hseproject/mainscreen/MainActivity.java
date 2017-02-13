package com.example.geomslayer.hseproject.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.base.BaseApp;
import com.example.geomslayer.hseproject.details.ReadActivity;
import com.example.geomslayer.hseproject.networking.Article;
import com.example.geomslayer.hseproject.networking.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.geomslayer.hseproject.base.BaseApp.getHttpClient;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView rvNews;
    private Spinner spinnerTopics;
    private ArrayList<Article> newsList;
    private ArrayList<Category> catsList;

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
                loadNews(((Category) parent.getItemAtPosition(pos)).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        rvNews = (RecyclerView) findViewById(R.id.rv_news);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
    }

    // Load all topics
    private void loadTopics() {
        Request request = new Request.Builder()
                .url("http://52.36.210.200/categories")
                .build();
        getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: couldn't load categories.");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Type type = new TypeToken<ArrayList<Category>>() {}.getType();
                catsList = new Gson().fromJson(response.body().string(), type);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinnerTopics.setAdapter(new TopicAdapter(MainActivity.this, catsList));
                    }
                });
            }
        });
    }

    // Shows news on the main screen on a listView
    private void loadNews(long topicId) {
        if (newsList != null && !newsList.isEmpty()) {
            newsList.clear();
            rvNews.getAdapter().notifyDataSetChanged();
        }

        String url = "http://52.36.210.200/news/" + topicId;
        Request request = new Request.Builder()
                .url(url)
                .build();
        BaseApp.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: couldn't load news.");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Type type = new TypeToken<ArrayList<Article>>() {}.getType();
                newsList = new Gson().fromJson(response.body().string(), type);
                final NewsAdapter adapter = new NewsAdapter(MainActivity.this, newsList);
                adapter.setOnitemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        Intent readIntent = new Intent(MainActivity.this, ReadActivity.class);
//                        readIntent.putExtra(ReadActivity.EXTRA_NEWS, newsList.get(position).id);
                        Article curArticle = newsList.get(position);
                        curArticle.categoryObj = catsList.get(spinnerTopics.getSelectedItemPosition());
                        readIntent.putExtra(ReadActivity.EXTRA_NEWS,
                                new Gson().toJson(curArticle, Article.class));
                        startActivity(readIntent);
                    }
                });
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvNews.setAdapter(adapter);
                    }
                });
            }
        });

    }

}
