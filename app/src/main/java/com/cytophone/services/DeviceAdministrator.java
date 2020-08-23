package com.cytophone.services;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceAdministrator extends DeviceAdminReceiver {
    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
        super.onLockTaskModeEntering(context, intent, pkg);
        Log.d(this.TAG + ".onLockTaskModeEntering", "");
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        super.onLockTaskModeExiting(context, intent);
        Log.d(this.TAG + ".onLockTaskModeExiting","");
    }

    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext()
                ,DeviceAdministrator.class);
    }

    final String TAG = "DeviceAdministrator";
}
