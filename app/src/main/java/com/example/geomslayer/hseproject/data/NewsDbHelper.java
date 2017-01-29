package com.example.geomslayer.hseproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.geomslayer.hseproject.data.NewsContract.OptionEntry;
import com.example.geomslayer.hseproject.data.NewsContract.NewsEntry;
import com.example.geomslayer.hseproject.data.NewsContract.TopicEntry;

/**
 * Created by geomslayer on 21.01.17.
 */

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "news.db";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TOPICS_TABLE = "CREATE TABLE " + TopicEntry.TABLE_NAME + " (" +
                TopicEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TopicEntry.COLUMN_BODY + " TEXT NOT NULL" +
                ");";

        final String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_TOPIC_ID + " INTEGER NOT NULL, " +
                NewsEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                NewsEntry.COLUMN_QUESTION + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + NewsEntry.COLUMN_TOPIC_ID + ") REFERENCES " +
                TopicEntry.TABLE_NAME + "(" + TopicEntry._ID + ")" +
                ");";

        final String SQL_CREATE_OPTIONS_TABLE = "CREATE TABLE " + OptionEntry.TABLE_NAME + " (" +
                OptionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OptionEntry.COLUMN_BODY + " TEXT NOT NULL, " +
                OptionEntry.COLUMN_IS_ANSWER + " INTEGER NOT NULL, " +
                OptionEntry.COLUMN_NEWS_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + OptionEntry.COLUMN_NEWS_ID + ") REFERENCES " +
                NewsEntry.TABLE_NAME + "(" + NewsEntry._ID + ")" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TOPICS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_OPTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + OptionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + NewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TopicEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
