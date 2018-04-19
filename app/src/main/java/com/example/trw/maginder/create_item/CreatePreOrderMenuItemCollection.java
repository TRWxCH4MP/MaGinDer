package com.example.trw.maginder.create_item;

import java.util.List;

/**
 * Created by _TRW on 1/2/2561.
 */

public class CreatePreOrderMenuItemCollection {

    private String name_user;
    private String status;
    private String timestamp;
    private List<String> data;


    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
