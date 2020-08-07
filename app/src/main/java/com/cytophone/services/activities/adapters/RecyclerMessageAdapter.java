package com.cytophone.services.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cytophone.services.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerMessageAdapter extends RecyclerView.Adapter<RecyclerMessageAdapter.ViewMessageHolder> {

    String data1[], data2[];
    int images[];
    Context context;

    public RecyclerMessageAdapter(Context ct, String s1[], String s2[], int img[]){
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
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
        holder.myText1.setText(data1[position]);
        holder.myText2.setText(data2[position]);
        holder.myImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
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
