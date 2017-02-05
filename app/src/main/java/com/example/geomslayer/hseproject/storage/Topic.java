package com.example.geomslayer.hseproject.storage;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by geomslayer on 04.02.17.
 */

@Table(database = AppDatabase.class)
public class Topic extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String text;
}
