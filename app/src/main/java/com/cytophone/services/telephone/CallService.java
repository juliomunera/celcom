package com.cytophone.services.telephone;

import com.android.internal.telephony.ITelephony;
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
import android.net.Uri;

import java.lang.reflect.Method;

public final class CallService extends InCallService {
    @Override
    public void onCallAdded(@NotNull Call call) {
        super.onCallAdded(call);

        Log.d("D/ClosedComm", "OnCallAdded");
        Log.d("D/ClosedComm", "Call details: " + call.getDetails());

        String callerNumber = call.getDetails().getHandle().getSchemeSpecificPart();
        PartyEntity party = CytophoneApp.getPartyHandlerDB().searchSuscriber(callerNumber);

        OngoingCall.INSTANCE.setCall(call);
        CallView.start((Context) this, call, party);
    }

    @Override
    public void onCallRemoved(@NotNull Call call) {
        super.onCallRemoved(call);

        Log.d("D/ClosedComm", "OnCallRemoved");
        Log.d("D/ClosedComm", "Call details: " + call.getDetails());

        OngoingCall.INSTANCE.setCall(null);
    }

    @Override
    public void onConnectionEvent(Call call, String event, Bundle extras) {
        super.onConnectionEvent(call, event, extras);

        Log.d("D/ClosedComm", "Disconnect code: " +
                call.getDetails().getDisconnectCause().getCode());
        Log.d("D/ClosedComm", "Disconnect reason: " +
                call.getDetails().getDisconnectCause().getReason());
        Log.d("D/ClosedComm", "Disconnect description: " +
                call.getDetails().getDisconnectCause().getDescription());
        Log.d("D/ClosedComm", "Event: " + event);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("D/ClosedComm", "OnStart");
        return START_STICKY;
    }

    private void rejectCall() {
        try {
            TelephonyManager tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("SoonBlockedPrivateApi")
            Method m = tm.getClass().getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            telephonyService.endCall();
        } catch (Exception e) {
            Log.d("D/ClosedComm", "Error -> " + e.getMessage());
        }
    }


    private Handler _handler = new Handler();
    private Runnable _hangUp = new Runnable() {
        @Override
        public void run() {
            _call.reject(true,"No Authorized");
            //_call.disconnect();
        }
    };
    private Call _call;
}
