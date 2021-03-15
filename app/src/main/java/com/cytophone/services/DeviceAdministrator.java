package com.cytophone.services;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.os.UserManager;
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
        //DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(
        //        Context.DEVICE_POLICY_SERVICE);
        //ComponentName admin = DeviceAdministrator.getComponentName(context);
        //dpm.addUserRestriction(admin, UserManager.DISALLOW_CREATE_WINDOWS);
        Log.d(this.TAG + ".onLockTaskModeEntering", "");
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        super.onLockTaskModeExiting(context, intent);
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //context.startActivity(new Intent(context, ContactView.class).
        //            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        Log.d(this.TAG + ".onLockTaskModeExiting","");
    }

    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext()
                ,DeviceAdministrator.class);
    }

    final String TAG = "DeviceAdministrator";
}
