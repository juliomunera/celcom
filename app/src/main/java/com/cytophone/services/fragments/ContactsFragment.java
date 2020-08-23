package com.cytophone.services.fragments;

import com.cytophone.services.activities.adapters.ContactAdapter;
import com.cytophone.services.utilities.ItemSelectListener;
import com.cytophone.services.activities.ContactListitem;
import com.cytophone.services.CellCommApp;
import com.cytophone.services.entities.*;
import com.cytophone.services.R;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.os.Bundle;
import android.util.Log;
import java.util.List;

public class ContactsFragment extends Fragment implements IFragment {
    // region events methods declarations
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView rvw = (RecyclerView)view.findViewById(R.id.recyclerView);

        this._parties = CellCommApp.getInstanceDB().partyDAO().getAllOrderSuscribers();
        this._adapter = new ContactAdapter(this._parties);
        this._adapter.setListener(new ItemSelectListener<PartyEntity>() {
            @Override
            public void onSelect(PartyEntity item) {
                ((ContactListitem)ContactsFragment.this.getActivity()).
                        makeCall(item.getCodedNumber());
            }
        });
        rvw.setAdapter(this._adapter);
        rvw.setLayoutManager( new LinearLayoutManager(container.getContext(),
                              LinearLayoutManager.VERTICAL,
                        false));
        rvw.addItemDecoration(new DividerItemDecoration(container.getContext(),
                                    DividerItemDecoration.VERTICAL));
        rvw.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void applyChanges(String action, IEntityBase message)  {
        if( message == null ) return;

        try {
            Log.e(this.TAG + ".applyChanges", "action: " + action);

            if ( action.contains("delete") )  this.remove((SMSEntity)message);
            else if ( action.contains("insert") ) this.add((SMSEntity)message);
            this._adapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.e(this.TAG + ".applyChanges", "error: " + e.getMessage());
        }
    }

    @Override
    public int getID() {
        return R.id.contactdevice;
    }
    // endregion


    // region public methods
    private void add(SMSEntity message) throws Exception {
        PartyEntity party = message.getPartyObject();
        this._parties.add(party);
    }

    private void remove(SMSEntity message) throws Exception {
        PartyEntity party = message.getPartyObject();
        this._parties.removeIf(p -> {
            return p.getNumber().equals(party.getNumber());
        });
    }
    // endregion

    // region fields declarations
    final String TAG = "ContactsFragment";
    List<PartyEntity> _parties;
    ContactAdapter _adapter;
    // endregion
}
