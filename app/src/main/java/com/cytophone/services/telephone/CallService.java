package com.cytophone.services.telephone;

import com.cytophone.services.entities.PartyEntity;
import com.cytophone.services.utilities.Utils;
import com.cytophone.services.CleanerCallLog;
import com.cytophone.services.views.CallView;
import com.cytophone.services.CellCommApp;

import org.jetbrains.annotations.NotNull;

import android.telecom.InCallService;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

public final class CallService extends InCallService {
    @Override
    public void onCallAdded(@NotNull Call call) {
        super.onCallAdded(call);
        Log.d(this.TAG + ".OnCallAdded", "call details: " + call.getDetails() );

        try {
            String number = call.getDetails().getHandle().getSchemeSpecificPart();
            PartyEntity party = CellCommApp.getPartyHandlerDB().searchSuscriber(number);

            //Se habilita la recepción de llamadas de #s desconocidos 2021/03/31
            //if ( null == party  || getCalls().size() > 1 ) {
            if ( getCalls().size() > 1 ) {
                call.reject(false, "");
                call.disconnect();
            } else {
                //Se habilita la recepción de llamadas de #s desconocidos 2021/03/31
                if ( null == party )   party = getAnonymousParty( number );
                OngoingCall.INSTANCE.setCall(call);
                CallView.start((Context) this, call, party);
            }
        } finally {
            this.schedulerDeleteCallLog(30);
        }
    }

    @Override
    public void onCallRemoved(@NotNull Call call) {
        super.onCallRemoved(call);
        Log.d(this.TAG + ".OnCallRemoved", "call details: " + call.getDetails() );

        try {
            Call currentCall = OngoingCall.INSTANCE.getCall();
            if( currentCall != null && call != null ) {
                if( OngoingCall.INSTANCE.getCall().equals(call) ) {
                    OngoingCall.INSTANCE.setCall(null);
                }
            }
        } finally {
            this.schedulerDeleteCallLog(10);
        }
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

    private void schedulerDeleteCallLog(int seconds) {
        Date start = Utils.getCurrentTime("EST");
        Date end = Utils.addSeconds( start,seconds);
        int timeOut = (int) (end.getTime() - start.getTime()) / 1000;

        CleanerCallLog cleaner = new CleanerCallLog(this.getBaseContext(), timeOut);
    }

    private PartyEntity getAnonymousParty(String number)
    {
        String countryCode = "57", placeID = "000000", name = "Número no identificado";
        return  new PartyEntity(countryCode, number,placeID,name,2 );
    }

    final String TAG = "CallService";
}
