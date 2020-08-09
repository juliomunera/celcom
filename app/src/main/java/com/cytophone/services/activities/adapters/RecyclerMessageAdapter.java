package com.cytophone.services.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cytophone.services.R;
import com.cytophone.services.entities.EventEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerMessageAdapter extends RecyclerView.Adapter<RecyclerMessageAdapter.ViewMessageHolder> {

    List<EventEntity> _messages;
    Context context;

    public RecyclerMessageAdapter(Context ct, List<EventEntity> eventList){
        context = ct;
        /*
        data1 = s1;
        data2 = s2;
        images = img;
        */

        _messages = eventList;
    }

    @NonNull
    @Override
    public ViewMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_message_list, parent, false);
        return new ViewMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMessageHolder holder, int position) {
        holder.myText1.setText(_messages.get(position).getAction());
        holder.myText2.setText(String.format("%tY-%<tm-%<td %<tH:%<tM:%<tS",_messages.get(position).getCreatedDate()));

        String actionType = _messages.get(position).getAction().toLowerCase();
        if(actionType.contains(("subscriber"))){
            if(actionType.contains(("insert")))
                holder.myImage.setImageResource(R.drawable.ic_playlist_add_black_24dp);
            else if(actionType.contains(("delete")))
                holder.myImage.setImageResource(R.drawable.ic_clear_black_24dp);
            else if(actionType.contains(("update")))
                holder.myImage.setImageResource(R.drawable.ic_swap_horiz_black_24dp);
        }else if(actionType.contains(("authorizator"))){
            if(actionType.contains(("insert")))
                holder.myImage.setImageResource(R.drawable.ic_playlist_add_black_24dp);
            else if(actionType.contains(("delete")))
                holder.myImage.setImageResource(R.drawable.ic_clear_black_24dp);
            else if(actionType.contains(("update")))
                holder.myImage.setImageResource(R.drawable.ic_swap_horiz_black_24dp);
        }else if(actionType.contains(("unlock"))){
            holder.myImage.setImageResource(R.drawable.ic_playlist_add_black_24dp);
        }
    }

    /*
    int _images[] = {
            R.drawable.ic_playlist_add_black_24dp,
            R.drawable.ic_swap_horiz_black_24dp,
            R.drawable.ic_clear_black_24dp,
            R.drawable.ic_playlist_add_black_24dp,
            R.drawable.ic_playlist_add_black_24dp
    };
     */

    @Override
    public int getItemCount() {
        return _messages.size();
    }

    public class ViewMessageHolder extends RecyclerView.ViewHolder {

        TextView myText1, myText2;
        ImageView myImage;

        public ViewMessageHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.myText1);
            myText2 = itemView.findViewById(R.id.myText2);
            myImage = itemView.findViewById(R.id.myImageView);
        }
    }
}
