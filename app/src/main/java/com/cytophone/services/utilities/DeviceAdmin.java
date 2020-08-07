package com.cytophone.services.utilities;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceAdmin extends DeviceAdminReceiver {
    /**
     * @param context The context of the application.
     * @return The component name of this component in the given context.
     */
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(),
                DeviceAdmin.class);
    }

    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
        super.onLockTaskModeEntering(context, intent, pkg);
        Log.d(Constants.DEV_ADMIN_TAG, "onLockTaskModeEntering");
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        super.onLockTaskModeExiting(context, intent);
        Log.d(Constants.DEV_ADMIN_TAG, "onLockTaskModeExiting");
    }
}
