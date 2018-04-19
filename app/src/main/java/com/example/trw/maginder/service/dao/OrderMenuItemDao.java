package com.example.trw.maginder.service.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by _TRW on 23/1/2561.
 */

public class OrderMenuItemDao {

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("img")
    private String img;

    @SerializedName("ID_Menu")
    private String menuId;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
