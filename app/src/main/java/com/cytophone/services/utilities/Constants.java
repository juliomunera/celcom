package com.cytophone.services.utilities;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class Constants {
    public static final Pattern HEXADECIMAL_PATTERN = compile("\\p{XDigit}+");
    public static final Pattern B64_PATTERN = compile("^([A-Za-z0-9+/]{4})" +
            "*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");

    public static final Pattern TIME_ELAPSED_PATTERN = compile("^[0-9]{4}$");
    public static final Pattern PARTY_NAME_PATTERN = compile("^([A-Za-zÀ-ÿ" +
            "\\u00f1\\u00d1\\s0-9.*_-]{1,25})$");
    public static final Pattern MSISDN1_PATTERN = compile("^573[0-9]{9}$");
    public static final Pattern MSISDN2_PATTERN = compile("^3[0-9]{9}$");
    public static final Pattern PLACEID_PATTERN = compile("^[0-9]{6}$");

    public static final Pattern CODE_PATTERN = compile("^[0-9]{4}$");
    public static final Pattern ACTION_PATTERN = compile("^[1-7]$");

    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DOB_FORMAT = "yyyy-MM-dd";

    public static final int READ_PHONE_STATE_PERMISSIONS_REQUEST = 0;
    public static final int ANSWER_PHONE_CALLS_REQUEST = 0; //101;
    public static final int MODIFY_PHONE_STATE_REQUEST = 123;
    public static final int CALL_PHONE_REQUEST = 1;

    public static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    public static final int READ_SMS_PERMISSIONS_REQUEST = 1;

    public static final int DEFAULT_COPY_BUFFER_SIZE = 1024;

    public static final String START_ACTIVITY_TAG = "StartActivity";
    public static final String SMS_BCR_TAG = "BroadcastReceiver";
    public static final String DEV_ADMIN_TAG = "DeviceAdmin";

    public static final String[] KNOW_ERRORS = new String[] {
            "La acción %1$s no es válida.",
            "El identificador de ubicación %1$s no es válido.",
            "El número móvil %1$s no es válido.",
            "El nombre del subscriptor no es válido %1$s no es válido.",
            "El código de desbloqueo %1$s no es válido.",
            "El tiempo de vigencia para el código de desbloqueo %1$s tisn't no es válido ."
    };
}

