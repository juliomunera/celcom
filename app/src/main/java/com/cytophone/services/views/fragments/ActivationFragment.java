package com.cytophone.services.views.fragments;

import com.cytophone.services.entities.IEntityBase;
import com.cytophone.services.entities.SMSEntity;
import com.cytophone.services.utilities.Utils;
import com.cytophone.services.R;

import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;

import android.content.pm.PackageManager;
import android.content.Context;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.view.View;

import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import android.os.Bundle;
import android.os.Build;

import android.Manifest;

public class ActivationFragment extends Fragment implements IFragment {
    // region events methods declarations
    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activation, container, false);

        if (container != null && view != null) {
            if (container.getContext() != null) {
                initializeControls(view);
            }
        }
        return view;
    }

    // region public methods
    @Override
    public void applyChanges(String action, IEntityBase message)  {}

    @Override
    public int getID() {
        return 0;
    }
    // endregion

    @SuppressLint("MissingPermission")
    public String getDeviceIMEI() {
        TelephonyManager tm = (TelephonyManager) ActivationFragment.this.getContext().
                getSystemService(ActivationFragment.this.getContext().TELEPHONY_SERVICE);
        String imei = "";

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (tm.getPhoneCount() == 2) { imei = tm.getImei(0); }
                    else { imei = tm.getImei(); }
                } else {
                    if (tm.getPhoneCount() == 2) { imei = tm.getDeviceId(0); }
                    else { imei = tm.getDeviceId(); }
                }
            }
            else { imei = tm.getDeviceId(); }
        }
        return imei;
    }

    public void setEnable(boolean enabled) {}
    // endregion

    // region private methods
    private void add(SMSEntity message) throws Exception {}

    private void initializeControls(View view) {
        EditText edt1 = (EditText) view.findViewById(R.id.edtCountryCode);
        EditText edt2 = (EditText) view.findViewById(R.id.edtPhone);

        Button btn = (Button) view.findViewById(R.id.btnActivationRequest);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt1 != null && edt2 != null) {
                    String countryCode = edt1.getText().toString();
                    String phoneNo = edt2.getText().toString();

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService
                            (Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt1.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(edt2.getWindowToken(), 0);

                    sendSMS(phoneNo, countryCode);
                } else  {
                    Toast t = Toast.makeText(ActivationFragment.this.getContext()
                            ,"Debe ingresar el código del país y el número móvil autorizado."
                            , Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP,0,0);
                    t.show();
                }
            }
        });
    }

    private void remove(SMSEntity message) throws Exception {}

    private void sendSMS(String phoneNumber, String countryCode) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String smsMsg = "0|" + countryCode + '|'  + getDeviceIMEI();
            smsMsg = Utils.encodeBase64(smsMsg);
            smsMsg = Utils.convertStringToHex(smsMsg);

            smsManager.sendTextMessage(phoneNumber
                    , null, smsMsg
                    , null
                    , null);

            Toast t = Toast.makeText(ActivationFragment.this.getContext()
                    , "El mensaje con la solicitud fue enviado."
                    , Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP,0,0);
            t.show();
        } catch (Exception ex) {
            Toast  t = Toast.makeText(ActivationFragment.this.getContext()
                    ,"Ocurrío un error enviado el mensaje con la solicitud. Comuníquese " +
                      "con el equipo de soporte."
                    , Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP,0,0);
            t.show();
            ex.printStackTrace();
        }
    }
    // endregion

    // region fields declarations
    final String TAG = "ActivationFragment";

    Context _context = null;
    // endregion
}
