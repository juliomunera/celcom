package com.cytophone.services.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cytophone.services.R;
import com.cytophone.services.activities.adapters.RecyclerMessageAdapter;

public class MessageFragment extends Fragment {


    String s1[] = { "Mensaje No. 1 ingreso", "Mensaje dos actualización",
                    "Mensaje eliminación", "Un nuevo mensaje de ingreso",
                    "Ultimo mensaje de ingreso al sistema"},

            s2[] = { "2020-06-24 12:35", "2020-06-23 08:21",
                     "2020-06-20 16:50", "2020-06-20 13:25",
                    "2020-06-20 13:24"};

    public MessageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        this._recyclerView = (RecyclerView)view.findViewById(R.id.recyclerMessages);

        RecyclerMessageAdapter adapter = new RecyclerMessageAdapter(container.getContext(),
                s1,
                s2,
                _images);
        _recyclerView.setAdapter(adapter);
        _recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        _recyclerView.addItemDecoration(
                new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    RecyclerView _recyclerView;
    int _images[] = {
            R.drawable.ic_playlist_add_black_24dp,
            R.drawable.ic_swap_horiz_black_24dp,
            R.drawable.ic_clear_black_24dp,
            R.drawable.ic_playlist_add_black_24dp,
            R.drawable.ic_playlist_add_black_24dp
    };
}
