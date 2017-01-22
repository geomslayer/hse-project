package com.example.geomslayer.hseproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.geomslayer.hseproject.data.NewsContract;

import java.util.ArrayList;

/**
 * Created by geomslayer on 21.01.17.
 */

class NewsAdapter extends CursorAdapter {

    private static final String TAG = "NewsAdapter";

    private final int TITLE_INDEX;
    private final int CONTENT_INDEX;

    private ArrayList<ViewHolder> holders;

    NewsAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        TITLE_INDEX = cursor.getColumnIndexOrThrow(NewsContract.NewsEntry.COLUMN_TITLE);
        CONTENT_INDEX = cursor.getColumnIndexOrThrow(NewsContract.NewsEntry.COLUMN_CONTENT);
        holders = new ArrayList<>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_item, parent, false);
        view.setTag(holders.size());
        holders.add(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = holders.get((int) view.getTag());
        holder.content.setText(cursor.getString(CONTENT_INDEX));
        holder.title.setText(cursor.getString(TITLE_INDEX));
    }

    private class ViewHolder {
        final TextView title;
        final TextView content;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.txt_item_title);
            content = (TextView) view.findViewById(R.id.txt_item_content);
        }
    }


}
