package com.cytophone.services.utilities;

import org.jetbrains.annotations.NotNull;

public class CallStateStringKt {
    @NotNull
    public static final String asString(int value) {
        switch(value) {
            case 0: return "NEW";
            case 1:return "DIALING";
            case 2: return "RINGING";
            case 3: return "HOLDING";
            case 4: return "ACTIVE";
            case 7: return "DISCONNECTED";
            case 8: return "SELECT_PHONE_ACCOUNT";
            case 9: return "CONNECTING";
            case 10: return "DISCONNECTING";

            default: return "UNKNOWN";
        }
    }
}