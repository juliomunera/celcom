package com.cytophone.services.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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
import com.cytophone.services.activities.ContactListitem;
import com.cytophone.services.activities.adapters.RecyclerMessageAdapter;
import com.cytophone.services.dao.EventDAO;
import com.cytophone.services.entities.EventEntity;
import com.cytophone.services.entities.PartyEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MessageFragment extends Fragment {

    public MessageFragment() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        this._recyclerView = (RecyclerView)view.findViewById(R.id.recyclerMessages);

        _messages = CytophoneApp.getInstanceDB().eventDAO().get20LastMessages();

        initializeBroadcaster(view);

        RecyclerMessageAdapter adapter = new RecyclerMessageAdapter(container.getContext(), _messages);
        _recyclerView.setAdapter(adapter);
        _recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        _recyclerView.addItemDecoration(
                new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    RecyclerView _recyclerView;

    private void initializeBroadcaster(View view) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SUSCRIBER_EVENTS");
        view.getContext().registerReceiver(_receiver, intentFilter);
    }

    private BroadcastReceiver _receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ( intent.getAction().equals("SUSCRIBER_EVENTS") ) {
                Bundle bundle = intent.getExtras();
                String action = bundle.getString("action");
                PartyEntity party = (PartyEntity)intent.getSerializableExtra("suscriber") ;

            }
        }
    };

    List<EventEntity> _messages;
}
