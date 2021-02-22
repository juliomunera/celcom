package com.cytophone.services.entities;

import com.cytophone.services.dao.*;

import androidx.room.TypeConverters;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import java.io.Serializable;
import java.util.Date;

@Entity
(
        tableName = "Party"
        , primaryKeys = { "countryCode", "number", "placeID", "roleID" }
        , indices = {
              @Index(value = { "name" }, name = "IX_Party_Name")
            , @Index(value = { "number", "roleID" }, name = "IX_Party_Number_RoleID")
        }
        /*
        indices =
        {
            @Index(value = { "id" }, name = "UX_Party_ID", unique = true),
        }*/
)
public class PartyEntity implements IEntityBase, Serializable  {
    //region getter methods
    public Date getCreatedDate() {
        return this._createdDate;
    }

    @NonNull
    public String getCodedNumber() {
        return this._codedNumber;
    }

    @NonNull
    public String getCountryCode() { return this._countryCode; }

    @NonNull
    public String getName() {
        return this._name;
    }

    @Ignore
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

    @Ignore
    public String getNameAndPlaceID() {
        return this._name + this._placeID;
    }

    //region setter methods
    public void setCreatedDate(@NonNull Date createdDate) {
        this._createdDate = createdDate;
    }

    public void setCodedNumber(@NonNull String number) {
        this._codedNumber = number;
    }

    public void setCountryCode(@NonNull String countryCode) {
        this._countryCode = countryCode;
    }

    public void setCompleteNumber(@NonNull String code) {
        this._countryCode = code;
    }
    public void setName(@NonNull String name) {
        this._name = name;
    }

    @Ignore
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
    //endregion

    //region constructors methods.
    public PartyEntity() {
        this._createdDate = new Date(System.currentTimeMillis());
    }

    public PartyEntity(@NonNull String countryCode
            , @NonNull String number
            , @NonNull String placeId
            , @NonNull String name
            , @NonNull Integer roleID) {
        this();

        this._countryCode = countryCode;
        this._placeID = placeId;
        this._roleID = roleID;
        this._number = number;
        this._name = name;
    }
    //endregion

    //region fields declarations
    @ColumnInfo(name="createdDate")
    @TypeConverters(TimestampConverter.class)
    private Date _createdDate;

    @ColumnInfo(name="countryCode")
    @NonNull
    private String _countryCode;

    @ColumnInfo(name="number")
    @NonNull
    private String _codedNumber;

    @ColumnInfo(name="name")
    @NonNull
    private String _name;

    @Ignore
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
    //endregion
}