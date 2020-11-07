package com.cytophone.services.views;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.cytophone.services.DeviceAdministrator;
import com.cytophone.services.utilities.Constants;
import com.cytophone.services.entities.SMSEntity;
import com.cytophone.services.views.uicontrols.*;
import com.cytophone.services.utilities.Utils;
import com.cytophone.services.views.fragments.*;
import com.cytophone.services.R;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.app.Activity;

import androidx.core.content.PermissionChecker;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;

import android.provider.Telephony;
import android.provider.Settings;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.SignalStrength;
import android.telecom.TelecomManager;

import android.view.MenuItem;
import android.view.View;

import android.os.UserManager;
import android.os.Handler;
import android.os.Bundle;
import android.os.Build;
import android.net.Uri;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Arrays;
import android.util.Log;
import java.util.List;

public class ContactView extends AppCompatActivity {
    //region events methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_listitem);

        this.initializeReceivers();
        this.checkPermissions();
        //this.checkDefaultHome();
        this.checkDefaultSMS();
        this.checkDefaultDialer();
        this.initializeFragments();

        this.startLockTaskDelayed();
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) checkSetDefaultResult(resultCode);
    }

    public void onRequestPermissionsResult(int requestCode
            , @NotNull String[] permissions
            , @NotNull int[] grantResults) {
        String msg = "El permiso %s no fue autorizado, la aplicación se finalizará.";
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (int i = permissions.length - 1; i >= 0; --i) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this
                        , String.format( msg, permissions[i] )
                        , Toast.LENGTH_LONG).show();
                    this.closeNow(); // Exit the app if one permission is not granted.
                    return;
                }
            }
        }
    }

    protected void onStart() {
        super.onStart();

        //this.offerReplacingDefaultHome();
        //this.offerReplacingDefaultSMS();
        this.offerReplacingDefaultDialer();
    }
    //endregion

    //region initialize component methods
    private void initializeFragments() {
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                     Optional<Fragment> f = _fragments.stream().filter(i ->
                             ((IFragment) i).getID() == item.getItemId()).findFirst();
                     if (f != null) showSelectedFragment(f.get());
                     return f != null;
                 }
        });

        ((SecurityFragment)_fragments.get(2)).setListener( new View.OnClickListener() {
            public void onClick(View v) {
                ContactView.this.stopLockTask();
            }
        });
        this.showSelectedFragment(_fragments.get(0));
    }

    public  void initializePolices(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) return;

        int owner = R.string.deviceOwner;
        this._adminName = DeviceAdministrator.getComponentName(this);
        this._dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        if (!this._dpm.isDeviceOwnerApp(getPackageName())) {
            owner = R.string.notDeviceOwner;
            this._isAdmin = false;
        }

        Toast.makeText(this, owner, Toast.LENGTH_SHORT).show();
        this.setPolicies(true, this._isAdmin);
    }
    // endregion

    private void checkDefaultDialer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (!((TelecomManager) getApplicationContext().getSystemService(TELECOM_SERVICE)).
            getDefaultDialerPackage().equals(this.getPackageName())) return;

        Intent i = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).
            putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                this.getPackageName());
        startActivityForResult(i, 123);
    }

    private void checkDefaultHome() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        final Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);

        final ResolveInfo ri = getPackageManager().resolveActivity(i, 0);
        if ((ri.activityInfo != null &&
            getPackageName().equals(ri.activityInfo.packageName))) return;
        startActivityForResult(new Intent(Settings.ACTION_HOME_SETTINGS), 123);
    }

    private void checkDefaultSMS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (Telephony.Sms.getDefaultSmsPackage(this).equals(this.getPackageName())) return;

        Intent i = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT).
            putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, this.getPackageName());
        startActivityForResult(i, 123);
    }

    private void checkSetDefaultResult(int resultCode) {
        String message = resultCode != RESULT_OK
            ? "El usuario acepto establecer la aplicación para uso predeterminado."
            : "El usuario no acepto establecer la aplicación para uso predeterminado.";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private final void offerReplacingDefaultDialer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        final TelecomManager tm = this.getSystemService(TelecomManager.class);
        if (this.getPackageName().equals(tm.getDefaultDialerPackage())) {
            final Intent i = new Intent("android.telecom.action.CHANGE_DEFAULT_DIALER").
                putExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME",
                        this.getPackageName());
            this.startActivity(i);
        }
    }

    private final void offerReplacingDefaultHome() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo ri = getPackageManager().resolveActivity(i, 0);
        if ((ri.activityInfo != null && getPackageName().equals(ri.activityInfo.packageName))) {
            i = new Intent("android.provider.Settings.ACTION_HOME_SETTINGS").
                putExtra("android.Telephony.Sms.Intents.EXTRA_PACKAGE_NAME",
                        this.getPackageName());
            this.startActivity(i);
        }
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

    public void makeCall(String codedNumber) {
        if (PermissionChecker.checkSelfPermission(this,
                "android.permission.CALL_PHONE") == REQUEST_PERMISSION) {
            String number = Utils.decodeBase64(codedNumber);
            Uri uri = Uri.parse("tel:" + number.trim());
            this.startActivity(new Intent(Intent.ACTION_CALL, uri));
        } else {
            ActivityCompat.requestPermissions( (Activity)this
                , new String[]{"android.permission.CALL_PHONE"}
                , 0);
        }
    }

    private void showSelectedFragment(Fragment fragment) {
        getSupportFragmentManager().
            beginTransaction().
            replace(R.id.container, fragment).
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).
            commit();
    }

    //region private methods declaration
    protected void checkPermissions() {
        final List<String> missingPermissions  = new ArrayList<String>();

        // Check all required dynamic permissions.
        for (final String permission : Constants.APP_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) missingPermissions.add(permission);
        }

        if (!missingPermissions.isEmpty()) {
            // Request all missing permissions.
            final String[] permissions = missingPermissions.toArray(new
                    String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this
                , permissions
                , REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[Constants.APP_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS
                , Constants.APP_PERMISSIONS
                , grantResults);
        }
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) finishAffinity();
        else finish();
    }

    private void initializeReceivers() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("CELLCOMM_MESSAGE_CONTACTMGMT");
        registerReceiver( this._cntctreceiver, filter );

        filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver( this._batteryreceiver, filter );

        filter = new IntentFilter();
        filter.addAction("CELLCOMM_MESSAGE_STOPLOCK");
        registerReceiver( this._alarmreceiver, filter );

        this._phone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        this._phone.listen(phoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private void enableStayOnWhilePluggedIn(Boolean active) {
        String  value = active ? Integer.toString(Constants.BATTERY_FLAGS): "0";
        this._dpm.setGlobalSetting( this._adminName
            , Settings.Global.STAY_ON_WHILE_PLUGGED_IN
            , value);
    }

    private void setAsHomeApp(Boolean enable) {
        if (enable) {
            IntentFilter i = new IntentFilter(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.addCategory(Intent.CATEGORY_HOME);

            this._dpm.addPersistentPreferredActivity ( ContactView.this._adminName
                , i
                , new ComponentName(getPackageName(), ContactView.class.getName()) );
        } else {
            ContactView.this._dpm.clearPackagePersistentPreferredActivities (
                    ContactView.this._adminName, getPackageName() );
        }
    }

    private void setBatteryLevel(Float level) {
        BatteryLevelControl blc = findViewById(R.id.headerView).
            findViewById(R.id.imageBatteryLevel);
        blc.setLevel(level);
    }

    private void setImmersiveMode(Boolean enable) {
        int flags = enable ? Constants.UI_FLAGS_IMMERSE_DISABLE
                : Constants.UI_FLAGS_IMMERSE_ENABLE;
        this.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    private void setKeyGuardEnabled(Boolean enable) {
        this._dpm.setKeyguardDisabled(this._adminName, !enable);
    }

    private void setLockTask(Boolean start, Boolean isAdministrator) {
        if (isAdministrator) {
            this._dpm.setLockTaskPackages(this._adminName
                , new String[] { getPackageName()
                    , "com.google.android.dialer"
                    , "com.android.server.telecom"
                });
        }
        if (start) this.startLockTask();
        else this.stopLockTask();
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

    private void setRestrictions(Boolean disallow) {
        this.setUserRestriction(UserManager.DISALLOW_ADD_USER, disallow);
        this.setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, disallow);
        this.setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, disallow);
        this.setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, disallow);
        this.setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, disallow);
    }

    private void setSignalLevel(int level) {
        SignalLevelControl slc = findViewById(R.id.headerView).
                findViewById(R.id.imageSignalLevel);
        slc.setLevel(level);
    }

    private void setUpdatePolicy(Boolean enable) {
        SystemUpdatePolicy policy =  enable
                ? SystemUpdatePolicy.createWindowedInstallPolicy(60, 120): null;
        this._dpm.setSystemUpdatePolicy(this._adminName, policy);
    }

    private void setUserRestriction(String restriction, Boolean disallow) {
        if (disallow) this._dpm.addUserRestriction(this._adminName,restriction);
        else this._dpm.clearUserRestriction(this._adminName,restriction);
    }

    private void startLockTaskDelayed () {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // start lock task mode if its not already active
                try {
                    if( ContactView.this.isLocked ) initializePolices();
                } catch(IllegalArgumentException e) {
                    Log.e(this.TAG, "Was not in foreground yet, try again.");
                    startLockTaskDelayed();
                }
            }
            final String TAG = "ContactView.StartLockTaskDelayed";
        }, 1);
    }

    // region fields declarations
    private BroadcastReceiver _cntctreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ( intent.getAction().equals("CELLCOMM_MESSAGE_CONTACTMGMT") ) {
                try {
                    Bundle bundle = intent.getExtras();
                    SMSEntity sms = (SMSEntity) intent.getSerializableExtra("data");
                    String action = bundle.getString("action");

                    _fragments.stream().forEach(f -> ((IFragment) f).applyChanges(action, sms));
                } catch (Exception e) {
                    Log.e(this.TAG, "error : " + e.getMessage());
                }
            }
        }
        final String TAG = "ContactView.SMSReceiver.onReceive";
    };

    private BroadcastReceiver _alarmreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ( intent.getAction().equals("CELLCOMM_MESSAGE_ALARM") ) {
                ContactView.this.startLockTaskDelayed();
            }
        }
    };

    private BroadcastReceiver _batteryreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPresent = intent.getBooleanExtra("present", false);
            Integer rawLevel = intent.getIntExtra("level", -1);
            Integer scale = intent.getIntExtra("scale", -1);

            if (isPresent) {
                Float level = rawLevel >= 0 && scale > 0
                    ? (float)((rawLevel * 100) / scale): 0f;
                setBatteryLevel(level);
            } else {
                Log.i(this.TAG,"Bateria no presente.");
            }
        }
        final String TAG = "BatteryReceiver.onReceive";
    };

    private final PhoneStateListener phoneListener = new  PhoneStateListener(){
        public void onSignalStrengthsChanged (SignalStrength signalStrength) {
            setSignalLevel(signalStrength.getLevel());
            super.onSignalStrengthsChanged(signalStrength);
            Log.d(TAG, String.valueOf(signalStrength.getLevel()));
        }
        final String TAG = "PhoneStateListener.onSignalStrengthsChanged";
    };


    //Permissions that need to be explicitly requested from end user.
    List<Fragment> _fragments = Arrays.asList( new Fragment[] {
        new ContactsFragment(), new MessageFragment(), new SecurityFragment() });

    // Permissions request code.
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private final static int REQUEST_PERMISSION = 0;

    private final String TAG = "ContactView";

    boolean isLocked = true, _isAdmin = true;

    DevicePolicyManager _dpm = null;
    ComponentName _adminName = null;
    TelephonyManager _phone  = null;
    // endregion
}
