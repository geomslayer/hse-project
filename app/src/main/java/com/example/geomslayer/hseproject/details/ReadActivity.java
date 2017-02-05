package com.example.geomslayer.hseproject.details;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.stats.StatsManager;
import com.example.geomslayer.hseproject.storage.News;
import com.example.geomslayer.hseproject.storage.News_Table;
import com.example.geomslayer.hseproject.storage.Option;
import com.example.geomslayer.hseproject.storage.Option_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class ReadActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ReadActivity";

    public static final String EXTRA_NEWS = "news_extra";

    private StatsManager statsManager;
    private News news;
    private ArrayList<Option> options;

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

        long newsId = getIntent().getLongExtra(EXTRA_NEWS, 0L);
        news = SQLite.select()
                .from(News.class)
                .where(News_Table.id.eq(newsId))
                .querySingle();

        options = new ArrayList<>(SQLite.select()
                .from(Option.class)
                .where(Option_Table.news_id.eq(news.id))
                .queryList());

        displayNews();
        displayOptions();

        if (!news.wasRead) {
            statsManager.readNews(news.topic.id);
            news.wasRead = true;
            news.update();
            Log.d(TAG, "onCreate: just read");
        } else {
            Log.d(TAG, "onCreate: already read");
        }
    }

    @Override
    protected void onPause() {
        StatsManager.saveManager(
                getSharedPreferences(StatsManager.PREF_NAME, MODE_PRIVATE), statsManager);
        super.onPause();
    }

    private void displayNews() {
        if (news == null) {
            return;
        }

        ((TextView) findViewById(R.id.txt_topic)).setText(news.topic.text);
        ((TextView) findViewById(R.id.txt_title)).setText(news.title);
        ((TextView) findViewById(R.id.txt_content)).setText(news.text);
        ((TextView) findViewById(R.id.txt_date)).setText(news.date.toString());
        ((TextView) findViewById(R.id.txt_question)).setText(news.question);
    }

    private void displayOptions() {
        if (options == null) {
            return;
        }

        LinearLayout layoutOptions = (LinearLayout) findViewById(R.id.option_list);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (Option option : options) {
            Button optionBtn = new Button(this);
            optionBtn.setTag(option.isAnswer);
            optionBtn.setText(option.text);
            optionBtn.setOnClickListener(this);
            layoutOptions.addView(optionBtn, layoutParams);
        }
    }

    @Override
    public void onClick(View view) {
        boolean isAnswer = (boolean) view.getTag();
        Snackbar.make(findViewById(R.id.activity_read),
                (isAnswer ? R.string.right : R.string.wrong),
                Snackbar.LENGTH_SHORT).show();
        if (news.answerStatus == StatsManager.NOT_ANSWERED) {
            statsManager.answer(isAnswer);
            news.answerStatus = (isAnswer ? StatsManager.RIGHT_ANSWERED : StatsManager.WRONG_ANSWERED);
            news.update();
            Log.d(TAG, "onClick: just answered");
        } else {
            Log.d(TAG, "onClick: already answered");
        }
    }
}
