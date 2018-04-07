package com.example.trw.maginder.service.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by _TRW on 6/1/2561.
 */

public class RestaurantItemDao {

    @SerializedName("ID_Menu")
    private String idMenu;

    @SerializedName("ID_Kitchen")
    private String idKitchen;

    @SerializedName("ID_menu_type")
    private String idMenuType;

    @SerializedName("name_type")
    private String nameType;

    @SerializedName("name")
    private String name;

    @SerializedName("detail")
    private String detail;

    @SerializedName("price")
    private String price;

    @SerializedName("date")
    private String date;

    @SerializedName("img")
    private String img;

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getIdKitchen() {
        return idKitchen;
    }

    public void setIdKitchen(String idKitchen) {
        this.idKitchen = idKitchen;
    }

    public String getIdMenuType() {
        return idMenuType;
    }

    public void setIdMenuType(String idMenuType) {
        this.idMenuType = idMenuType;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
