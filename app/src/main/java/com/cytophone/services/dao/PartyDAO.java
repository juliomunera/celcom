package com.cytophone.services.dao;

import com.cytophone.services.entities.*;

import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;

import androidx.lifecycle.LiveData;
import java.util.List;

@Dao
public abstract class PartyDAO implements IDAO {
    //region add methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void add(PartyEntity ... party);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void add(EventEntity ... event);

    @Transaction()
    public void add(PartyEntity party, EventEntity event) {
        add(event);
        add(party);
    }
    //endregion

    //region delete methods.
    @Query("DELETE FROM party WHERE number = :number AND roleID = :roleID")
    abstract int delete(String number, int roleID);

    @Transaction()
    public int delete(PartyEntity party, EventEntity event) {
        add(event);
        return delete(party.getNumber(), party.getRoleID());
    }
    //endregion

    //region get methods.
    @Query("SELECT * FROM party WHERE number = :number")
    public abstract PartyEntity getPartyByNumber(String number);

    @Query("SELECT * FROM party WHERE name = :name")
    public abstract PartyEntity getPartyByName(String name);

    @Query("SELECT * FROM party WHERE number = :number AND roleID = :roleID")
    public abstract PartyEntity getPartByNumberAndRole(String number, Integer roleID);

    /*
    @Query("SELECT * FROM party ORDER BY name ASC LIMIT 100")
    public abstract LiveData<List<PartyEntity>> getSortAllParties();
    */

    @Query("SELECT * FROM party WHERE roleID = 2 ORDER BY name ASC")
    public abstract List<PartyEntity> getAllOrderSuscribers();
    //endregion
}