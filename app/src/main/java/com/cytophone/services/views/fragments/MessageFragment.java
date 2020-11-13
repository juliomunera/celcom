package com.cytophone.services.views.fragments;

import com.cytophone.services.views.adapters.MessageAdapter;
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

public class MessageFragment extends Fragment implements IFragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        if (container != null && view != null) {
            if (container.getContext() != null) {
                this._messages = CellCommApp.getInstanceDB().eventDAO().get20LastMessages();
                if( this._messages != null) {
                    this._adapter = new MessageAdapter(this._messages);
                    if (this._adapter != null) {
                        RecyclerView rvw = (RecyclerView) view.findViewById(R.id.recyclerMessages);
                        rvw.setAdapter(this._adapter);
                        rvw.setLayoutManager(new LinearLayoutManager(container.getContext()));
                        rvw.addItemDecoration(new DividerItemDecoration(container.getContext(),
                                DividerItemDecoration.VERTICAL));
                        rvw.setItemAnimator(new DefaultItemAnimator());
                    }
                }
            }
        }
        return view;
    }

    @Override
    public void applyChanges(String action, IEntityBase message)  {
        if ( null == message ) return;

        try {
            Log.e(this.TAG + ".applyChanges","action: " + action);

            this._messages.add(((SMSEntity)message).getEventObject());
            this._adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(this.TAG + ".applyChanges","error: " + e.getMessage());
        }
    }

    @Override
    public int getID() {
        return R.id.smsmessages;
    }

    public void setEnable(boolean enabled)
    {};

    // region fields declarations
    final String TAG = "MessageFragment";
    List<EventEntity> _messages;
    MessageAdapter _adapter;
    // endregion
}
