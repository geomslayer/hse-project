package com.example.geomslayer.hseproject;

/**
 * Created by geomslayer on 11.01.17.
 */

class Entry {

    private String title;
    private String date;
    private String topic;
    private String content;

    public Entry() {
        this.title = "Title";
        this.date = "Date";
        this.topic = "Topic";
        this.content = "Content";
    }

    public Entry(String title, String date, String topic, String content) {
        this.title = title;
        this.date = date;
        this.topic = topic;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getTopic() {
        return topic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
