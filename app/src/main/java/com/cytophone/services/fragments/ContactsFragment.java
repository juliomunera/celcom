package com.cytophone.services.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cytophone.services.CytophoneApp;
import com.cytophone.services.R;
import com.cytophone.services.activities.adapters.RecyclerAdapter;
import com.cytophone.services.entities.PartyEntity;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    public ContactsFragment(Activity activity) {
        this._parentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView rvw = (RecyclerView)view.findViewById(R.id.recyclerView);
        List<PartyEntity> list = CytophoneApp.getPersistenceDB().partyDAO().getAllParties();

        this._contactList = new ArrayList<>();
        for (PartyEntity data : list) {
            this._contactList.add(data.getName());
        }

        this._recyclerAdapter = new RecyclerAdapter(this._contactList, this._parentActivity);
        rvw.setAdapter(this._recyclerAdapter);
        rvw.setLayoutManager( new LinearLayoutManager(container.getContext(),
                                    LinearLayoutManager.VERTICAL,
                        false));
        rvw.addItemDecoration(new DividerItemDecoration(container.getContext(),
                                    DividerItemDecoration.VERTICAL));
        rvw.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    public void manageContact(String action, String name) {
        try {
            if( action.contains("delete") ) {
                this._contactList.removeIf(n -> n.equals(name));
            } else if ( action.contains("update") ){
                this._contactList.removeIf(n -> n.equals(name));
                this._contactList.add(name);
            } else if ( action.contains("insert") ){
                this._contactList.add(name);
            }
            this._recyclerAdapter.notifyDataSetChanged();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    };

    //region fields declarations
    RecyclerAdapter _recyclerAdapter;
    List<String> _contactList;
    Activity _parentActivity;
    //end region
}
