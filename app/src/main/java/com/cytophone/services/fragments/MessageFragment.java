package com.cytophone.services.fragments;

import com.cytophone.services.activities.adapters.MessageAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import com.cytophone.services.entities.EventEntity;
import com.cytophone.services.entities.IEntityBase;
import com.cytophone.services.entities.SMSEntity;
import com.cytophone.services.CytophoneApp;
import com.cytophone.services.R;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MessageFragment extends Fragment implements IFragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        RecyclerView rvw = (RecyclerView) view.findViewById(R.id.recyclerMessages);

        this._messages = CytophoneApp.getInstanceDB().eventDAO().get20LastMessages();
        this._adapter = new MessageAdapter(this._messages);

        rvw.setAdapter(this._adapter);
        rvw.setLayoutManager(new LinearLayoutManager(container.getContext()));
        rvw.addItemDecoration(new DividerItemDecoration(container.getContext(),
                DividerItemDecoration.VERTICAL));
        rvw.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void applyChanges(String action, IEntityBase message)  {
        if( message == null ) return;

        try {
            this._messages.add(((SMSEntity)message).getEventObject());
            this._adapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.e("E/CellComm", "applyChanges -> " + e.getMessage());
        }
    }

    @Override
    public int getID() {
        return R.id.smsmessages;
    }

    // region fields declarations
    List<EventEntity> _messages;
    MessageAdapter _adapter;
    // endregion
}
