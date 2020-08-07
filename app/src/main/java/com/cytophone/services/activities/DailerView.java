package com.cytophone.services.activities;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.collections.ArraysKt;
import com.cytophone.services.R;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.Context;
import android.content.Intent;

import android.telecom.TelecomManager;

import android.provider.Settings;
import android.provider.Telephony;

import android.view.KeyEvent;
import android.app.Activity;

import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.os.Build;
import android.Manifest;

import java.util.ArrayList;
import java.util.Arrays;
import android.net.Uri;
import java.util.List;

class DialerView extends AppCompatActivity {
    @Override
    public void onBackPressed() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailer_view);

        /*
        TextInputEditText tiet = (TextInputEditText) this.findViewById(R.id.tv_name);
        Uri uri = this.getIntent().getData();
        if( null != uri ) {
            tiet.setText(uri.getSchemeSpecificPart());
        }

         */

        this.checkPermissions();

        this.checkDefaultHome();
        this.checkDefaultSMS();
        this.checkDefaultDialer();
    }

    protected void onStart() {
        super.onStart();

        this.offerReplacingDefaultHome();
        this.offerReplacingDefaultSMS();
        this.offerReplacingDefaultDialer();

        /*
        ((TextInputEditText) this.findViewById(R.id.phoneNumberInput)).
                setOnEditorActionListener((TextView.OnEditorActionListener)
                        (new TextView.OnEditorActionListener() {
                            public final boolean onEditorAction(TextView tv, int val, KeyEvent ke) {
                                DialerView.this.makeCall();
                                return true;
                            }
                        })); */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) checkSetDefaultResult(resultCode);
    }

    private void checkDefaultDialer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (!((TelecomManager) getApplicationContext().getSystemService(TELECOM_SERVICE)).
                getDefaultDialerPackage().equals(this.getPackageName())) return;

        Intent i = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).
                putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                        this.getPackageName());
        startActivityForResult(i, 123);
    }

    private void checkDefaultSMS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (Telephony.Sms.getDefaultSmsPackage(this).equals(this.getPackageName())) return;

        Intent i = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT).
                putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, this.getPackageName());
        startActivityForResult(i, 123);
    }

    private void checkDefaultHome() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        final Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo ri = getPackageManager().resolveActivity(i, 0);
        if ((ri.activityInfo != null && getPackageName().equals(ri.activityInfo.packageName)))
            return;

        startActivityForResult(new Intent(Settings.ACTION_HOME_SETTINGS), 123);
    }

    private void checkSetDefaultResult(int resultCode) {
        String message = resultCode == RESULT_CANCELED ?
                "User accepted request to become default app" :
                resultCode != RESULT_OK ?
                        "User accepted request to become default app" :
                        "User don't accepted request to become default app";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private final void makeCall() {
        if (PermissionChecker.checkSelfPermission((Context) this,
                "android.permission.CALL_PHONE") == 0) {
            /*
            TextInputEditText tiet = (TextInputEditText) this.findViewById(R.id.phoneNumberInput);
            Uri uri = Uri.parse("tel:" + tiet.getText().toString());
            this.startActivity(new Intent("android.intent.action.CALL", uri)); */
        } else {
            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{"android.permission.CALL_PHONE"},
                    0);
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION && ArraysKt.contains(grantResults, 0)) {
            this.makeCall();
        }

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (int i = permissions.length - 1; i >= 0; --i) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // Exit the app if one permission is not granted.
                    Toast.makeText(this, "Required permission '" +
                                    permissions[i] + "' not granted, exiting.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    private final void offerReplacingDefaultDialer() {
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

    // Permissions that need to be explicitly requested from end user.
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS,
    };

    // Permissions request code.
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    public static final int REQUEST_PERMISSION = 0;
}