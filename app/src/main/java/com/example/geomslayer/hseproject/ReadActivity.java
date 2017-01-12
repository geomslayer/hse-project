package com.example.geomslayer.hseproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        news = new Entry(
                "Определен самый мощный современный смартфон",
                "10 января, 21:18",
                "Наука и техника",
                "Флагман iPhone 7 Plus занял первое место в рейтинге самых производительных смартфонов 2016 года по версии создателей популярной программы-бенчмарка AnTuTu. В тестах гаджет набрал более 180.000 баллов.\n" +
                        "\n" +
                        "На втором месте оказался еще один продукт Apple – iPhone 7 (172.000 баллов). В тройку лучших смартфонов также попал китайский OnePlus 3T (163.000 баллов).\n" +
                        "\n" +
                        "Кроме того, в десятке самых производительных мобильных устройств оказались OnePlus3, Xiaomi Mi 5s, Moto Z, LeEco Le Pro3, ZTE AXON 7 и Asus Zenfone.\n" +
                        "\n" +
                        "AnTuTu является одним из лидирующих тестов для определения производительности мобильных устройств. На сегодняшний день программу использует более 100 миллионов пользователей по всему миру."
        );
        question = new Question(
                "Какой по мнению экспертов смартфон является самым мощным в 2016?",
                new ArrayList<>(Arrays.asList("Xiaomi Mi5s Plus", "iPhone 7 Plus", "OnePlus 3T", "AnTuTu")),
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
            Toast.makeText(this, getString(R.string.right), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
        }
    }
}
