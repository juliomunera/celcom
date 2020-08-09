package com.cytophone.services.activities.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cytophone.services.CytophoneApp;
import com.cytophone.services.R;
import com.cytophone.services.activities.ContactListitem;
import com.cytophone.services.entities.PartyEntity;
import com.cytophone.services.fragments.ContactsFragment;
import com.cytophone.services.utilities.ItemSelectListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements Filterable {

    public RecyclerAdapter(List<PartyEntity> parties) {
        this._contactListAll.addAll(parties);
        this._contactList = parties;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder._tvRowCount.setText(String.format("Contacto %s", String.valueOf(position + 1)));
        holder._tvNumber.setText(_contactList.get(position).getName());
        holder._tvName.setText(_contactList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return _contactList.size();
    }

    @Override
    public Filter getFilter() {
        return _filter;
    }

    Filter _filter = new Filter() {
        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<PartyEntity> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(_contactListAll);
            } else {
                _contactListAll.stream().forEachOrdered(n -> {
                    if (n.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(n);
                    }
                });
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            RecyclerAdapter.this._contactList.clear();
            RecyclerAdapter.this._contactList.addAll (
                (Collection<? extends PartyEntity>) filterResults.values
            );
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            _tvRowCount = itemView.findViewById(R.id.rowCountTextView);
            _tvNumber = itemView.findViewById(R.id.tv_number);
            _ivItem = itemView.findViewById(R.id.imageView);
            _tvName = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            PartyEntity party = _contactList.get(getAdapterPosition());
            Toast.makeText(view.getContext(), party.getName(), Toast.LENGTH_SHORT).show();

            if( _listener != null) _listener.onSelect(party);
        }

        TextView _tvRowCount;
        TextView _tvNumber;
        TextView _tvName;

        ImageView _ivItem;
    }

    public void setListener(ItemSelectListener<PartyEntity> listener) {
        _listener = listener;
    }

    //region fields declaration
    private List<PartyEntity> _contactListAll  = new ArrayList<>();
    private ItemSelectListener<PartyEntity> _listener;
    private List<PartyEntity> _contactList;
    //endregion
}