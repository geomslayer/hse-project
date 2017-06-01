package com.example.geomslayer.hseproject.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.details.ReadActivity;
import com.example.geomslayer.hseproject.networking.Article;
import com.example.geomslayer.hseproject.networking.Category;
import com.example.geomslayer.hseproject.stats.StatsManager;
import com.example.geomslayer.hseproject.storage.Info;
import com.example.geomslayer.hseproject.storage.Info_Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.sql.language.SQLite;

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
    private static final String BASE_URL = "http://52.36.210.200/";

    private RecyclerView rvNews;
    private Spinner spinnerTopics;
    private ArrayList<Article> newsList;
    private ArrayList<Category> catsList;
    private LinearLayoutManager layoutManager;
    private StatsManager statsManager;
    private boolean canLoad = false;
    private ViewGroup placeholder;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placeholder = (ViewGroup) findViewById(R.id.no_connection);

        prepareView();
        loadTopics();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        statsManager = StatsManager.getSavedManager(
                getSharedPreferences(StatsManager.PREF_NAME, 0));
    }

    @Override
    protected void onPause() {
        super.onPause();

        StatsManager.saveManager(getSharedPreferences(StatsManager.PREF_NAME, 0), statsManager);
    }

    private void prepareView() {
        spinnerTopics = (Spinner) findViewById(R.id.sp_topics);
        spinnerTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (canLoad) {
                    loadNews(((Category) parent.getItemAtPosition(pos)).id);
                }
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
                .url(BASE_URL + "categories")
                .build();
        getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPlaceholderVisibility(true);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Type type = new TypeToken<ArrayList<Category>>() {}.getType();
                catsList = new Gson().fromJson(response.body().string(), type);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPlaceholderVisibility(false);
                        spinnerTopics.setAdapter(new TopicAdapter(MainActivity.this, catsList));
                        long favCatId = statsManager.getPopularTopicId();
                        canLoad = true;
                        for (int i = 0; i < catsList.size(); ++i) {
                            if (catsList.get(i).id == favCatId) {
                                spinnerTopics.setSelection(i);
                                break;
                            }
                        }
                    }
                });
            }
        });
    }

    Category getCurrentCategory() {
        return catsList.get(spinnerTopics.getSelectedItemPosition());
    }

    LinearLayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(this);
        }
        return layoutManager;
    }

    // Shows news on the main screen on a listView
    private void loadNews(long topicId) {
        if (newsList != null && !newsList.isEmpty()) {
            newsList.clear();
            rvNews.getAdapter().notifyDataSetChanged();
        }

        String url = BASE_URL + "news/" + topicId;
        Request request = new Request.Builder()
                .url(url)
                .build();
        getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPlaceholderVisibility(true);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Type type = new TypeToken<ArrayList<Article>>() {}.getType();
                newsList = new Gson().fromJson(response.body().string(), type);
                for (Article art : newsList) {
                    Info info = SQLite.select().from(Info.class)
                            .where(Info_Table.id.eq(art.id)).querySingle();
                    art.status = info == null ? Article.NOT_READ : Article.WAS_READ;
                }
                final NewsAdapter adapter = new NewsAdapter(MainActivity.this, newsList);
                adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        Intent readIntent = new Intent(MainActivity.this, ReadActivity.class);
                        Article curArticle = newsList.get(position);
                        if (curArticle.status == Article.NOT_READ) {
                            curArticle.status = Article.WAS_READ;
                            Info info = new Info(curArticle.id);
                            info.save();
                            statsManager.readNews(curArticle.category);
                            rvNews.getAdapter().notifyItemChanged(position);
                        }
                        curArticle.categoryObj = getCurrentCategory();
                        readIntent.putExtra(ReadActivity.EXTRA_NEWS,
                                new Gson().toJson(curArticle, Article.class));
                        startActivity(readIntent);
                    }
                });
                final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                        rvNews.getContext(), layoutManager.getOrientation());
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPlaceholderVisibility(false);
                        rvNews.setAdapter(adapter);
                        rvNews.setLayoutManager(getLayoutManager());
                        rvNews.addOnScrollListener(scrollListener);
                        rvNews.addItemDecoration(dividerItemDecoration);
                    }
                });
            }
        });
    }

    private EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(
            getLayoutManager()) {
        @Override
        public void onLoadMore(int page, final int totalItemsCount, RecyclerView view) {
            Article lastArticle = newsList.get(totalItemsCount - 1);
            String url = BASE_URL + String.format("news/%d/%d/%d",
                    getCurrentCategory().id, lastArticle.date, lastArticle.id);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            getHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {}

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Type type = new TypeToken<ArrayList<Article>>() {}.getType();
                    final ArrayList<Article> newArticles = new Gson().fromJson(response.body().string(), type);
                    for (Article art : newArticles) {
                        Info info = SQLite.select().from(Info.class)
                                .where(Info_Table.id.eq(art.id)).querySingle();
                        art.status = info == null ? Article.NOT_READ : Article.WAS_READ;
                    }
                    if (!newArticles.isEmpty()) {
                        newsList.addAll(newArticles);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setPlaceholderVisibility(false);
                                rvNews.getAdapter().notifyItemRangeInserted(
                                        totalItemsCount, newArticles.size());
                            }
                        });
                    }
                }
            });
        }
    };

    private void setPlaceholderVisibility(boolean visible) {
        placeholder.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}
