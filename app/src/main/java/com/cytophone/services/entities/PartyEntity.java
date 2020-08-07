package com.cytophone.services.entities;

import com.cytophone.services.dao.*;

import androidx.room.TypeConverters;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.Date;

@Entity(tableName = "Party",
        primaryKeys = { "number", "placeID", "roleID" }
        /*,indices = {
        @Index(value = { "id" },
        name = "UX_Party_ID",
        unique = true),
        }*/)
public class PartyEntity implements IEntityBase {
    //Getter methods
    public Date getCreatedDate() {
        return this._createdDate;
    }

    @NonNull
    public String getName() {
        return this._name;
    }

    @NonNull
    public String getNumber() {
        return this._number;
    }

    @NonNull
    public String getPlaceID() {
        return this._placeID;
    }

    @NonNull
    public Integer getRoleID() {
        return this._roleID;
    }

    public Date getUpdatedDate() {
        return this._updatedDate;
    }

    //Setter methods
    public void setCreatedDate(@NonNull Date createdDate) {
        this._createdDate = createdDate;
    }

    /*
    public void setId(@NonNull String id) {
    this._id = id;
    }
    */

    public void setName(@NonNull String name) {
        this._name = name;
    }

    public void setNumber(@NonNull String number) {
        this._number = number;
    }

    public void setPlaceID(@NonNull String placeID) {
        this._placeID = placeID;
    }

    public void setRoleID(@NonNull Integer roleID) {
        this._roleID = roleID;
    }

    public void setUpdatedDate(Date updatedDate) {
        this._updatedDate = updatedDate;
    }

    public void setUpdateDateToCurrentDate() {
        this._updatedDate = new Date(System.currentTimeMillis());
    }

    //Constructors Methods.
    public PartyEntity() {
        this._createdDate = new Date(System.currentTimeMillis());
    }

    public PartyEntity(@NonNull String number
            , @NonNull String placeId
            , @NonNull String name
            , @NonNull Integer roleID) {
        this();

        this._placeID = placeId;
        this._roleID = roleID;
        this._number = number;
        this._name = name;
    }

    //Fields declaration
    @ColumnInfo(name="createdDate")
    @TypeConverters(TimestampConverter.class)
    private Date _createdDate;

    //@ColumnInfo(name="id")
    //@PrimaryKey
    //@NonNull
    //private String _id;

    @ColumnInfo(name="name")
    @NonNull
    private String _name;

    @ColumnInfo(name="number")
    @NonNull
    private String _number;

    @ColumnInfo(name="placeID")
    @NonNull
    private String _placeID;

    @ColumnInfo(name="roleID")
    @NonNull
    private Integer _roleID;

    @ColumnInfo(name="updatedDate")
    @TypeConverters(TimestampConverter.class)
    private Date _updatedDate;
}