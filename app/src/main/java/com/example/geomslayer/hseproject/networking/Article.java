package com.example.geomslayer.hseproject.networking;

public class Article {
    public static final int NOT_READ = 0;
    public static final int WAS_READ = 1;

    public long id;
    public long category;
    public Category categoryObj;
    public String title;
    public String text;
    public long date;
    public String link;
    public String img;
    public int status;
}
