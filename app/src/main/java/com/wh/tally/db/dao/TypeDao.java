package com.wh.tally.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;

import com.wh.tally.db.entity.TypeBean;

import java.util.List;

@Dao
public interface TypeDao {
    @Insert
    void insert(TypeBean typeBean);

    @Query("select * from type where kind = :kind")
    List<TypeBean> getTypeList(int kind);

    // 查询数据库中数据的数量
    @Query("select count(*) FROM type")
    int getTypeCount();
}
