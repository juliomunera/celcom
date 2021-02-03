package com.cytophone.services;

import android.app.admin.DeviceAdminReceiver;
import android.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import com.cytophone.services.views.ContactView;

public class DeviceAdministrator extends DeviceAdminReceiver {
    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
        super.onLockTaskModeEntering(context, intent, pkg);
        Log.d(this.TAG + ".onLockTaskModeEntering", "");
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        super.onLockTaskModeExiting(context, intent);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        context.startActivity(new Intent(context, ContactView.class).
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        Log.d(this.TAG + ".onLockTaskModeExiting","");
    }

    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext()
                ,DeviceAdministrator.class);
    }

    final String TAG = "DeviceAdministrator";
}
