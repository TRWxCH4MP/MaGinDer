package com.example.trw.maginder.service.dao;

import com.example.trw.maginder.adapter.item.PreOrderMenuItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by _TRW on 23/1/2561.
 */

public class PreOrderMenuItemCollectionDao {

    @SerializedName("data")
    private List<OrderMenuItemDao> data;

    public List<OrderMenuItemDao> getData() {
        return data;
    }

    public void setData(List<OrderMenuItemDao> data) {
        this.data = data;
    }
}
