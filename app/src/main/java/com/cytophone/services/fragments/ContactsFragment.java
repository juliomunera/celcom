package com.cytophone.services.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    RecyclerAdapter recyclerAdapter;
    List<String> contactList;
    Activity parentActivity;

    public ContactsFragment(Activity activity) {
        this.parentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        List<PartyEntity> list =CytophoneApp.getPersistenceDB().partyDAO().getAllParties();

        contactList = new ArrayList<>();
        for (PartyEntity data : list) {
            contactList.add(data.getName());
        }

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(contactList, this.parentActivity);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

}
