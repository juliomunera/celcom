package com.cytophone.services.dao;

import com.cytophone.services.entities.*;

import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;

@Dao
public abstract class CodeDAO implements IDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void add(CodeEntity... code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void add(EventEntity... event);

    @Transaction()
    public void add(CodeEntity code, EventEntity event){
        add(code);
        add(event);
    }

    @Query("SELECT * " +
        "FROM " +
        " Codes " +
        "WHERE " +
        " code = :code " +
        " AND type = 'U' " +
        "ORDER BY createdDate DESC " +
        "LIMIT 1")
    public abstract CodeEntity getLastUnLockCodeByCodeNumber(String code);

    @Query("SELECT * " +
            "FROM " +
            " Codes " +
            "WHERE " +
            " type = 'A' " +
            "ORDER BY createdDate DESC " +
            "LIMIT 1")
    public abstract CodeEntity getActivationCode();

    @Query("SELECT * " +
            "FROM " +
            " Codes " +
            "WHERE " +
            " type = 'D' " +
            "ORDER BY createdDate DESC " +
            "LIMIT 1")
    public abstract CodeEntity getDeactivationCode();

    @Query("SELECT * " +
            "FROM " +
            " Codes " +
            "WHERE " +
            " type = 'S' " +
            "ORDER BY createdDate DESC " +
            "LIMIT 1")
    public abstract CodeEntity getSuspendCode();
}