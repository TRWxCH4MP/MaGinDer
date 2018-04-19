package com.example.trw.maginder.create_item;

import java.util.List;

/**
 * Created by _TRW on 18/1/2561.
 */

public class CreateTableItem {

    private String Id_restaurant;
    private String Id_table;
    private String Id_zone;
    private String Name_table;
    private String Status;
    private List<CreateTable> data;

    public String getId_restaurant() {
        return Id_restaurant;
    }

    public void setId_restaurant(String id_restaurant) {
        Id_restaurant = id_restaurant;
    }

    public String getId_table() {
        return Id_table;
    }

    public void setId_table(String id_table) {
        Id_table = id_table;
    }

    public String getId_zone() {
        return Id_zone;
    }

    public void setId_zone(String id_zone) {
        Id_zone = id_zone;
    }

    public String getName_table() {
        return Name_table;
    }

    public void setName_table(String name_table) {
        Name_table = name_table;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CreateTable> getData() {
        return data;
    }

    public void setData(List<CreateTable> data) {
        this.data = data;
    }
}
