package com.cytophone.services;

import android.content.BroadcastReceiver;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class LockerDevice extends BroadcastReceiver {
    public LockerDevice() {}

    public LockerDevice(Context context,  int timeoutInSeconds){
        AlarmManager alarmMgr = (AlarmManager)context.
                    getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, LockerDevice.class);

        PendingIntent pi = PendingIntent.getBroadcast(context
                , 0
                , i
                , PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, timeoutInSeconds);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent(intent.getAction()));
    }

    private final String REMINDER_BUNDLE = "CellCommReminderBundle";
}
