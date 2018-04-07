package com.example.trw.maginder.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;

import java.util.List;

/**
 * Created by _TRW on 25/12/2560.
 */

@Dao
public interface ItemDao {

    @Insert
    void insertMenu(MenuEntity menuEntity);

    @Query("SELECT * FROM menu")
    List<MenuEntity> loadAllMenu();

    @Query("DELETE FROM menu")
    void deleteAllMenu();

    @Query("DELETE FROM menu WHERE primary_id = :primaryId")
    void deleteMenuByPrimaryId(String primaryId);

}
