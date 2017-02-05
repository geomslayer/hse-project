package com.example.geomslayer.hseproject.base;

import android.app.Application;

import com.example.geomslayer.hseproject.storage.News;
import com.example.geomslayer.hseproject.storage.Option;
import com.example.geomslayer.hseproject.storage.Topic;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.sql.Date;
import java.util.Random;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(new FlowConfig.Builder(this).build());

//        deleteDatabase(AppDatabase.NAME + ".db");
//        StatsManager.saveManager(getSharedPreferences(StatsManager.PREF_NAME, 0), null);

        // add dummy data
        if (SQLite.select().from(Topic.class).count() == 0) {
            Random rand = new Random();

            // topics

            Topic hiTech = new Topic();
            hiTech.text = "Hi-tech";
            hiTech.save();

            Topic politics = new Topic();
            politics.text = "Politics";
            politics.save();

            Topic sport = new Topic();
            sport.text = "Sport";
            sport.save();

            // news

            News news1 = new News();
            news1.topic = hiTech;
            news1.date = new Date(rand.nextLong());
            news1.question = "Question 1";
            news1.text = "News 1";
            news1.title = "Title 1";
            news1.save();

            News news2 = new News();
            news2.topic = hiTech;
            news2.date = new Date(rand.nextLong());
            news2.question = "Question 2";
            news2.text = "News 2";
            news2.title = "Title 2";
            news2.save();

            News news3 = new News();
            news3.topic = politics;
            news3.date = new Date(rand.nextLong());
            news3.question = "Question 3";
            news3.text = "News 3";
            news3.title = "Title 3";
            news3.save();

            News news4 = new News();
            news4.topic = politics;
            news4.date = new Date(rand.nextLong());
            news4.question = "Question 4";
            news4.text = "News 4";
            news4.title = "Title 4";
            news4.save();

            News news5 = new News();
            news5.topic = sport;
            news5.date = new Date(rand.nextLong());
            news5.question = "Question 5";
            news5.text = "News 5";
            news5.title = "Title 5";
            news5.save();

            News news6 = new News();
            news6.topic = sport;
            news6.date = new Date(rand.nextLong());
            news6.question = "Question 6";
            news6.text = "News 6";
            news6.title = "Title 6";
            news6.save();

            // options

            Option opt1 = new Option();
            opt1.text = "Option 1";
            opt1.isAnswer = true;
            opt1.news = news1;
            opt1.save();

            Option opt2 = new Option();
            opt2.text = "Option 2";
            opt2.isAnswer = false;
            opt2.news = news1;
            opt2.save();

            Option opt3 = new Option();
            opt3.text = "Option 1";
            opt3.isAnswer = false;
            opt3.news = news3;
            opt3.save();

            Option opt4 = new Option();
            opt4.text = "Option 2";
            opt4.isAnswer = true;
            opt4.news = news3;
            opt4.save();

            Option opt5 = new Option();
            opt5.text = "Option 1";
            opt5.isAnswer = true;
            opt5.news = news5;
            opt5.save();

            Option opt6 = new Option();
            opt6.text = "Option 2";
            opt6.isAnswer = false;
            opt6.news = news5;
            opt6.save();
        }
    }
}
