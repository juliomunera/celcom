package com.cytophone.services.fragments;

import com.cytophone.services.entities.UnlockCodeEntity;
import com.cytophone.services.entities.IEntityBase;
import com.cytophone.services.CytophoneApp;
import com.cytophone.services.R;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.os.Bundle;

public class SecurityFragment extends Fragment implements IFragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_security, container, false);
        Button btn = (Button) view.findViewById(R.id.btnOK);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String number = SecurityFragment.this.getMSISDN();
                if( number.length() == 1 ) return;

                UnlockCodeEntity entity = searchCode(number);
                if( entity != null ) {
                    SecurityFragment.this.sendMessage(entity);
                    SecurityFragment.this.getActivity().stopLockTask();
                } else {
                    Toast.makeText(SecurityFragment.this.getContext(),
                            "No se puede realizar el desbloqueo.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void applyChanges(String action, IEntityBase message)
    {}

    @Override
    public int getID(){
        return R.id.blockoption;
    }

    private String getCode() {
        EditText edt = this.getView().findViewById(R.id.edtCode);
        return edt.getText().toString();
    }

    private String getMSISDN() {
        EditText edt = this.getView().findViewById(R.id.edtMSISDN);
        return edt.getText().toString();
    }

    private UnlockCodeEntity searchCode(String number ) {
        UnlockCodeEntity entity = CytophoneApp.getInstanceDB().unlockCodeDAO().
                getUnLockCodeByMSISDN(number);

        return entity != null && this.getCode().equals(entity.getCode())
              ? entity : null;
    }

    private void sendMessage(UnlockCodeEntity entity) {
        Intent intent = new Intent("CELLCOM_MESSAGE_UNLOCK");
        intent.putExtra( "data", entity );

        this.getView().getContext().sendBroadcast(intent);
    }

    public void setListener(View.OnClickListener listener) {
        this._listener = listener;
    }

    private View.OnClickListener _listener;
}
