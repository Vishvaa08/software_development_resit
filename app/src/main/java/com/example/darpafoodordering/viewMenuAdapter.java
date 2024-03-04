package com.example.darpafoodordering;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class viewMenuAdapter extends FirebaseRecyclerAdapter<viewMenuHelperClass,viewMenuAdapter.viewHolder>{

    public viewMenuAdapter(@NonNull FirebaseRecyclerOptions<viewMenuHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull viewMenuHelperClass model) {
        holder.itemName.setText(model.getName());
        holder.itemPrice.setText(model.getPrice());
        holder.itemSubCategory.setText(model.getfoodtype());
        holder.itemStatus.setText(model.getStatus());

        Glide.with(holder.itemPic.getContext())
                .load(model.getImage())
                .error(R.drawable.img)
                .into(holder.itemPic);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu_layout, parent, false);
        return new viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder{

        ImageView itemPic;
        TextView itemName, itemPrice, itemSubCategory, itemStatus;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            itemPic = (ImageView) itemView.findViewById(R.id.itemPic);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            itemSubCategory = (TextView) itemView.findViewById(R.id.itemSubCategory);
            itemStatus = (TextView) itemView.findViewById(R.id.itemStatus);
        }
    }

}
