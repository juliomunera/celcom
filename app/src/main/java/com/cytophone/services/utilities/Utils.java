package com.cytophone.services.utilities;

import java.io.UnsupportedEncodingException;
import android.content.Context;

import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;

import java.util.regex.Matcher;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.util.Base64;
import org.json.JSONArray;

import android.util.Log;
import java.util.List;

public class Utils {
    public static <T> List<T> getList(
            JSONArray jsonArray,
            IParseCallable<T> callable) throws Exception {
        List<T> list = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            T t = callable.call(jsonArray, i);
            list.add(t);
        }
        return list;
    }

    private static String loadJSONFromAsset(Context Context, String jsonAssets) {
        String json;
        try {
            InputStream is =  Context.getAssets().open(jsonAssets);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.d("Asset data", json);
        return json;
    }

    public static boolean isBase64Encode(final String string) {
        final String sanitized = string.replaceAll("\\s", "");
        Matcher m = Constants.B64_PATTERN.matcher(sanitized);
        return m.find();
    }

    public static boolean isHexadecimal(final String input) {
        Matcher m = Constants.HEXADECIMAL_PATTERN.matcher(input);
        return m.matches();
    }

    public static String decodeBase64(String coded) {
        String msg = "";
        try {
            msg =  new String(Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (Exception ex) {
            Log.e(Utils.TAG + ".decodeBase64",  ex.getMessage());
        }
        return msg;
    }

    public static String encodeBase64(String text) {
        String msg = "";
        try {
            msg =  new String(Base64.encode(text.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            Log.e(Utils.TAG + ".encodeBase64",  e.getMessage());
        }
        return msg;
    }

    public static String convertHexToString(String hexValue) {
        String s1 = "";
        for (int i = 0; i < hexValue.length(); i += 2) {
            String s2 = hexValue.substring(i, (i + 2));
            int decimal = Integer.parseInt(s2, 16);
            s1 = s1 + (char) decimal;
        }
        return s1;
    }

    public static String convertStringToHex(String value) {
        final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
        byte[] buffer =  value.getBytes();

        char[] chars = new char[2 * buffer.length];
        for (int i = 0; i < buffer.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buffer[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buffer[i] & 0x0F];
        }
        return new String(chars);
    }

    public static Date getCurrentTime(String timeZoneId) {
        Calendar calTZ = new GregorianCalendar(TimeZone.getTimeZone(timeZoneId));
        calTZ.setTimeInMillis(new Date().getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calTZ.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calTZ.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calTZ.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calTZ.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calTZ.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calTZ.get(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calTZ.get(Calendar.MILLISECOND));

        return calendar.getTime();
    }

    public static Date addSeconds(Date period, int seconds) {
        Calendar clnd = Calendar.getInstance();
        clnd.setTime(period);
        clnd.add(Calendar.SECOND, seconds);

        return clnd.getTime();
    }

    private static final String TAG = "Utils";
}