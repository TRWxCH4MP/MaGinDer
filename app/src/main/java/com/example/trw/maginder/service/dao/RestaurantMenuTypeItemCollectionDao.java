package com.example.trw.maginder.service.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by _TRW on 9/1/2561.
 */

public class RestaurantMenuTypeItemCollectionDao {

    @SerializedName("data")
    private List<RestaurantMenuTypeItemDao> data;

    public List<RestaurantMenuTypeItemDao> getData() {
        return data;
    }

    public void setData(List<RestaurantMenuTypeItemDao> data) {
        this.data = data;
    }
}
