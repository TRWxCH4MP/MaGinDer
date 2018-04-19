package com.example.trw.maginder.service.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by _TRW on 16/1/2561.
 */

public class TableItemCollectionDao {

    @SerializedName("data")
    private List<TableZoneItemDao> data;

    public List<TableZoneItemDao> getData() {
        return data;
    }

    public void setData(List<TableZoneItemDao> data) {
        this.data = data;
    }
}
