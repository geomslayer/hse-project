package com.example.geomslayer.hseproject.stats;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.TreeMap;

public class StatsManager {
    private static final String PREF_STATS = "pref_stats";
    public static final String PREF_NAME = "statistics";

    public static final int NOT_ANSWERED = 0;
    public static final int RIGHT_ANSWERED = 1;
    public static final int WRONG_ANSWERED = 2;

    private long popularTopicId;
    private TreeMap<Long, Integer> count;
    private int allAnswered;
    private int answeredRight;
    private int allRead;

    public StatsManager() {
        count = new TreeMap<>();
        allAnswered = 0;
        popularTopicId = -1;
        allRead = 0;
        answeredRight = 0;
    }

    public void readNews(long curTopicId) {
        ++allRead;
        int cnt = 0;
        if (count.containsKey(curTopicId)) {
            cnt = count.get(curTopicId);
        }
        count.put(curTopicId, ++cnt);
        if (popularTopicId == -1 || count.get(popularTopicId) < cnt) {
            popularTopicId = curTopicId;
        }
    }

    public void answer(boolean right) {
        ++allAnswered;
        if (right) {
            ++answeredRight;
        }
    }

    public static StatsManager getSavedManager(SharedPreferences sp) {
        String json = sp.getString(PREF_STATS, "");
        StatsManager instance = new Gson().fromJson(json, StatsManager.class);
        if (instance == null) {
            return new StatsManager();
        }
        return instance;
    }

    public static void saveManager(SharedPreferences sp, StatsManager manager) {
        sp.edit()
                .putString(PREF_STATS, new Gson().toJson(manager))
                .apply();
    }

    public int getAllRead() {
        return allRead;
    }

    public int getAnsweredRight() {
        return answeredRight;
    }

    public int getAllAnswered() {
        return allAnswered;
    }

    public long getPopularTopicId() {
        return popularTopicId;
    }
}
