package com.cytophone.services.telephone;

import com.cytophone.services.CytophoneApp;
import com.cytophone.services.activities.CallView;
import com.cytophone.services.entities.PartyEntity;

import org.jetbrains.annotations.NotNull;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.telecom.InCallService;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public final class CallService extends InCallService {
    @Override
    public void onCallAdded(@NotNull Call call) {
        super.onCallAdded(call);

        Log.d("D/CellComm", "OnCallAdded");
        Log.d("D/CellComm", "Call details: " + call.getDetails());

        String callerNumber = call.getDetails().getHandle().getSchemeSpecificPart();
        PartyEntity party = CytophoneApp.getPartyHandlerDB().searchSuscriber(callerNumber);

        OngoingCall.INSTANCE.setCall(call);
        CallView.start((Context) this, call, party);
    }

    @Override
    public void onCallRemoved(@NotNull Call call) {
        super.onCallRemoved(call);

        Log.d("D/CellComm", "OnCallRemoved");
        Log.d("D/CellComm", "Call details: " + call.getDetails());

        OngoingCall.INSTANCE.setCall(null);
    }

    @Override
    public void onConnectionEvent(Call call, String event, Bundle extras) {
        super.onConnectionEvent(call, event, extras);

        Log.d("D/CellComm", "Disconnect code: " +
                call.getDetails().getDisconnectCause().getCode());
        Log.d("D/CellComm", "Disconnect reason: " +
                call.getDetails().getDisconnectCause().getReason());
        Log.d("D/CellComm", "Disconnect description: " +
                call.getDetails().getDisconnectCause().getDescription());
        Log.d("D/CellComm", "Event: " + event);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("D/CellComm", "onStartCommand");
        return START_STICKY;
    }

    private Call _call;
}
