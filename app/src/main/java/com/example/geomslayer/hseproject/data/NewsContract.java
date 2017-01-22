package com.example.geomslayer.hseproject.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by geomslayer on 21.01.17.
 */

public class NewsContract {
    public static final String CONTENT_AUTHORITY = "com.example.geomslayer.hseproject.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Table for news
     */
    public static class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news";
        public static final String PATH_SIMPLE = "simple";
        public static final String PATH_FULL = "full";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // Columns:
        // The body of a news entry
        public static final String COLUMN_CONTENT = "content";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TOPIC_ID = "topic_id";
        public static final String COLUMN_DATE = "date";

        // The text of related question
        public static final String COLUMN_QUESTION = "question";

        public static Uri buildSimpleNewsUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_SIMPLE).build();
        }

        public static Uri buildSimpleNewsUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_SIMPLE).appendPath(String.valueOf(id)).build();
        }

        public static Uri buildFullNewsUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_FULL).appendPath(String.valueOf(id)).build();
        }

        public static Uri buildFullNewsUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_FULL).build();
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

    public static class OptionEntry implements BaseColumns {
        public static final String TABLE_NAME = "options";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // Columns:
        // The _id of related news
        public static final String COLUMN_NEWS_ID = "news_id";

        // The body of the option
        public static final String COLUMN_BODY = "body";

        // Whether the option is right
        public static final String COLUMN_IS_ANSWER = "is_answer";

        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

    public static class TopicEntry implements BaseColumns {
        public static final String TABLE_NAME = "topics";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // Columns:
        public static final String COLUMN_BODY = "body";

        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

}