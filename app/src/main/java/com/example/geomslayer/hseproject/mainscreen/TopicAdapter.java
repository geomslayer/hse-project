package com.example.geomslayer.hseproject.mainscreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.geomslayer.hseproject.networking.Category;

import java.util.ArrayList;

public class TopicAdapter extends ArrayAdapter<Category> {

    public TopicAdapter(Context context, ArrayList<Category> topicList) {
        super(context, android.R.layout.simple_spinner_item, topicList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category topic = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_spinner_item, null);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(topic.text);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Category topic = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(topic.text);
        return convertView;
    }
}
