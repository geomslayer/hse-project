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
import com.example.geomslayer.hseproject.base.BaseApp;
import com.example.geomslayer.hseproject.networking.Article;
import com.example.geomslayer.hseproject.networking.Question;
import com.example.geomslayer.hseproject.stats.StatsManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class ReadActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ReadActivity";

    public static final String EXTRA_NEWS = "news_extra";
    public static final String EXTRA_CAT = "cat_extra";

    private StatsManager statsManager;
    private Article article;

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

        requestQuestions(article.id);
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
            Picasso.with(this).load(article.img)
                    .placeholder(R.drawable.news_placeholder)
                    .error(R.mipmap.ic_launcher)
                    .noFade()
                    .into(imgView);
        }

        // tmp:
        ((TextView) findViewById(R.id.txt_question)).setText(getString(R.string.question));
    }

    private void displayOptions(ArrayList<Question> options) {
        if (options == null) {
            return;
        }

        LinearLayout layoutOptions = (LinearLayout) findViewById(R.id.option_list);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (Question option : options) {
            Button optionBtn = new Button(this);
            optionBtn.setTag(option.is_ans);
            optionBtn.setText(option.text);
            optionBtn.setOnClickListener(this);
            layoutOptions.addView(optionBtn, layoutParams);
        }
    }

    private void requestQuestions(long newsId) {
        Request request = new Request.Builder()
                .url(BaseApp.BASE_URL + "questions/" + newsId)
                .build();
        BaseApp.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: couldn't load questions.");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Type type = new TypeToken<ArrayList<Question>>() {}.getType();
                final ArrayList<Question> qs = new Gson().fromJson(response.body().string(), type);
                ReadActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayOptions(qs);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        boolean isAnswer = (boolean) view.getTag();
        Snackbar.make(findViewById(R.id.activity_read),
                (isAnswer ? R.string.right : R.string.wrong),
                Snackbar.LENGTH_SHORT).show();
//        if (news.answerStatus == StatsManager.NOT_ANSWERED) {
//            statsManager.answer(isAnswer);
//            news.answerStatus = (isAnswer ? StatsManager.RIGHT_ANSWERED : StatsManager.WRONG_ANSWERED);
//            news.update();
//            Log.d(TAG, "onClick: just answered");
//        } else {
//            Log.d(TAG, "onClick: already answered");
//        }
    }
}
