package com.example.trw.maginder.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by _TRW on 25/12/2560.
 */

@Entity(tableName = "menu")
public class MenuEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primary_id")
    private int primaryId;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "id_menu")
    private String idMenu;

    @ColumnInfo(name = "id_kitchen")
    private String idKitchen;

    @ColumnInfo(name = "img")
    private String img;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private String price;

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

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
}
