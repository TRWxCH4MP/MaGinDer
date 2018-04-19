package com.example.trw.maginder.service.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by _TRW on 9/1/2561.
 */

public class RestaurantMenuTypeItemDao {

    @SerializedName("ID_menu_type")
    private String idMenuType;

    @SerializedName("name")
    private String name;

    public String getIdMenuType() {
        return idMenuType;
    }

    public void setIdMenuType(String idMenuType) {
        this.idMenuType = idMenuType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
