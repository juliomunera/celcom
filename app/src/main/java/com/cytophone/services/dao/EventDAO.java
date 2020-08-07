package com.cytophone.services.dao;

import com.cytophone.services.entities.EventEntity;
import androidx.room.OnConflictStrategy;
import androidx.room.Insert;
import androidx.room.Dao;

@Dao
abstract class EventDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void add(EventEntity event);
}
