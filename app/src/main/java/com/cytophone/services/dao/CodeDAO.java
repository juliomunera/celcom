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
        if ( code.getType() != "U" ) delete(code.getMsisdn());
        add(code);
        add(event);
    }

    @Query("DELETE FROM codes WHERE msisdn = :number AND type in ( 'A','D' )")
    abstract int delete(String number);

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
}