package com.cytophone.services.dao;

import com.cytophone.services.utilities.Constants;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormat;
import java.util.Date;

import androidx.room.TypeConverter;

public class TimestampConverter {
    static DateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT);

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TypeConverter
    public static String dateToTimestamp(Date value) {
        return value == null ? null : df.format(value);
    }
}

