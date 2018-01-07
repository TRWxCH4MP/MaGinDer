package com.example.trw.maginder.service.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by _TRW on 7/1/2561.
 */

public class RestaurantItemCollectionDao {

    @SerializedName("data")
    private List<RestaurantItemDao> data;

    public List<RestaurantItemDao> getData() {
        return data;
    }

    public void setData(List<RestaurantItemDao> data) {
        this.data = data;
    }
}
