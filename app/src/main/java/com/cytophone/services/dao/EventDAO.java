package com.cytophone.services.dao;

import com.cytophone.services.entities.EventEntity;

import androidx.room.OnConflictStrategy;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;

import java.util.List;

@Dao
abstract class EventDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void add(EventEntity event);

    @Query("SELECT * FROM EventLog ORDER BY createdDate DESC")
    public abstract List<EventEntity> get20LastMessages();
}
