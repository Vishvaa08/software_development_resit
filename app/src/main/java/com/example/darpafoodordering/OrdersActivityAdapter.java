package com.example.darpafoodordering;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class OrdersActivityAdapter extends FirebaseRecyclerAdapter<ViewOrdersHelperClass,OrdersActivityAdapter.viewHolder>{

    public OrdersActivityAdapter(@NonNull FirebaseRecyclerOptions<ViewOrdersHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull ViewOrdersHelperClass model) {
        holder.orderName.setText(model.getName());
        holder.orderStatus.setText(model.getStatus());
        holder.orderPrice.setText("RM" + model.getPrice());
        holder.orderQuantity.setText(model.getQuantity());
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_orders_layout, parent, false);
        return new viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView orderName, orderStatus, orderPrice, orderQuantity;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.orderName);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderQuantity = itemView.findViewById(R.id.orderQuantity);
        }
    }

}
