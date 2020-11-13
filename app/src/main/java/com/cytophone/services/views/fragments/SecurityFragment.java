package com.cytophone.services.views.fragments;

import com.cytophone.services.CleanerCallLog;
import com.cytophone.services.LockerDevice;
import com.cytophone.services.CellCommApp;
import com.cytophone.services.entities.*;
import com.cytophone.services.R;
import com.cytophone.services.utilities.Utils;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.content.Intent;

import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import android.os.Bundle;

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

    private UnlockCodeEntity searchCode(String number) {
        UnlockCodeEntity entity = CellCommApp.getInstanceDB().unlockCodeDAO().
                getUnLockCodeByCode(number);
        return entity != null ? entity : null;
    }

    public void setListener(View.OnClickListener listener) {
        this._listener = listener;
    }

    private void sendMessage(UnlockCodeEntity entity) {
        Intent i = new Intent("CELLCOMM_MESSAGE_UNLOCK");
        i.putExtra( "data", entity );
        this.getView().getContext().sendBroadcast(i);
    }

    private void schedulerLockDevice(Date start, Date end) {
        LockerDevice locker = new LockerDevice(this.getContext()
                , (int) (end.getTime() - start.getTime()) / 1000);
    }

    private void schedulerDeleteCallLog(int seconds) {
        Date start = Utils.getCurrentTime("EST");
        Date end = Utils.addSeconds( start,seconds);

        CleanerCallLog cleaner = new CleanerCallLog(getContext()
                , (int) (end.getTime() - start.getTime()) / 1000);
    }

    private View.OnClickListener _blocklistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String msg = "El desbloqueo no pudo efectuarse.";
            String number = SecurityFragment.this.getCode();

            if (number.length() == 1) return;

            UnlockCodeEntity entity = searchCode(number);
            if (entity != null) {
                if (entity.getEndDate().after(new Date(System.currentTimeMillis()))) {
                    SecurityFragment.this.schedulerLockDevice(entity.getCreatedDate(),
                            entity.getEndDate());
                    SecurityFragment.this.schedulerDeleteCallLog(10);
                    SecurityFragment.this.getActivity().stopLockTask();
                    msg = "Desbloqueo activado.";
                }
            }

            Toast.makeText(SecurityFragment.this.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener _unblocklistener = new View.OnClickListener() {
        public void onClick(View v) {
            SecurityFragment.this.getActivity().startLockTask();
            Toast.makeText(SecurityFragment.this.getContext()
                    , "Bloqueo activado"
                    , Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener _listener;
}
