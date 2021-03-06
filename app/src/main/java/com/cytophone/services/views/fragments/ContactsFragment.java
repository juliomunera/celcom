package com.cytophone.services.views.fragments;

import com.cytophone.services.views.adapters.ContactAdapter;
import com.cytophone.services.utilities.ItemSelectListener;
import com.cytophone.services.views.ContactView;
import com.cytophone.services.CellCommApp;
import com.cytophone.services.entities.*;
import com.cytophone.services.R;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.Fragment;

import android.text.TextWatcher;
import android.text.Editable;

import android.content.Context;

import android.widget.EditText;
import android.widget.TextView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements IFragment {
    // region events methods declarations
    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        if (container != null && view != null) {
            if (container.getContext() != null) {
                initializeAdapter(view, container);
                initializeSearchControls(view);
            }
        }
        return view;
    }

    @Override
    public void applyChanges(String action, IEntityBase message)  {
        if( message == null ) return;

        try {
            Log.d(this.TAG + ".applyChanges", action);
            if( null != this._adapter && null != this._parties ) {
                if (action.contains("delete")) {
                    this.remove((SMSEntity) message);
                } else if (action.contains("insert")) {
                    this.add((SMSEntity) message);
                }
                this._adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            Log.e(this.TAG + ".applyChanges", e.getMessage());
        }
    }

    @Override
    public int getID() {
        return R.id.contactdevice;
    }

    public void setEnable(boolean enabled) {
        RecyclerView rvw = (RecyclerView) this.getView().findViewById(R.id.recyclerView);
        rvw.setEnabled(enabled);
    }
    // endregion

    // region public methods
    private void add(SMSEntity message) throws Exception {
        this.remove(message);
        this._parties.add(message.getPartyObject());
    }

    private void initializeAdapter(View view, ViewGroup container) {
        this._parties = CellCommApp.getInstanceDB().partyDAO().getAllOrderSuscribers();
        if( this._parties == null )  this._parties = new ArrayList<>();

        this._adapter = new ContactAdapter(this._parties);
        if (this._adapter != null) {
            this._adapter.setListener(new ItemSelectListener<PartyEntity>() {
                @Override
                public void onSelect(PartyEntity item) {
                    ((ContactView) ContactsFragment.this.getActivity()).
                            makeCall(item.getCodedNumber());
                }
            });

            RecyclerView rvw = (RecyclerView) view.findViewById(R.id.recyclerView);
            rvw.setAdapter(this._adapter);
            rvw.setLayoutManager(new LinearLayoutManager(container.getContext()
                    , LinearLayoutManager.VERTICAL
                    , false));
            rvw.addItemDecoration(new DividerItemDecoration(container.getContext()
                    , DividerItemDecoration.VERTICAL));
            rvw.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private void initializeSearchControls(View view) {
        EditText edt = (EditText) view.findViewById(R.id.edtSearchBox);
        edt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        ? true  // Just ignore the [Enter] key
                        : false; // Handle all other keys in the default way
            }
        });

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        TextView tvw = (TextView) view.findViewById(R.id.tvwCancel);
        tvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt != null) {
                    edt.setText("");
                    hideKB(edt);
                }
            }
        });

        tvw.requestFocus();
        hideKB(edt);
    }

    private void hideKB(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService
                (Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    private void remove(SMSEntity message) throws Exception {
        PartyEntity party = message.getPartyObject();
        this._parties.removeIf(p -> {
            return p.getNumber().equals(party.getNumber());
        });
    }
    // endregion

    // region fields declarations
    private final String TAG = "ContactsFragment";

    private  List<PartyEntity> _parties;
    private ContactAdapter _adapter;
    // endregion
}
