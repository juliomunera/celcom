package com.cytophone.services.utilities;

import static java.util.regex.Pattern.compile;
import java.util.regex.Pattern;

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
    public static final Pattern ACTION_PATTERN = compile("^[1|3|4|6|7]$");

    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DOB_FORMAT = "yyyy-MM-dd";
}

