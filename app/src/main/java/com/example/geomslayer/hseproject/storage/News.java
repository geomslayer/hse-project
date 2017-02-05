package com.example.geomslayer.hseproject.storage;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Date;

/**
 * Created by geomslayer on 04.02.17.
 */

@Table(database = AppDatabase.class)
public class News extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    @ForeignKey
    public Topic topic;

    @Column
    public String title;

    @Column
    public String text;

    @Column
    public String question;

    @Column
    public Date date;

    @Column(defaultValue = "0")
    public Boolean wasRead;

    @Column(defaultValue = "0")
    public int answerStatus;
}
