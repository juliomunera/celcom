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

import java.util.ArrayList;
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
                if( this._messages == null) this._messages = new ArrayList<>();

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
        return view;
    }

    @Override
    public void applyChanges(String action, IEntityBase message)  {
        if ( null == message ) return;

        try {
            Log.d(this.TAG + ".applyChanges","action -> " + action);
            if( null != this._messages && null != this._adapter ) {
                EventEntity event = ((SMSEntity) message).getEventObject();
                this._messages.add(event);
                this._adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(this.TAG + ".applyChanges", e.getMessage());
        }
    }

    @Override
    public int getID() {
        return R.id.smsmessages;
    }

    public void setEnable(boolean enabled)
    {};

    // region fields declarations
    private final String TAG = "MessageFragment";
    private List<EventEntity> _messages;
    private MessageAdapter _adapter;
    // endregion
}
