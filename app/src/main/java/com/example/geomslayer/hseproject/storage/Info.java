package com.example.geomslayer.hseproject.storage;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class Info extends BaseModel {
    @PrimaryKey
    public long id;

    @Column(defaultValue = "1")
    public int status;

    public Info() {}
    public Info(long id) {
        this.id = id;
    }
}
