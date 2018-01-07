package com.example.trw.maginder.db;

import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;

import java.util.List;

/**
 * Created by _TRW on 26/12/2560.
 */

public interface SendListDataCallback {

    void loadEmployeeDataCallback(List<EmployeeEntity> employeeEntities, boolean isSuccess);

    void loadMenuTypeDataCallback(List<MenuEntity> menuEntities, boolean isSuccess);
}
