package com.cytophone.services;

import android.content.BroadcastReceiver;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;

import java.util.Calendar;

public class LockDeviceReceiver extends BroadcastReceiver {
    public LockDeviceReceiver() {}

    public LockDeviceReceiver(Context context, Bundle extras, int timeoutInSeconds){
        AlarmManager alarmMgr = (AlarmManager)context.
                    getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LockDeviceReceiver.class);
        intent.putExtra(REMINDER_BUNDLE, extras);
        PendingIntent pi = PendingIntent.getBroadcast(context
                , 0
                , intent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, timeoutInSeconds);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
    }

    private final String REMINDER_BUNDLE = "CellCommReminderBundle";
}
