package com.wh.tally.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.wh.tally.db.entity.AccountBean;

import java.util.List;

@Dao
public interface AccountDao {

    @Insert
    void insert(AccountBean accountBean);

    @Query("select * from account where year = :year and month = :month and day = :day order by id desc")
    List<AccountBean> getAccountListOfDay(int year,int month,int day);

    // 获取某一天的收/支
    @Query("select sum(money) from account where year = :year and month = :month and day = :day and kind = :kind")
    Long getInOrOutOneDay(int year,int month,int day,int kind);

    // 获取某一个月的收/支
    @Query("select sum(money) from account where year =:year and month = :month and kind = :kind")
    Long getInOrOutOneMonth(int year,int month,int kind);

    // 获取某一个年的收/支
    @Query("select sum(money) from account where year =:year and kind = :kind")
    Long getInOrOutOneYear(int year,int kind);

    @Delete
    void removeById(AccountBean accountBean);

    @Query("select * from account where beizhu like '%' || :searchContent || '%' or typename like '%' || :searchContent || '%'")
    List<AccountBean> getAccountsByBeizhuOrTypename(String searchContent);

    // 获取本月所有收支记录
    @Query("select * from account where year =:year and month = :month")
    List<AccountBean> getInAndOutOneMonth(int year, int month);

    @Query("select distinct(year) from account order by year asc")
    List<Integer> getYearList();
}
