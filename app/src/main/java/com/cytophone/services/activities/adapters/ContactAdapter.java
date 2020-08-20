package com.cytophone.services.activities.adapters;

import com.cytophone.services.utilities.ItemSelectListener;
import com.cytophone.services.entities.PartyEntity;
import com.cytophone.services.R;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.View;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public ContactAdapter(List<PartyEntity> parties) {
        this._contacts = parties;
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
        holder._number.setText(this._contacts.get(position).getCodedNumber());
        holder._placeID.setText(this._contacts.get(position).getPlaceID());
        holder._name.setText(this._contacts.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return ContactAdapter.this._contacts.size();
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
            PartyEntity party = _contacts.get(getAdapterPosition());
            Toast.makeText(view.getContext(), party.getName(), Toast.LENGTH_SHORT).show();

            if( ContactAdapter.this._listener != null) {
                ContactAdapter.this._listener.onSelect(party);
            }
        }

        TextView _placeID, _number, _name;
        ImageView _item;
    }

    public void setListener(ItemSelectListener<PartyEntity> listener) {
        this._listener = listener;
    }

    //region fields declaration
    private ItemSelectListener<PartyEntity> _listener;
    private List<PartyEntity> _contacts;
    //endregion
}