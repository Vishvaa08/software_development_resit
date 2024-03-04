package com.example.darpafoodordering;

import static androidx.core.content.ContextCompat.startActivity;

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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewUserAsianAdapter extends FirebaseRecyclerAdapter<ViewUserMenuHelperClass,ViewUserAsianAdapter.viewHolder>{

    public ViewUserAsianAdapter(@NonNull FirebaseRecyclerOptions<ViewUserMenuHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull ViewUserMenuHelperClass model) {
        holder.menuName.setText(model.getName());
        holder.menuPrice.setText(model.getPrice());

        Glide.with(holder.menuPic.getContext())
                .load(model.getImage())
                .error(R.drawable.img)
                .into(holder.menuPic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), AsianDetailsActivity.class);
                intent.putExtra("name", model.getName());
                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_user_menu_layout, parent, false);
        return new viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder{

        ImageView menuPic;
        TextView menuName, menuPrice;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            menuPic = itemView.findViewById(R.id.menuPic);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
        }
    }

}
