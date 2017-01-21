package com.example.geomslayer.hseproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.geomslayer.hseproject.data.NewsContract.TopicEntry;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor cursor = getContentResolver().query(
                TopicEntry.CONTENT_URI,
                new String[] {TopicEntry.COLUMN_BODY},
                null,
                null,
                TopicEntry.COLUMN_BODY);

        if (cursor != null) {
            final int bodyInd = cursor.getColumnIndex(TopicEntry.COLUMN_BODY);

            while (cursor.moveToNext()) {
                Log.d(TAG, "onCreate: " + cursor.getString(bodyInd));
            }
        }

    }

    public void readNewsCallback(View view) {
        Intent readNews = new Intent(this, ReadActivity.class);
        startActivity(readNews);
    }
}
