package com.cytophone.services.fragments;

import com.cytophone.services.entities.UnlockCodeEntity;
import com.cytophone.services.entities.IEntityBase;
import com.cytophone.services.CellCommApp;
import com.cytophone.services.R;

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

        Button btn = (Button) view.findViewById(R.id.btnOK);
        btn.setOnClickListener( _blocklistener );

        btn = (Button) view.findViewById(R.id.btnCancel);
        btn.setOnClickListener( _unblocklistener );
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

    private UnlockCodeEntity searchCode(String number) {
        UnlockCodeEntity entity = CellCommApp.getInstanceDB().unlockCodeDAO().
                getUnLockCodeByCode(number);
        return entity != null ? entity : null;
    }

    public void setListener(View.OnClickListener listener) {
        this._listener = listener;
    }

    private void sendMessage(UnlockCodeEntity entity) {
        Intent intent = new Intent("CELLCOM_MESSAGE_UNLOCK");
        intent.putExtra( "data", entity );
        this.getView().getContext().sendBroadcast(intent);
    }

    private View.OnClickListener _blocklistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String message = "El desbloqueo no pudo efectuarse.";
            String number = SecurityFragment.this.getCode();

            if (number.length() == 1) return;

            UnlockCodeEntity entity = searchCode(number);
            if (entity != null) {
                if( entity.getEndDate().after(new Date(System.currentTimeMillis())) ) {
                    SecurityFragment.this.sendMessage(entity);
                    SecurityFragment.this.getActivity().stopLockTask();
                    message = "Desbloqueo activado.";
                }
            }
            Toast.makeText(SecurityFragment.this.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener _unblocklistener = new View.OnClickListener() {
        public void onClick(View v) {
            SecurityFragment.this.getActivity().startLockTask();
            Toast.makeText(SecurityFragment.this.getContext(),
                    "Bloqueo activado",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener _listener;
}
