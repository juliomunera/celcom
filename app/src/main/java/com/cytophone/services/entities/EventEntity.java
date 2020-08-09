package com.cytophone.services.entities;

import com.cytophone.services.dao.*;

import androidx.room.TypeConverters;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import java.util.Date;
import java.util.UUID;

@Entity( tableName = "EventLog"
         , indices = { @Index(value = { "createdDate" }, name = "IX_EventLog_createdDate") })
public class EventEntity implements IEntityBase {
    //region getter methods
    @NonNull
    public String getAPartyNumber() {
        return this._aPartyNumber;
    }

    @NonNull
    public String getBPartyNumber() {
        return this._bPartyNumber;
    }

    public Date getCreatedDate() {
        return this._createdDate;
    }

    public Date getEndDateTime() {
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

    public Date getStartDateTime() {
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

    public void setCreatedDate(Date createdDate) {
        this._createdDate = createdDate;
    }

    public void setId(String id) {
        this._id = id;
    }

    public void setEndDateTime(Date dateTime) {
        this._endDateTime = dateTime;
    }

    public void setEventType(@NonNull String type) {
        this._eventType = type;
    }

    public void setStartDateTime(Date dateTime) {
        this._startDateTime = dateTime;
    }
    //endregion

    //region constructor methods
    @Ignore()
    public EventEntity() {
        this._createdDate = new Date(System.currentTimeMillis());
        this._id = UUID.randomUUID().toString();
    }

    @Ignore()
    public EventEntity(@NonNull String aPartyNumber,
                       @NonNull String bPartyNumber,
                       @NonNull String eventType,
                       @NonNull Date startDateTime) {
        this();

        if ((new Date(System.currentTimeMillis())).compareTo(startDateTime) >= 0) {
            this._startDateTime = startDateTime;
        }

        this._startDateTime = startDateTime;
        this._aPartyNumber = aPartyNumber;
        this._bPartyNumber = bPartyNumber;
        this._eventType = eventType;
    }

    public EventEntity(@NonNull String aPartyNumber,
                       @NonNull String bPartyNumber,
                       @NonNull String eventType,
                       @NonNull Date startDateTime,
                       @NonNull Date endDateTime) {
        this(aPartyNumber, bPartyNumber, eventType, startDateTime);
        if (startDateTime.compareTo(endDateTime) >= 0) {
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
    @TypeConverters(TimestampConverter.class)
    @NonNull
    private Date _createdDate;

    @ColumnInfo(name = "eventType")
    @NonNull
    private String _eventType;

    @ColumnInfo(name = "endDateTime")
    @TypeConverters(TimestampConverter.class)
    private Date _endDateTime;

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private String _id;

    @ColumnInfo(name = "startDateTime") // defaultValue = "DATETIME('now')"
    @TypeConverters(TimestampConverter.class)
    private Date _startDateTime;
    //endregion
}