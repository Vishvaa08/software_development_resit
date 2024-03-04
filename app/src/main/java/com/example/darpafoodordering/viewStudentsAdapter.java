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

public class viewStudentsAdapter extends FirebaseRecyclerAdapter<viewStudentsHelperClass,viewStudentsAdapter.viewHolder>{


    public viewStudentsAdapter(@NonNull FirebaseRecyclerOptions<viewStudentsHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull viewStudentsHelperClass model) {
        holder.studentName.setText(model.getName());
        holder.studentPhone.setText(model.getPhoneNum());
        holder.studentEmail.setText(model.getEmail());
        holder.studentCredit.setText(model.getUserCredit());
        holder.studentUID.setText(model.getUserID());

        Glide.with(holder.studentPic.getContext())
                .load(model.getUser_picture())
                .error(R.drawable.img)
                .into(holder.studentPic);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_students_layout, parent, false);
        return new viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder{

        ImageView studentPic;
        TextView studentName, studentPhone, studentEmail, studentCredit, studentUID;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            studentPic = itemView.findViewById(R.id.studentPic);
            studentName = itemView.findViewById(R.id.studentName);
            studentPhone = itemView.findViewById(R.id.studentPhone);
            studentEmail = itemView.findViewById(R.id.studentEmail);
            studentCredit = itemView.findViewById(R.id.studentCredit);
            studentUID = itemView.findViewById(R.id.studentUID);
        }
    }

}
