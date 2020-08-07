package com.cytophone.services.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.MenuItem;

import com.cytophone.services.DeviceAdministrator;
import com.cytophone.services.R;
import com.cytophone.services.fragments.ContactsFragment;
import com.cytophone.services.fragments.MessageFragment;
import com.cytophone.services.fragments.SecurityFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/// hamilton

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.UserManager;
import android.os.Handler;

import android.provider.Settings;
import android.widget.Toast;
import android.view.View;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class ContactListitem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_listitem);
        showSelectedFragment(new ContactsFragment(ContactListitem.this));

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.contactdevice:
                        showSelectedFragment(new ContactsFragment(ContactListitem.this));
                        break;
                    case R.id.smsmessages:
                        showSelectedFragment(new MessageFragment());
                        break;
                    case R.id.blockoption:
                        showSelectedFragment(new SecurityFragment());
                        break;
                }
                return true;
            }
        });

        this.checkPermissions();
        this.checkDefaultSMS();
        this.startLockTaskDelayed();
    }

    private void checkDefaultSMS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (Telephony.Sms.getDefaultSmsPackage(this).equals(this.getPackageName())) return;

        Intent i = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT).
                putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, this.getPackageName());
        startActivityForResult(i, 123);
    }

    private void checkSetDefaultResult(int resultCode) {
        String message = resultCode == RESULT_CANCELED ?
                "User accepted request to become default app" :
                resultCode != RESULT_OK ?
                        "User accepted request to become default app" :
                        "User don't accepted request to become default app";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private final void offerReplacingDefaultSMS() {
        if (this.getPackageName().equals(Telephony.Sms.
                getDefaultSmsPackage(this.getApplicationContext()))) {
            final Intent i = new Intent("android.Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT").
                    putExtra("android.Telephony.Sms.Intents.EXTRA_PACKAGE_NAME",
                            this.getPackageName());
            this.startActivity(i);
        }
    }

    public void makeCall(String number) {

        if (PermissionChecker.checkSelfPermission(this,
                "android.permission.CALL_PHONE") == 0) {

            Uri uri = Uri.parse("tel:" + number);
            this.startActivity(new Intent("android.intent.action.CALL", uri));
            Intent i = new Intent(this, MakeCall.class);
            this.startActivity(i);
        } else {

            ActivityCompat.requestPermissions( (Activity)this,
                    new String[]{"android.permission.CALL_PHONE"},
                    0);
        }
    }

    private void showSelectedFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    //region override methods.
    @Override
    public void onBackPressed() {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            //checkSetDefaultDialerResult(resultCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {

        if( requestCode == REQUEST_CODE_ASK_PERMISSIONS ) {
            for (int index = permissions.length - 1; index >= 0; --index) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    // Exit the app if one permission is not granted.
                    Toast.makeText(this, "El permiso '" +
                                    permissions[index] + "' no fue otorgado.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    //region private methods declaration
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();

        // Check all required dynamic permissions.
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {
            // Request all missing permissions.
            final String[] permissions = missingPermissions.toArray(
                    new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS,
                    REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    private void initializePolices(){
        int owner = R.string.deviceOwner;

        adminName = DeviceAdministrator.getComponentName(this);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        if (!dpm.isDeviceOwnerApp(getPackageName())) {
            owner = R.string.notDeviceOwner;
            this.isAdmin = false;
        }

        Toast.makeText(this, owner, Toast.LENGTH_SHORT).show();
        this.setPolicies(true, this.isAdmin);
    }

    private void enableStayOnWhilePluggedIn(Boolean active) {
        if (active) {
            this.dpm.setGlobalSetting(this.adminName,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                    Integer.toString(BatteryManager.BATTERY_PLUGGED_AC
                            | BatteryManager.BATTERY_PLUGGED_USB
                            | BatteryManager.BATTERY_PLUGGED_WIRELESS));
        } else {
            this.dpm.setGlobalSetting(this.adminName,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                    "0");
        }
    }

    private void setAsHomeApp(Boolean enable) {
        if (enable) {
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
            intentFilter.addCategory(Intent.CATEGORY_HOME);
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

            this.dpm.addPersistentPreferredActivity(
                    this.adminName,
                    intentFilter,
                    new ComponentName(getPackageName(),
                            ContactListitem.class.getName())
            );
        } else {
            ContactListitem.this.dpm.clearPackagePersistentPreferredActivities(
                    ContactListitem.this.adminName,
                    getPackageName());
        }
    }

    private void setKeyGuardEnabled(Boolean enable) {
        this.dpm.setKeyguardDisabled(this.adminName, !enable);
    }

    private void setImmersiveMode(Boolean enable) {
        int flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (enable) {
            flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        View decorView = this.getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
    }

    private void setRestrictions(Boolean disallow) {
        this.setUserRestriction(UserManager.DISALLOW_ADD_USER, disallow);
        this.setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, disallow);
        this.setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, disallow);
        this.setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, disallow);
        this.setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, disallow);
    }

    private void setPolicies(Boolean enable, Boolean isAdministrator) {
        if (isAdministrator) {
            this.setRestrictions(enable);
            this.enableStayOnWhilePluggedIn(enable);
            this.setUpdatePolicy(enable);
            this.setAsHomeApp(enable);
            this.setKeyGuardEnabled(enable);
        }
        this.setLockTask(enable, isAdministrator);
        this.setImmersiveMode(enable);
    }

    private void setLockTask(Boolean start, Boolean isAdministrator) {
        if (isAdministrator) {
            this.dpm.setLockTaskPackages(this.adminName,
                    new String[]{ getPackageName() });
        }
        if (start) this.startLockTask();
        else this.stopLockTask();
    }

    private void setUpdatePolicy(Boolean enable) {
        if (enable) {
            this.dpm.setSystemUpdatePolicy(this.adminName,
                    SystemUpdatePolicy.createWindowedInstallPolicy(60, 120));
        } else {
            this.dpm.setSystemUpdatePolicy(this.adminName,
                    null);
        }
    }

    private void setUserRestriction(String restriction, Boolean disallow) {
        if (disallow) {
            this.dpm.addUserRestriction(this.adminName,restriction);
        } else {
            this.dpm.clearUserRestriction(this.adminName,restriction);
        }
    }

    private void startLockTaskDelayed () {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // start lock task mode if its not already active
                try {
                    if( ContactListitem.this.isLocked ) {
                        initializePolices();
                        //activatePackages();
                    }
                } catch(IllegalArgumentException e) {
                    Log.e("D/ClosedComm", "Was not in foreground yet, try again..");
                    startLockTaskDelayed();
                }
            }
        }, 10);
    }
    //endregion

    //region fields declarations
    //Permissions that need to be explicitly requested from end user.
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS,
    };

    //Permissions request code.
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    public static final int REQUEST_PERMISSION = 0;

    DevicePolicyManager dpm = null;
    ComponentName adminName = null;

    boolean isLocked = true;
    boolean isAdmin = true;
    //endregion

}
