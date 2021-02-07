package com.cytophone.services.views.fragments;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import android.os.Bundle;

import com.cytophone.services.entities.IEntityBase;
import com.cytophone.services.R;

public class RegistrationFragment extends Fragment implements IFragment {
    // region events methods declarations
    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        if (container != null && view != null) {
            if (container.getContext() != null) {
                _context = container.getContext();
                initializeControls(view);
            }
        }
        return view;
    }

    @Override
    public void applyChanges(String action, IEntityBase message) {}

    @Override
    public int getID() {
        return R.id.contactdevice;
    }

    @Override
    public void setEnable(boolean enabled) {}
    // endregion

    // region public methods
    private void initializeControls(View view) {
        Button btnOK = (Button) view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imei = getIMEINumber();
                sendSMS( "",imei);
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
    // endregion

    // region private methods
    private void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,
                    null, msg,
                    null,
                    null);
            Toast.makeText(_context, "Message Sent",Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(_context,ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public String getIMEINumber()
            throws SecurityException, NullPointerException {
        TelephonyManager tm = (TelephonyManager) _context.getSystemService(
                Context.TELEPHONY_SERVICE);
        String imei;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert tm != null;
            imei = tm.getImei();
            //this change is for Android 10 as per security concern it will not provide
            // the imei number.
            if (imei == null) {
                imei = Settings.Secure.getString(_context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        } else {
            assert tm != null;
            if (tm.getDeviceId() != null && !tm.getDeviceId().equals("000000000000000")) {
                imei = tm.getDeviceId();
            } else {
                imei = Settings.Secure.getString(_context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        return imei;
    }
    // endregion

    // region fields declarations
    final String TAG = "RegistrationFragment";

    Context _context = null;
    // endregion
}
