package com.cytophone.services.views.adapters;

import com.cytophone.services.entities.EventEntity;
import com.cytophone.services.R;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewMessageHolder> {

    public MessageAdapter(List<EventEntity> messages) {
        this._messages = messages;
    }

    @NonNull
    @Override
    public ViewMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_message_list, parent, false);
        this._context = view.getContext();

        return new ViewMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMessageHolder holder, int position) {
        try{
            String actionType = this._messages.get(position).getAction().toLowerCase();
            String date = this._messages.get(position).getCreatedDate();

            setAction(holder, actionType);
            //setDate(holder, String.format("%tY-%<tm-%<td %<tH:%<tM:%<tS",date));
            setDate(holder, this._messages.get(position).getCreatedDate());
            setHolderIcon(holder, actionType);
        } catch (Exception ex) {
            Log.e(TAG + ".onBindViewHolder", ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return this._messages.size();
    }

    private void setAction(ViewMessageHolder holder, String action) {
        action = action.replace("suscriberinsert", this._context.getString(R.string.suscriberinsert))
                .replace("suscriberdelete", this._context.getString(R.string.suscriberdelete))
                .replace("authorizatorinsert", this._context.getString(R.string.authorizatorinsert))
                .replace("authorizatordelete", this._context.getString(R.string.authorizatordelete))
                .replace("unlockcodeinsert", this._context.getString(R.string.unlock))
                .replace("deactivationcodeinsert", this._context.getString(R.string.deactivationcode))
                .replace("activationcodeinsert", this._context.getString(R.string.activationcode));
        holder._action.setText(action);
    }

    private void setDate(ViewMessageHolder holder, String date) {
        if( date == null ) date = "";
        holder._date.setText(date);
    }

    private void setHolderIcon(ViewMessageHolder holder, String action) {
        if (action.contains(("suscriber"))) {
            if (action.contains(("insert"))) {
                setImage(holder,R.drawable.ic_playlist_add_black_24dp);
            } else if (action.contains(("delete"))) {
                setImage(holder,R.drawable.ic_clear_black_24dp);
            }
        }else if (action.contains(("authorizator"))) {
            if (action.contains(("insert"))) {
                setImage(holder,R.drawable.ic_playlist_add_black_24dp);
            } else if(action.contains(("delete"))) {
                setImage(holder,R.drawable.ic_clear_black_24dp);
            }
        } else if( action.contains(("unlock")) ||
                action.contains(("activation")) ||
                action.contains(("deactivation")) ) {
            setImage(holder,R.drawable.ic_baseline_lock_open_24);
        }
    }

    private void setImage(ViewMessageHolder holder, int resourceID) {
        holder._icon.setImageResource(resourceID);
    }

    public class ViewMessageHolder extends RecyclerView.ViewHolder {
        public ViewMessageHolder(@NonNull View itemView) {
            super(itemView);

            this._action = itemView.findViewById(R.id.myText1);
            this._date = itemView.findViewById(R.id.myText2);
            this._icon = itemView.findViewById(R.id.myImageView);
        }

        TextView _action, _date;
        ImageView _icon;
    }

    // region fields declarations
    private final String TAG = "MessageFragment";

    private List<EventEntity> _messages;
    private Context _context;
    // endregion
}
