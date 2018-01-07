package com.example.trw.maginder.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by _TRW on 25/12/2560.
 */

@Entity(tableName = "menu_type")
public class MenuEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "menu_type_id")
    private int menuTypeId;

    @ColumnInfo(name = "menu_type_name")
    private String menuTypeName;

    public int getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(int menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public String getMenuTypeName() {
        return menuTypeName;
    }

    public void setMenuTypeName(String menuTypeName) {
        this.menuTypeName = menuTypeName;
    }
}
