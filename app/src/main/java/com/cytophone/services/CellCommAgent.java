package com.cytophone.services;

import androidx.annotation.Nullable;
import android.content.Intent;
import android.app.Service;

import android.os.IBinder;
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG + ".onDestroy", "");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(this.TAG + ".onStartCommand", "");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    //region fields declaration
    final String TAG = "CellCommAgent";
    //endregion
}
