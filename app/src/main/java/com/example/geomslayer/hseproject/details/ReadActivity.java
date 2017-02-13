package com.example.geomslayer.hseproject.details;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.geomslayer.hseproject.R;
import com.example.geomslayer.hseproject.base.BaseActivity;
import com.example.geomslayer.hseproject.networking.Article;
import com.example.geomslayer.hseproject.stats.StatsManager;
import com.example.geomslayer.hseproject.storage.News;
import com.example.geomslayer.hseproject.storage.Option;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReadActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ReadActivity";

    public static final String EXTRA_NEWS = "news_extra";
    public static final String EXTRA_CAT = "cat_extra";

    private StatsManager statsManager;
    private News news;
    private Article article;
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

        String zipArticle = getIntent().getStringExtra(EXTRA_NEWS);
        article = new Gson().fromJson(zipArticle, Article.class);
        displayArticle();

//        if (!news.wasRead) {
//            statsManager.readNews(news.topic.id);
//            news.wasRead = true;
//            news.update();
//            Log.d(TAG, "onCreate: just read");
//        } else {
//            Log.d(TAG, "onCreate: already read");
//        }
    }

    @Override
    protected void onPause() {
        StatsManager.saveManager(
                getSharedPreferences(StatsManager.PREF_NAME, MODE_PRIVATE), statsManager);
        super.onPause();
    }

    private void displayArticle() {
        ((TextView) findViewById(R.id.txt_topic)).setText(article.categoryObj.text);
        ((TextView) findViewById(R.id.txt_title)).setText(article.title);
        ((TextView) findViewById(R.id.txt_content)).setText(article.text);

        Date date = new Date(article.date);
        DateFormat df = new SimpleDateFormat("HH:mm, dd MMMM yyyy", new Locale("ru", "RU"));
        ((TextView) findViewById(R.id.txt_date)).setText(df.format(date));

        ImageView imgView = (ImageView) findViewById(R.id.img_article);
        if (article.img.equals("")) {
            imgView.setVisibility(View.GONE);
        } else {
            Picasso.with(this)
                    .load(article.img)
                    .into(imgView);
        }
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
