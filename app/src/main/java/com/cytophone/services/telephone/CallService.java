package com.cytophone.services.telephone;

import com.cytophone.services.entities.PartyEntity;
import com.cytophone.services.activities.CallView;
import com.cytophone.services.CellCommApp;

import org.jetbrains.annotations.NotNull;

import android.telecom.InCallService;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.os.Bundle;
import android.util.Log;

public final class CallService extends InCallService {
    @Override
    public void onCallAdded(@NotNull Call call) {
        super.onCallAdded(call);
        Log.d(this.TAG + ".OnCallAdded", "call details->" + call.getDetails());

        try {
            String callerNumber = call.getDetails().getHandle().getSchemeSpecificPart();
            PartyEntity party = CellCommApp.getPartyHandlerDB().searchSuscriber(callerNumber);

            sendBroadCastMessage(1, callerNumber);

            OngoingCall.INSTANCE.setCall(call);
            CallView.start((Context) this, call, party);
        } finally {}
    }

    @Override
    public void onCallRemoved(@NotNull Call call) {
        super.onCallRemoved(call);
        Log.d(this.TAG + ".OnCallRemoved", "call details->" + call.getDetails());

        try {
            String callerNumber = call.getDetails().getHandle().getSchemeSpecificPart();

            sendBroadCastMessage(0, callerNumber);
        } finally {}

        OngoingCall.INSTANCE.setCall(null);
    }

    @Override
    public void onConnectionEvent(Call call, String event, Bundle extras) {
        super.onConnectionEvent(call, event, extras);

        Log.d(this.TAG + ".onConnectionEvent", "disconnect code: " +
                call.getDetails().getDisconnectCause().getCode());
        Log.d(this.TAG + ".onConnectionEvent", "disconnect reason: " +
                call.getDetails().getDisconnectCause().getReason());
        Log.d(this.TAG + ".onConnectionEvent", "disconnect description: " +
                call.getDetails().getDisconnectCause().getDescription());
        Log.d(this.TAG + ".onConnectionEvent", "event: " + event);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(this.TAG + ".onStartCommand", "");
        return START_STICKY;
    }

    private void sendBroadCastMessage(int type, String callerNumber) {
        Intent intent = new Intent(CELLCOMM_EVENT).
            putExtra("EVENT_TYPE", type).
            putExtra( "CALLER_NUMBER", callerNumber);
        this.getApplicationContext().sendBroadcast(intent);
    }

    final String CELLCOMM_EVENT = "CELLCOMM_MESSAGE_CALLMGMT";
    final String TAG = "CallService";
}
