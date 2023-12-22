package com.wh.tally.db.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wh.tally.db.dao.AccountDao;
import com.wh.tally.db.dao.TypeDao;
import com.wh.tally.db.entity.AccountBean;
import com.wh.tally.db.entity.TypeBean;

@Database(entities = {TypeBean.class, AccountBean.class},version = 3,exportSchema = false)
public abstract class TallyDatabase extends RoomDatabase {

    public abstract TypeDao getTypeDao();

    public abstract AccountDao getAccountDao();

    // 单例模式获取数据库实例
    private static volatile TallyDatabase INSTANCE;

    public static TallyDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TallyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TallyDatabase.class, "tally")
                            .allowMainThreadQueries()
                            .addMigrations()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
