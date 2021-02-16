package com.cytophone.services.entities;

import com.cytophone.services.dao.TimestampConverter;

import androidx.room.TypeConverters;
import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

import java.util.concurrent.TimeUnit;
import java.util.UUID;
import java.util.Date;

@Entity(tableName = "Codes")
public class CodeEntity implements IEntityBase, Serializable {
    //Getter methods
    public Date getCreatedDate() {
        return this._createdDate;
    }

    @NonNull
    public String getCode() {
        return this._code;
    }

    public Date getEndDate() {
        return this._endDate;
    }

    @NonNull
    public String getId() {
        return this._id;
    }

    @NonNull
    public String getMsisdn() {
        return this._msisdn;
    }

    @NonNull
    public char getType() {
        return this._type;
    }

    //Setter methods
    public void setCreatedDate(@NonNull Date createdDate) {
        this._createdDate = createdDate;
    }

    public void setCode(@NonNull String code) {
        this._code = code;
    }

    public void setEndDate(@NonNull Date endDate) {
        this._endDate = endDate;
    }

    public void setId(@NonNull String id) {
        this._id = id;
    }

    public void setMsisdn(@NonNull String msisdn) {
        this._msisdn = msisdn;
    }

    public void setType(@NonNull char type) {
        this._type = type;
    }

    //Constructor methods
    public CodeEntity() {
        this._createdDate = new Date(System.currentTimeMillis());
        this._id = UUID.randomUUID().toString();
    }

    public CodeEntity(String code, String MSISDN, long seconds, char type){
        this();

        this._endDate = new Date( System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
        this._msisdn = MSISDN;
        this._code = code;
        this._type = type;
    }

    //Fields declaration
    @ColumnInfo(name="createdDate")
    @TypeConverters(TimestampConverter.class)
    private Date _createdDate;

    @ColumnInfo(name = "code")
    @NonNull
    private String _code;

    @ColumnInfo(name="endDate")
    @TypeConverters(TimestampConverter.class)
    Date _endDate;

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private String _id;

    @ColumnInfo(name = "msisdn")
    @NonNull
    private String _msisdn;

    @ColumnInfo(name = "type")
    @NonNull
    private char _type;
}