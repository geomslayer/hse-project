package com.example.geomslayer.hseproject;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.geomslayer.hseproject.data.NewsContract.NewsEntry;
import com.example.geomslayer.hseproject.data.NewsContract.TopicEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class ReadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ReadActivity";

    private Entry news;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Uri newsEntryUri = getIntent().getData();
        Cursor cursor = getContentResolver().query(newsEntryUri, null, null, null, null);
        cursor.moveToNext();

        news = new Entry(
                cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(TopicEntry.COLUMN_BODY)),
                cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_CONTENT))
        );

        question = new Question(
                cursor.getString(cursor.getColumnIndex(NewsEntry.COLUMN_QUESTION)),
                new ArrayList<>(Arrays.asList("Вариант 1", "Вариант 2", "Вариант 3")),
                1
        );

        displayEntry();
        displayQuestion();
    }

    private void displayEntry() {
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        TextView txtTopic = (TextView) findViewById(R.id.txt_topic);
        TextView txtDate = (TextView) findViewById(R.id.txt_date);
        TextView txtContent = (TextView) findViewById(R.id.txt_content);

        txtTitle.setText(news.getTitle());
        txtTopic.setText(news.getTopic());
        txtDate.setText(news.getDate());
        txtContent.setText(news.getContent());
    }

    private void displayQuestion() {
        TextView txtQuestion = (TextView) findViewById(R.id.txt_question);
        txtQuestion.setText(question.getQuestion());

        LinearLayout ltOptions = (LinearLayout) findViewById(R.id.option_list);
        ArrayList<String> options = question.getOptions();
        ViewGroup.LayoutParams ltParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        for (int i = 0; i < options.size(); ++i) {
            Button option = new Button(this);
            option.setTag(i);
            option.setText(options.get(i));
            option.setOnClickListener(this);
            ltOptions.addView(option, ltParams);
        }
    }

    @Override
    public void onClick(View view) {
        int tag = (int) view.getTag();
        if (question.checkAnswer(tag)) {
            // Toast.makeText(this, getString(R.string.right), Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.activity_read), R.string.right, Snackbar.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.activity_read), R.string.wrong, Snackbar.LENGTH_SHORT).show();
        }
    }
}
