package com.cytophone.services;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import android.provider.CallLog;
import android.app.Service;

import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;

public class CellCommAgent extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(this.TAG + ".onCreate", "");

        IntentFilter filter = new IntentFilter();
        filter.addAction(this.CELLCOMM_EVENT);
        registerReceiver(this._cleanercalls, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG + ".onDestroy", "");
        unregisterReceiver(this._cleanercalls);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(this.TAG + ".onStartCommand", "");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    //region fields declaration
    BroadcastReceiver _cleanercalls = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(this.TAG + ".onReceive", "receive message.");
            String number = getNumberFromIntent(intent);
            int deleted = 0;

            if (null != number) deleted = deleteCallLog(number);
            Log.i(this.TAG + ".onReceive", "total calls deleted: " + deleted);
        }

        private int deleteCallLog(String callerNumber) {
            ContentResolver cr = getContentResolver();
            String query = CallLog.Calls.NUMBER + " = ?";
            String[] args = { callerNumber };

            return cr.delete(CallLog.Calls.CONTENT_URI, query, args);
        }

        private final String getNumberFromIntent(Intent filter) {
            if (filter.getAction().equals(CELLCOMM_EVENT)) {
                Bundle bundle = filter.getExtras();
                return bundle.getString("NUMBER");
            }
            return null;
        }

        final String TAG = "CellCommAgent.BroadcastReceiver";
    };

    final String CELLCOMM_EVENT = "CELLCOMM_MESSAGE_CALLMGMT";
    final String TAG = "CellCommAgent";
    //endregion
}
