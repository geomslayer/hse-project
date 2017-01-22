package com.example.geomslayer.hseproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.geomslayer.hseproject.data.NewsContract;
import com.example.geomslayer.hseproject.data.NewsContract.NewsEntry;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";

    @Override
    int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem spinnerItem = menu.findItem(R.id.action_topic);

        Spinner spinnerTopics = (Spinner) MenuItemCompat.getActionView(spinnerItem);
        Cursor topicsCursor = getContentResolver().query(NewsContract.TopicEntry.CONTENT_URI, null, null, null, null);
        SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, topicsCursor,
                new String[]{NewsContract.TopicEntry.COLUMN_BODY},
                new int[]{android.R.id.text1}, 0);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopics.setAdapter(spinnerAdapter);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getContentResolver().delete(NewsEntry.CONTENT_URI, null, null);
//
//        ContentValues values = new ContentValues();
//        values.put(NewsEntry.COLUMN_TITLE, "BlackBerry исполнилось 18 лет");
//        values.put(NewsEntry.COLUMN_CONTENT, "18 лет назад, 19 января 1999 года, канадская компания Research In Motion представила первое устройство под именем BlackBerry — пейджер BlackBerry Handheld (переименованный потом в BlackBerry 850) с возможностью отправки электронных писем и выделенным модулем беспроводной связи. Он имел миниатюрный LCD-дисплей, 32-битный процессор Intel 386, 2 МБ флэш-памяти, модем, поддержку тройного DES-шифрования данных и полноценную QWERTY-клавиатуру. Заряда одной АА-батарейки хватало на полный день. Девайс моментально стал культовым на корпоративном рынке, что обеспечило компании впечатляющую репутацию и лидерские позиции на многие годы вперёд. Сейчас смартфоны под брендом BlackBerry выпускает китайская компания TCL.");
//        values.put(NewsEntry.COLUMN_DATE, 0);
//        values.put(NewsEntry.COLUMN_TOPIC_ID, 1);
//        values.put(NewsEntry.COLUMN_QUESTION, "Что такое BlackBerry?");
//        getContentResolver().insert(NewsEntry.CONTENT_URI, values);
//
//        values.put(NewsEntry.COLUMN_TITLE, "Техника Apple подешевела в России");
//        values.put(NewsEntry.COLUMN_CONTENT, "Российские цены на технику продолжают колебаться вслед за курсом рубля. В прошлом году он практически не колебался, но под конец начал уверенно укрепляться, и этот рост продолжился сейчас, спровоцировав удешевление техники Apple. Так, минимальные iPhone 7 и 7 Plus теперь стоят у Apple 52 990 и 62 990 рублей соответственно (-4000 и -5000 рублей), аналогично подешевели и iPhone 6S / 6S Plus с 32 ГБ памяти (теперь 44 990 за 4,7\" и 52 990 5,5\"). AirPods теперь стоят 11 990 рублей, а 27\" iMac 5K – 137 990 рублей (-1000 и -12 000). Подешевели и планшеты iPad Pro, теперь они стартуют с 44 990 за 9,7\" и 58 990 за 12,9\". Минимальные Watch Series 2 стоят теперь 30 990 рублей, на 3000 меньше, чем прежде. Подешевели и MacBook Pro – на 3000 рублей версия без сенсорной панели и на 7000 рублей версия с ним. Актуальные цены на другие продукты компании можно узнать на сайте Apple.");
//        values.put(NewsEntry.COLUMN_DATE, 0);
//        values.put(NewsEntry.COLUMN_QUESTION, "У тебя есть iPhone?");
//        values.put(NewsEntry.COLUMN_TOPIC_ID, 1);
//        getContentResolver().insert(NewsEntry.CONTENT_URI, values);
//
//        values.put(NewsEntry.COLUMN_TITLE, "Больше миллиона человек хотят купить Nokia 6");
//        values.put(NewsEntry.COLUMN_CONTENT, "Первый смартфон HMD Global, анонсированный в начале этого года, уже послезавтра поступит в продажу. Девайс с названием Nokia 6 будет продаваться только в Китае и только через интернет-магазин JD.com, благодаря чему можно точно отслеживать количество желающих приобрести его. За два дня до начала продаж оно превысило миллион! На момент написания этих слов оно равно 1 018 645. Напомним, Nokia 6 получил ОС Android 7.0 с непонятной прошивкой на базе ядра CyanogenMod, чипсет Qualcomm Snapdragon 430, 4 ГБ ОЗУ, 64 ГБ встроенной памяти, 5,5\" Full HD-экран, 16-Мп заднюю и 8-Мп фронтальную камеры, аккумулятор на 3000 мАч, сканер отпечатков пальцев и цельнометаллический корпус. Цена смартфона в Китае составляет 1699 юаней (14 650 рублей).");
//        values.put(NewsEntry.COLUMN_DATE, 0);
//        values.put(NewsEntry.COLUMN_QUESTION, "Сколько стоит Nokia 6?");
//        values.put(NewsEntry.COLUMN_TOPIC_ID, 1);
//        getContentResolver().insert(NewsEntry.CONTENT_URI, values);

