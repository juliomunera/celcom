package com.cytophone.services.entities;

import com.cytophone.services.dao.TimestampConverter;

import androidx.room.TypeConverters;
import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.concurrent.TimeUnit;
import java.util.UUID;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.io.Serializable;

@Entity(tableName = "Codes")
public class CodeEntity implements IEntityBase, Serializable {
    //Getter methods
    public String getCreatedDate() {
        return this._createdDate;
    }

    @NonNull
    public String getCountryCode() {
        return this._countryCode;
    }

    @NonNull
    public String getCode() {
        return this._code;
    }

    public String getEndDate() {
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
    public String getType() {
        return this._type;
    }

    //Setter methods
    public void setCreatedDate(@NonNull String createdDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = sdf.parse(createdDate);
        }catch (ParseException pex) {
            createdDate = sdf.format(new Date(System.currentTimeMillis()));;
        }
        this._createdDate = createdDate;
    }

    public void setCode(@NonNull String code) {
        this._code = code;
    }

    public void setCountryCode(@NonNull String countryCode) {
        this._countryCode =  countryCode;
    }

    public void setEndDate(@NonNull String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(endDate);
        } catch (ParseException pex) {
            endDate = sdf.format(new Date(System.currentTimeMillis()));
        }
        this._endDate = endDate;
    }

    public void setId(@NonNull String id) {
        this._id = id;
    }

    public void setMsisdn(@NonNull String msisdn) {
        this._msisdn = msisdn;
    }

    public void setType(@NonNull String type) {
        this._type = type;
    }

    //Constructor methods
    public CodeEntity() {
        this._createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(new Date(System.currentTimeMillis()));
        this._id = UUID.randomUUID().toString();
    }

    public CodeEntity(String countryCode, String code, String MSISDN, long seconds, String type){
        this();

        this._endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(new Date( System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds)));
        this._countryCode = countryCode;
        this._msisdn = MSISDN;
        this._code = code;
        this._type = type;
    }

    //Fields declaration
    @ColumnInfo(name="createdDate")
    //@TypeConverters(TimestampConverter.class)
    private String _createdDate;

    @ColumnInfo(name = "code")
    @NonNull
    private String _code;

    @ColumnInfo(name = "countryCode")
    @NonNull
    private String _countryCode;

    @ColumnInfo(name="endDate")
    //@TypeConverters(TimestampConverter.class)
    private String _endDate;

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private String _id;

    @ColumnInfo(name = "msisdn")
    @NonNull
    private String _msisdn;

    @ColumnInfo(name = "type")
    @NonNull
    private String _type;
}