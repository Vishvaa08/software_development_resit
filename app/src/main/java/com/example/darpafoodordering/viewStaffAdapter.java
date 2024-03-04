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

public class viewStaffAdapter extends FirebaseRecyclerAdapter<viewStaffHelperClass,viewStaffAdapter.viewHolder>{


    public viewStaffAdapter(@NonNull FirebaseRecyclerOptions<viewStaffHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull viewStaffHelperClass model) {
        holder.staffName.setText(model.getName());
        holder.staffPhone.setText(model.getPhoneNum());
        holder.staffEmail.setText(model.getEmail());

        Glide.with(holder.staffPic.getContext())
                .load(model.getUser_picture())
                .error(R.drawable.img)
                .into(holder.staffPic);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_staff_layout, parent, false);
        return new viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder{

        ImageView staffPic;
        TextView staffName, staffPhone, staffEmail;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            staffPic = itemView.findViewById(R.id.staffPic);
            staffName = itemView.findViewById(R.id.staffName);
            staffPhone = itemView.findViewById(R.id.staffPhone);
            staffEmail = itemView.findViewById(R.id.staffEmail);
        }
    }

}
