package com.example.geomslayer.hseproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.geomslayer.hseproject.data.NewsContract.*;

/**
 * Created by geomslayer on 21.01.17.
 */

public class NewsProvider extends ContentProvider {

    private static final String TAG = "NewsProvider";

    private final UriMatcher matcher = buildMatcher();
    private NewsDbHelper openHelper;

    private final int TOPICS = 100;
    private final int TOPICS_ID = 101;

    private final int SIMPLE_NEWS = 200;
    private final int FULL_NEWS = 201;
    private final int NEWS_ID = 202;
    private final int FULL_NEWS_ID = 203;
    private final int NEWS = 204;

    private final int OPTIONS = 300;
    private final int OPTIONS_ID = 301;
    private final int OPTIONS_WITH_NEWS_ID = 302;

    private UriMatcher buildMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, TopicEntry.TABLE_NAME, TOPICS);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, TopicEntry.TABLE_NAME + "/#", TOPICS_ID);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsEntry.TABLE_NAME, NEWS);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY,
                NewsEntry.TABLE_NAME + "/" + NewsEntry.PATH_SIMPLE, SIMPLE_NEWS);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY,
                NewsEntry.TABLE_NAME + "/" + NewsEntry.PATH_FULL, FULL_NEWS);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY,
                NewsEntry.TABLE_NAME + "/" + NewsEntry.PATH_SIMPLE + "/#", NEWS_ID);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY,
                NewsEntry.TABLE_NAME + "/" + NewsEntry.PATH_FULL + "/#", FULL_NEWS_ID);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, OptionEntry.TABLE_NAME, OPTIONS);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, OptionEntry.TABLE_NAME + "/#", OPTIONS_ID);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY,
                OptionEntry.TABLE_NAME + "/" + OptionEntry.PATH_WITH_NEWS + "/#", OPTIONS_WITH_NEWS_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        openHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final int match = matcher.match(uri);
        long id;

        final String SQL_JOIN = NewsEntry.TABLE_NAME + " INNER JOIN " +
                TopicEntry.TABLE_NAME + " ON " +
                TopicEntry.TABLE_NAME + "." + TopicEntry._ID + " = " +
                NewsEntry.TABLE_NAME + "." + NewsEntry.COLUMN_TOPIC_ID;

        switch (match) {
            case TOPICS:
                queryBuilder.setTables(TopicEntry.TABLE_NAME);
                break;

            case TOPICS_ID:
                queryBuilder.setTables(TopicEntry.TABLE_NAME);
                id = TopicEntry.getIdFromUri(uri);
                queryBuilder.appendWhere(TopicEntry.TABLE_NAME + "." + TopicEntry._ID + " = " + id);
                break;

            case SIMPLE_NEWS:
                queryBuilder.setTables(NewsEntry.TABLE_NAME);
                break;

            case FULL_NEWS:
                queryBuilder.setTables(SQL_JOIN);
                break;

            case NEWS_ID:
                queryBuilder.setTables(NewsEntry.TABLE_NAME);
                id = NewsEntry.getIdFromUri(uri);
                queryBuilder.appendWhere(NewsEntry._ID + " = " + id);
                break;

            case FULL_NEWS_ID:
                queryBuilder.setTables(SQL_JOIN);
                id = NewsEntry.getIdFromUri(uri);
                queryBuilder.appendWhere(NewsEntry.TABLE_NAME + "." + NewsEntry._ID + " = " + id);
                break;

            case OPTIONS:
                queryBuilder.setTables(OptionEntry.TABLE_NAME);
                break;

            case OPTIONS_ID:
                queryBuilder.setTables(OptionEntry.TABLE_NAME);
                id = OptionEntry.getIdFromUri(uri);
                queryBuilder.appendWhere(OptionEntry.TABLE_NAME + "." + OptionEntry._ID + " = " + id);

            case OPTIONS_WITH_NEWS_ID:
                queryBuilder.setTables(OptionEntry.TABLE_NAME);
                id = OptionEntry.getIdFromUri(uri);
                queryBuilder.appendWhere(
                        OptionEntry.TABLE_NAME + "." + OptionEntry.COLUMN_NEWS_ID + " = " + id);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }

        return queryBuilder.query(
                openHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("getType is not implemented!");
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = matcher.match(uri);
        Uri retUri;
        long id;
        switch (match) {
            case TOPICS:
                id = db.insert(TopicEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    retUri = TopicEntry.buildTopicUri(id);
                } else {
                    throw new SQLException("Failed to insert row into " + TopicEntry.TABLE_NAME);
                }
                break;

            case NEWS:
                id = db.insert(NewsEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    retUri = NewsEntry.buildSimpleNewsUri(id);
                } else {
                    throw new SQLException("Failed to insert row into " + NewsEntry.TABLE_NAME);
                }
                break;

            case OPTIONS:
                id = db.insert(OptionEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    retUri = OptionEntry.buildUri(id);
                } else {
                    throw new SQLException("Failed to insert row into " + OptionEntry.TABLE_NAME);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = matcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case TOPICS:
                rowsDeleted = db.delete(TopicEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case NEWS:
                rowsDeleted = db.delete(NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case OPTIONS:
                rowsDeleted = db.delete(OptionEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = matcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case TOPICS:
                rowsUpdated = db.update(TopicEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case NEWS:
                rowsUpdated = db.update(NewsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case OPTIONS:
                rowsUpdated = db.update(OptionEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }
        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
