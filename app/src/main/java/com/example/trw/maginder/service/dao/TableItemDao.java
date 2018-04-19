package com.example.trw.maginder.service.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by _TRW on 16/1/2561.
 */

public class TableItemDao {

    @SerializedName("ID_table")
    private String tableId;

    @SerializedName("name_table")
    private String tableName;

    @SerializedName("seat")
    private String seat;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}
