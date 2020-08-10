package com.cytophone.services.fragments;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import com.cytophone.services.activities.adapters.RecyclerAdapter;
import com.cytophone.services.utilities.ItemSelectListener;
import com.cytophone.services.activities.ContactListitem;
import com.cytophone.services.entities.PartyEntity;
import com.cytophone.services.entities.SMSEntity;
import com.cytophone.services.CytophoneApp;
import com.cytophone.services.R;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

public class ContactsFragment extends Fragment {
    // region events methods declarations
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView rvw = (RecyclerView)view.findViewById(R.id.recyclerView);

        _parties = CytophoneApp.getInstanceDB().partyDAO().getAllOrderSuscribers();

        _recyclerAdapter = new RecyclerAdapter(this._parties);
        _recyclerAdapter.setListener(new ItemSelectListener<PartyEntity>() {
            @Override
            public void onSelect(PartyEntity item) {
                ((ContactListitem)_parentActivity).makeCall(item.getNumber());
            }
        });
        rvw.setAdapter(this._recyclerAdapter);
        rvw.setLayoutManager( new LinearLayoutManager(container.getContext(),
                              LinearLayoutManager.VERTICAL,
                        false));
        rvw.addItemDecoration(new DividerItemDecoration(container.getContext(),
                                    DividerItemDecoration.VERTICAL));
        rvw.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
    // endregion

    //region constructors methods
    public ContactsFragment (Activity activity){
        _parentActivity = activity;
    }
    //end region

    // region public methods
    private void add(SMSEntity message) throws Exception {
        PartyEntity party = message.getPartyObject();
        _parties.add(party);
    }

    public void applyChanges(String action, SMSEntity message) throws Exception {
        if( message == null ) return;

        if ( action.contains("delete") )  this.remove(message);
        else if ( action.contains("update") ) this.update(message);
        else if ( action.contains("insert") ) this.add(message);

        this._recyclerAdapter.notifyDataSetChanged();
    };

    private void remove(SMSEntity message) throws Exception {
        PartyEntity party = message.getPartyObject();
        _parties.removeIf(p ->  p.getNumber().equals(party.getNumber()) &&
                                p.getPlaceID().equals(party.getPlaceID()));
    }

    private void update(SMSEntity message) throws Exception {
        this.remove(message);
        this.add(message);
    }
    // endregion

    // region fields declarations
    RecyclerAdapter _recyclerAdapter;
    Activity _parentActivity = null;
    List<PartyEntity> _parties;
    // endregion
}
