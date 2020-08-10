package com.cytophone.services.fragments;

import com.cytophone.services.activities.adapters.RecyclerMessageAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.cytophone.services.entities.EventEntity;
import com.cytophone.services.entities.SMSEntity;
import com.cytophone.services.CytophoneApp;
import com.cytophone.services.R;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.os.Build;
import android.os.Bundle;

import java.util.List;

public class MessageFragment extends Fragment {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        _recyclerView = (RecyclerView) view.findViewById(R.id.recyclerMessages);

        _messages = CytophoneApp.getInstanceDB().eventDAO().get20LastMessages();

        _recyclerAdapter = new RecyclerMessageAdapter(container.getContext(), _messages);
        _recyclerView.setAdapter(_recyclerAdapter);
        _recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        _recyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(),
                DividerItemDecoration.VERTICAL));
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    public void applyChanges(String action, SMSEntity message) throws Exception {
        if( message == null ) return;

        _messages.add(message.getEventObject());
        _recyclerAdapter.notifyDataSetChanged();
    };

    // region fields declarations
    RecyclerMessageAdapter _recyclerAdapter;
    List<EventEntity> _messages;
    RecyclerView _recyclerView;
    // endregion
}
