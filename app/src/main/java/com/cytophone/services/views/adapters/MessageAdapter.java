package com.cytophone.services.views.adapters;

import com.cytophone.services.entities.EventEntity;
import com.cytophone.services.R;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
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
        String text = this._messages.get(position).getAction().toLowerCase();

        text = text.replace("suscriberinsert", this._context.getString(R.string.suscriberinsert))
            .replace("suscriberdelete", this._context.getString(R.string.suscriberdelete))
            .replace("authorizatorinsert", this._context.getString(R.string.authorizatorinsert))
            .replace("authorizatordelete", this._context.getString(R.string.authorizatordelete))
            .replace("unlockcodeinsert", this._context.getString(R.string.unlock));

        holder._text1.setText(text);
        holder._text2.setText(String.format("%tY-%<tm-%<td %<tH:%<tM:%<tS",
                _messages.get(position).getCreatedDate()));

        String actionType = this._messages.get(position).getAction().toLowerCase();
        if(actionType.contains(("suscriber"))){
            if(actionType.contains(("insert")))
                holder._image.setImageResource(R.drawable.ic_playlist_add_black_24dp);
            else if(actionType.contains(("delete")))
                holder._image.setImageResource(R.drawable.ic_clear_black_24dp);
        }else if(actionType.contains(("authorizator"))){
            if(actionType.contains(("insert")))
                holder._image.setImageResource(R.drawable.ic_playlist_add_black_24dp);
            else if(actionType.contains(("delete")))
                holder._image.setImageResource(R.drawable.ic_clear_black_24dp);
        }else if(actionType.contains(("unlock"))){
            holder._image.setImageResource(R.drawable.ic_baseline_lock_open_24);
        }
    }

    @Override
    public int getItemCount() {
        return this._messages.size();
    }

    public class ViewMessageHolder extends RecyclerView.ViewHolder {
        public ViewMessageHolder(@NonNull View itemView) {
            super(itemView);

            this._text1 = itemView.findViewById(R.id.myText1);
            this._text2 = itemView.findViewById(R.id.myText2);
            this._image = itemView.findViewById(R.id.myImageView);
        }

        TextView _text1, _text2;
        ImageView _image;
    }

    List<EventEntity> _messages;
    Context _context;
}
