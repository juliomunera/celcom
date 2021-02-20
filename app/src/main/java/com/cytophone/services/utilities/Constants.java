package com.cytophone.services.utilities;

import android.Manifest;
import android.os.BatteryManager;
import android.view.View;

import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

public class Constants {
    public static final Pattern HEXADECIMAL_PATTERN = compile("\\p{XDigit}+");

    public static final Pattern B64_PATTERN = compile("^([A-Za-z0-9+/]{4})" +
            "*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");

    public static final Pattern TIME_ELAPSED_PATTERN = compile("^[0-9]{4}$");

    public static final Pattern PARTY_NAME_PATTERN = compile("^([A-Za-zÀ-ÿ" +
            "\\u00f1\\u00d1\\s0-9.*_-]{1,25})$");

    public static final Pattern MSISDN_PATTERN = compile("^(?!000000000000)" +
            "(?!999999999999)([0-9]{12}$)");
    /*
    public static final Pattern MSISDN1_PATTERN = compile("^573[0-9]{9}$");
    public static final Pattern MSISDN2_PATTERN = compile("^03[0-9]{8}$");
    public static final Pattern MSISDN3_PATTERN = compile("^3[0-9]{9}$");
    */
    public static final Pattern PLACEID_PATTERN = compile("^[0-9]{6}$");

    /*
    public static final Pattern ACTION_PATTERN = compile("^([1|3|4|6|7|8|9]|10)$");
    */
    public static final Pattern ACTION_PATTERN = compile("^([1|3|4|6|7|8|9])$");

    public static final Pattern CODE_PATTERN = compile("^[0-9]{4}$");

    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DOB_FORMAT = "yyyy-MM-dd";

    public static final String[] APP_PERMISSIONS = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS
    };

    public static final int UI_FLAGS_IMMERSE_DISABLE =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

    public static final int UI_FLAGS_IMMERSE_ENABLE =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_FULLSCREEN;

    public static final int BATTERY_FLAGS =
            BatteryManager.BATTERY_PLUGGED_AC |
            BatteryManager.BATTERY_PLUGGED_USB |
            BatteryManager.BATTERY_PLUGGED_WIRELESS;
}

