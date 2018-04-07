package com.example.trw.maginder.db.callback;

import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;

import java.util.List;

/**
 * Created by _TRW on 26/12/2560.
 */

public interface SendListDataCallback {

    void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess);
}
