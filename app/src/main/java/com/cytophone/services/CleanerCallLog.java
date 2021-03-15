package com.cytophone.services;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlarmManager;

import android.provider.CallLog;

import java.util.Calendar;
import android.util.Log;

public class CleanerCallLog extends BroadcastReceiver {
    public CleanerCallLog() {}

    public CleanerCallLog(Context context, int timeoutInSeconds){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, CleanerCallLog.class);

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
        try {
            this.deleteCallsFromStatusBar(context);
            this.deleteCallsFromCallLog(context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteCallsFromCallLog(Context context) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);

        String query = CallLog.Calls.DATE + " <= ?";
        String[] arguments = new String[]{String.valueOf(cal.getTime())};

        ContentResolver cr = context.getContentResolver();
        int deleted = cr.delete(CallLog.Calls.CONTENT_URI, query, arguments);
        Log.i(this.TAG + ".onReceive", "total llamadas eliminadas -> " + deleted);
    }

    private void deleteCallsFromStatusBar(Context context) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm = (NotificationManager) context.getSystemService(ns);
        nm.cancelAll();
    }

    private final String TAG = "CleanerCallLog";
}
