package com.cytophone.services.entities;

//import com.cytophone.services.dao.*;
//import androidx.room.TypeConverters;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormat;

import java.util.Date;
import java.util.UUID;

@Entity( tableName = "EventLog"
         , indices = { @Index(value = { "createdDate" }, name = "IX_EventLog_createdDate") })
public class EventEntity implements IEntityBase {
    @Ignore
    private Date getDate(String dateTime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        } catch (ParseException pex) {
            return null;
        }
    }

    //region getter methods
    @NonNull
    public String getAPartyNumber() {
        return this._aPartyNumber;
    }

    @NonNull
    public String getBPartyNumber() {
        return this._bPartyNumber;
    }

    public String getCreatedDate() {
        return this._createdDate;
    }

    public String getEndDateTime() {
        return this._endDateTime;
    }

    @NonNull
    public String getEventType() {
        return this._eventType;
    }

    @NonNull
    public String getId() {
        return this._id;
    }

    @NonNull
    public String getAction() {
        return this._action;
    }

    @NonNull
    public void setAction(String value ) {
        this._action = value;
    }

    public String getStartDateTime() {
        return this._startDateTime;
    }
    //endregion

    //region setter methods
    public void setAPartyNumber(@NonNull String number) {
        this._aPartyNumber = number;
    }

    public void setBPartyNumber(@NonNull String number) {
        this._bPartyNumber = number;
    }

    public void setCreatedDate(String createdDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(createdDate);
        } catch (Exception ex) {
            createdDate = sdf.format(new Date(System.currentTimeMillis()));
        }
        this._createdDate = createdDate;
    }

    public void setId(String id) {
        this._id = id;
    }

    public void setEndDateTime(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateTime);
        } catch (Exception ex) {
            dateTime = sdf.format(new Date(System.currentTimeMillis()));
        }
        this._endDateTime = dateTime;
    }

    public void setEventType(@NonNull String type) {
        this._eventType = type;
    }

    public void setStartDateTime(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateTime);
        } catch (Exception ex) {
            dateTime = sdf.format(new Date(System.currentTimeMillis()));
        }
        this._startDateTime = dateTime;
    }
    //endregion

    //region constructor methods
    @Ignore()
    public EventEntity() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this._createdDate =  sdf.format(new Date(System.currentTimeMillis()));
        this._id = UUID.randomUUID().toString();
    }

    @Ignore()
    public EventEntity(@NonNull String aPartyNumber,
                       @NonNull String bPartyNumber,
                       @NonNull String eventType,
                       @NonNull String startDateTime,
                       @NonNull String action)  {
        this();

        Date date = getDate(startDateTime);
        if ((new Date(System.currentTimeMillis())).compareTo(date) >= 0) {
            startDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                    format(new Date(System.currentTimeMillis()));
        }
        this._startDateTime = startDateTime;
        this._aPartyNumber = aPartyNumber;
        this._bPartyNumber = bPartyNumber;
        this._eventType = eventType;
        this._action = action;
    }

    public EventEntity(@NonNull String aPartyNumber,
                       @NonNull String bPartyNumber,
                       @NonNull String eventType,
                       @NonNull String startDateTime,
                       @NonNull String endDateTime,
                       @NonNull String action)  {
        this(aPartyNumber, bPartyNumber, eventType, startDateTime, action);
        if (endDateTime != null && startDateTime.compareTo(endDateTime) >= 0) {
            this._endDateTime = endDateTime;
        }
    }
    //endregion

    //region fields declarations
    @ColumnInfo(name = "aPartyNumber")
    @NonNull
    private String _aPartyNumber;

    @ColumnInfo(name = "bPartyNumber")
    @NonNull
    private String _bPartyNumber;

    @ColumnInfo(name = "createdDate")
    //@TypeConverters(TimestampConverter.class)
    @NonNull
    private String _createdDate;

    @ColumnInfo(name = "eventType")
    @NonNull
    private String _eventType;

    @ColumnInfo(name = "endDateTime")
    //@TypeConverters(TimestampConverter.class)
    private String _endDateTime;

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private String _id;

    @ColumnInfo(name = "startDateTime") // defaultValue = "DATETIME('now')"
    //@TypeConverters(TimestampConverter.class)
    private String _startDateTime;

    @ColumnInfo(name = "action")
    @NonNull
    private String _action;
    //endregion
}