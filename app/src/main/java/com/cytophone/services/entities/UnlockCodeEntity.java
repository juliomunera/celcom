package com.cytophone.services.entities;

import com.cytophone.services.dao.TimestampConverter;

import androidx.room.TypeConverters;
import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "UnlockCodes")
public class UnlockCodeEntity implements IEntityBase {
    //Getter methods
    public Date getCreatedDate() {
        return createdDate;
    }

    @NonNull
    public String getCode() { return code; }

    public Date getEndDate() {
        return endDate;
    }

    @NonNull
    public String getId() { return id; }

    @NonNull
    public String getMsisdn() { return msisdn; }

    //Setter methods
    public void setCode(@NonNull String code) { this.code = code; }

    public void setId(@NonNull String id) { this.id = id; }

    public void setMsisdn(@NonNull String msisdn) { this.msisdn = msisdn; }

    //Constructor methods
    public UnlockCodeEntity() {
        this.createdDate = new Date(System.currentTimeMillis());
        this.id = UUID.randomUUID().toString();
    }

    public UnlockCodeEntity(String code, String MSISDN, long seconds){
        this();

        this.endDate = new Date( System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
        this.msisdn = MSISDN;
        this.code = code;
    }

    //Fields declaration
    @ColumnInfo(name="createdDate")
    @TypeConverters(TimestampConverter.class)
    Date createdDate;

    @ColumnInfo(name = "code")
    @NonNull
    private String code;

    @ColumnInfo(name="endDate")
    @TypeConverters(TimestampConverter.class)
    Date endDate;

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "msisdn")
    @NonNull
    private String msisdn;
}