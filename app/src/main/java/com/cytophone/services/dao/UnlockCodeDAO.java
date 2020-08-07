package com.cytophone.services.dao;

import com.cytophone.services.entities.*;

import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;

@Dao
public abstract class UnlockCodeDAO implements IDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void add(UnlockCodeEntity... code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void add(EventEntity... event);

    @Transaction()
    public void add(UnlockCodeEntity code, EventEntity event){
        add(code);
        add(event);
    }

    @Query("SELECT * FROM party WHERE number = :number AND roleID = 1")
    public abstract PartyEntity getPartByNumberAndRole(String number);

    /*
    @Query( "SELECT max(createdDate), " +
    "code " +
    "FROM " +
    "UnlockCodes " +
    "WHERE " +
    "endDate < date('now') " +
    "GROUP By code " +
    "LIMIT 1")
    public abstract UnlockCodeEntity getLastCode();
    */
}