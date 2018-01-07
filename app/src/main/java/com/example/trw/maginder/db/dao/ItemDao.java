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
    void insertEmployee(EmployeeEntity employeeEntity);

    @Insert
    void insertMenuType(MenuEntity menuEntity);

    @Query("SELECT * FROM employee")
    List<EmployeeEntity> loadAllEmployee();

    @Query("SELECT * FROM menu_type")
    List<MenuEntity> loadAllMenuType();
}
