package com.example.trw.maginder.adapter.item;

import com.example.trw.maginder.adapter.ViewType;

/**
 * Created by _TRW on 16/1/2561.
 */

public class TableItem extends BaseItem {

    private String tableName;
    private String tableState;
    private String tableId;
    private String zoneId;

    public TableItem() {
        super(ViewType.TYPE_TABLE_VIEW);
    }

    public String getTableName() {
        return tableName;
    }

    public TableItem setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getTableState() {
        return tableState;
    }

    public TableItem setTableState(String tableState) {
        this.tableState = tableState;
        return this;
    }

    public String getTableId() {
        return tableId;
    }

    public TableItem setTableId(String tableId) {
        this.tableId = tableId;
        return this;
    }

    public String getZoneId() {
        return zoneId;
    }

    public TableItem setZoneId(String zoneId) {
        this.zoneId = zoneId;
        return this;
    }
}
