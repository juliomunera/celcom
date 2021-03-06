package com.cytophone.services.views.fragments;

import com.cytophone.services.utilities.Utils;
import com.cytophone.services.CleanerCallLog;
import com.cytophone.services.LockerDevice;
import com.cytophone.services.CellCommApp;
import com.cytophone.services.entities.*;
import com.cytophone.services.R;
import com.cytophone.services.views.ContactView;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.content.Intent;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SecurityFragment extends Fragment implements IFragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_security, container, false);
        ((Button) view.findViewById(R.id.btnCancel)).setOnClickListener( _unblocklistener );
        ((Button) view.findViewById(R.id.btnOK)).setOnClickListener( _blocklistener );
        return view;
    }

    @Override
    public void applyChanges(String action, IEntityBase message)
    {}

    @Override
    public int getID(){
        return R.id.blockoption;
    }

    public void setEnable(boolean enabled)
    {};

    private String getCode() {
        EditText edt = this.getView().findViewById(R.id.edtCode);
        return edt.getText().toString();
    }

    private Date getDate(String dateTime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        } catch (Exception ex) {
            return new Date(System.currentTimeMillis());
        }
    }

    private CodeEntity searchCode(String number) {
        try {
            CodeEntity entity = CellCommApp.getInstanceDB().codeDAO().
                    getLastUnLockCodeByCodeNumber(number);
            return entity != null ? entity : null;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void setListener(View.OnClickListener listener) {
        this._listener = listener;
    }

    private void sendMessage(CodeEntity entity) {
        Intent i = new Intent("CELLCOMM_MESSAGE_UNLOCK");
        i.putExtra( "data", entity );
        this.getView().getContext().sendBroadcast(i);
    }

    private void schedulerLockDevice(Date start, Date end) {
        try {
            LockerDevice locker = new LockerDevice(this.getContext()
                    , (int) (end.getTime() - start.getTime()) / 1000);
        } catch(Exception ex) {
            Log.e( this.TAG + ".schedulerLockDevice", "error ->" + ex.getMessage() );
        }
    }

    private void schedulerDeleteCallLog(int seconds) {
        try {
            Date start = Utils.getCurrentTime("EST");
            Date end = Utils.addSeconds(start, seconds);

            CleanerCallLog cleaner = new CleanerCallLog(this.getContext()
                    , (int) (end.getTime() - start.getTime()) / 1000);
        } catch(Exception ex) {
            Log.e( this.TAG + ".schedulerDeleteCallLog",  ex.getMessage() );
        }
    }

    private View.OnClickListener _blocklistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                String msg = "El desbloqueo no pudo efectuarse.";
                String number = SecurityFragment.this.getCode();

                if (number.length() > 0) {
                    CodeEntity entity = searchCode(number);
                    if (entity != null) {
                        Date d1 = getDate(entity.getCreatedDate());
                        Date d2 = getDate(entity.getEndDate());

                        if (d2.after(new Date(System.currentTimeMillis()))) {
                            SecurityFragment.this.schedulerLockDevice(d1, d2);
                            SecurityFragment.this.schedulerDeleteCallLog(10);

                            // Corrección al defecto al desbloqueo de la APP
                            ContactView a = (ContactView) SecurityFragment.this.getActivity();
                            ((CellCommApp)a.getApplicationContext()).setLockModeEnabled(false);
                            a.stopLockTaskDelayed();
                            msg = "Desbloqueo activado.";
                        }
                    }
                }
                showMessage(SecurityFragment.this.getContext(), msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener _unblocklistener = new View.OnClickListener() {
        public void onClick(View v) {
            ContactView a = (ContactView)SecurityFragment.this.getActivity();
            ((CellCommApp)a.getApplicationContext()).setLockModeEnabled(true);
            a.startLockTaskDelayed();
            showMessage(SecurityFragment.this.getContext(), "Bloqueo activado");
        }
    };

    private void showMessage(Context context, String message) {
        Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        //t.setGravity(Gravity.TOP, 0, 0);
        t.show();
    }

    private final String TAG = "SecurytyFragment";
    private View.OnClickListener _listener;
}