//        getContentResolver().delete(OptionEntry.CONTENT_URI, null, null);
//
//        ContentValues values = new ContentValues();
//        values.put(OptionEntry.COLUMN_BODY, "Черная ягода");
//        values.put(OptionEntry.COLUMN_IS_ANSWER, 0);
//        values.put(OptionEntry.COLUMN_NEWS_ID, 97);
//        getContentResolver().insert(OptionEntry.CONTENT_URI, values);
//
//        values.put(OptionEntry.COLUMN_BODY, "Канадская кампания");
//        values.put(OptionEntry.COLUMN_IS_ANSWER, 1);
//        values.put(OptionEntry.COLUMN_NEWS_ID, 97);
//        getContentResolver().insert(OptionEntry.CONTENT_URI, values);
//
//        values.put(OptionEntry.COLUMN_BODY, "Нет");
//        values.put(OptionEntry.COLUMN_IS_ANSWER, 0);
//        values.put(OptionEntry.COLUMN_NEWS_ID, 98);
//        getContentResolver().insert(OptionEntry.CONTENT_URI, values);
//
//        values.put(OptionEntry.COLUMN_BODY, "Да");
//        values.put(OptionEntry.COLUMN_IS_ANSWER, 1);
//        values.put(OptionEntry.COLUMN_NEWS_ID, 98);
//        getContentResolver().insert(OptionEntry.CONTENT_URI, values);
//
//        values.put(OptionEntry.COLUMN_BODY, "Что это?");
//        values.put(OptionEntry.COLUMN_IS_ANSWER, 0);
//        values.put(OptionEntry.COLUMN_NEWS_ID, 98);
//        getContentResolver().insert(OptionEntry.CONTENT_URI, values);
//
//        values.put(OptionEntry.COLUMN_BODY, "14 650 рублей");
//        values.put(OptionEntry.COLUMN_IS_ANSWER, 1);
//        values.put(OptionEntry.COLUMN_NEWS_ID, 99);
//        getContentResolver().insert(OptionEntry.CONTENT_URI, values);
//
//        values.put(OptionEntry.COLUMN_BODY, "1699 юаней");
//        values.put(OptionEntry.COLUMN_IS_ANSWER, 1);
//        values.put(OptionEntry.COLUMN_NEWS_ID, 99);
//        getContentResolver().insert(OptionEntry.CONTENT_URI, values);


        Cursor cursor = getContentResolver().query(
                NewsEntry.buildSimpleNewsUri(), null, null, null, null);

        NewsAdapter newsAdapter = new NewsAdapter(this, cursor);
        ListView listNews = (ListView) findViewById(R.id.list_news);
        listNews.setAdapter(newsAdapter);
        listNews.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent readIntent = new Intent(this, ReadActivity.class);
        readIntent.putExtra(NewsEntry._ID, id);
        startActivity(readIntent);
    }
}
