package com.example.darpafoodordering;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewCartAdapter extends FirebaseRecyclerAdapter<ViewCartHelperClass,ViewCartAdapter.viewHolder>{

    double lastTotalPrice = 0;
    Context context;

    public ViewCartAdapter(@NonNull FirebaseRecyclerOptions<ViewCartHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull ViewCartHelperClass model) {
        holder.cartName.setText(model.getName());
        holder.cartQuantity.setText(model.getQuantity());
        holder.cartPrice.setText("RM" + model.getPrice());

        double perItem = (Double.valueOf(model.getPrice())) * (Integer.valueOf(model.getQuantity()));
        lastTotalPrice = lastTotalPrice + perItem;
        Intent intent = new Intent("perItem");
        intent.putExtra("total", String.valueOf(lastTotalPrice));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cart_layout, parent, false);
        return new viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView cartName, cartQuantity, cartPrice;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            cartName = itemView.findViewById(R.id.cartName);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            cartPrice = itemView.findViewById(R.id.cartPrice);
        }
    }

}
