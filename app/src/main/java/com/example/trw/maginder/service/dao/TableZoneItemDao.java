package com.example.trw.maginder.service.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by _TRW on 16/1/2561.
 */

public class TableZoneItemDao {

    @SerializedName("ID_zone")
    private String zoneId;

    @SerializedName("Name_zone")
    private String zoneName;

    @SerializedName("table")
    private List<TableItemDao> table;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public List<TableItemDao> getTable() {
        return table;
    }

    public void setTable(List<TableItemDao> table) {
        this.table = table;
    }
}
