package com.cytophone.services.views.adapters;

import com.cytophone.services.utilities.ItemSelectListener;
import com.cytophone.services.entities.PartyEntity;
import com.cytophone.services.R;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Toast;

import java.util.stream.Collectors;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
        implements Filterable {
    public ContactAdapter(List<PartyEntity> parties) {
        this._allContacts = new ArrayList<PartyEntity>();
        this._allContacts.addAll(parties);
        this._contacts = parties;

        this._filter = new CustomFilter();
    }

    @Override
    public int getItemCount() {
        return this._contacts.size();
    }

    @Override
    public Filter getFilter() {
        return this._filter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder._number.setText(this._contacts.get(position).getCodedNumber());
        holder._placeID.setText(this._contacts.get(position).getPlaceID());
        holder._name.setText(this._contacts.get(position).getName());
    }

    public void setListener(ItemSelectListener<PartyEntity> listener) {
        this._listener = listener;
    }

    public class CustomFilter extends Filter {
        private CustomFilter() {
            super();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();
            List<PartyEntity> filteredList = new ArrayList<>();

            if ( constraint == null ||  constraint.length() == 0) {
                filteredList.addAll(ContactAdapter.this._allContacts);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                filteredList = ContactAdapter.this._allContacts.stream().
                        filter(
                                c-> c.getName().toLowerCase().contains(filterPattern)
                        ).collect(Collectors.toList());
            }

            results.count = filteredList.size();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ContactAdapter.this._contacts.clear();
            ContactAdapter.this._contacts.addAll((Collection<PartyEntity>) results.values);
            ContactAdapter.this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            _placeID = itemView.findViewById(R.id.rowCountTextView);
            _number = itemView.findViewById(R.id.tv_number);
            _item = itemView.findViewById(R.id.imageView);
            _name = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            PartyEntity party = ContactAdapter.this._contacts.get(getAdapterPosition());
            Toast.makeText(view.getContext(), party.getName(), Toast.LENGTH_SHORT).show();

            if( null != ContactAdapter.this._listener ) {
                ContactAdapter.this._listener.onSelect(party);
            }
        }

        TextView _placeID, _number, _name;
        ImageView _item;
    }

    //region fields declaration
    private ItemSelectListener<PartyEntity> _listener;
    private List<PartyEntity> _allContacts, _contacts;
    private CustomFilter _filter;
    //endregion
}