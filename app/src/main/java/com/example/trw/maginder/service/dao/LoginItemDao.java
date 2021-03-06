package com.example.trw.maginder.service.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by _TRW on 4/1/2561.
 */

public class LoginItemDao {

    @SerializedName("status")
    private boolean status;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("Restaurant_Name")
    private String restaurantName;

    @SerializedName("ID_Employee")
    private String idEmployee;

    @SerializedName("ID_Restaurant")
    private String idRestaurant;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }
}
