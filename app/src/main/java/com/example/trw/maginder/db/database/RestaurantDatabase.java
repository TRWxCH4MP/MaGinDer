package com.example.trw.maginder.db.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.trw.maginder.db.dao.ItemDao;
import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;

/**
 * Created by _TRW on 25/12/2560.
 */

@Database(entities = {EmployeeEntity.class, MenuEntity.class}, version = 1)
public abstract class RestaurantDatabase extends RoomDatabase{

    private static RestaurantDatabase INSTANCE;

    public abstract ItemDao itemDao();

    public static RestaurantDatabase getAppDatabase(Context context) {
        String DATABASE_NAME = "db-restaurant";
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context
            , RestaurantDatabase.class
            , DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
