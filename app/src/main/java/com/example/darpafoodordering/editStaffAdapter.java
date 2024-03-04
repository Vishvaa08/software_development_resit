package com.example.darpafoodordering;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class editStaffAdapter  extends FirebaseRecyclerAdapter<viewStaffHelperClass,editStaffAdapter.viewHolder> {

    public editStaffAdapter(@NonNull FirebaseRecyclerOptions<viewStaffHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull editStaffAdapter.viewHolder holder, final int position, @NonNull viewStaffHelperClass model) {
        holder.staffName.setText(model.getName());
        holder.staffEmail.setText(model.getEmail());
        holder.staffPhone.setText(model.getPhoneNum());
        holder.staffStatus.setText(model.getStatus());

        Glide.with(holder.staffPic.getContext())
                .load(model.getUser_picture())
                .error(R.drawable.img)
                .into(holder.staffPic);

        holder.btnEditStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.staffPic.getContext())
                        .setContentHolder(new ViewHolder(R.layout.edit_students_container))
                        .setExpanded(true, 800)
                        .create();

                //dialogPlus.show();

                View v = dialogPlus.getHolderView();

                Spinner studentStatus = v.findViewById(R.id.spStatus);
                Button btnConfirmEdit = v.findViewById(R.id.btnConfirmVerifyStudent);

                //adapter sets values for item category spinner
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.studentStatus, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                studentStatus.setAdapter(adapter);

                dialogPlus.show();

                btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Map<String, Object> map = new HashMap<>();
                        map.put("status", studentStatus.getSelectedItem().toString());

                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.staffName.getContext(), "Data Edited!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();

                                        Intent intent = new Intent(view.getContext(), viewStaff.class);
                                        v.getContext().startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.staffName.getContext(), "Error Editing Data!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDeleteStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.staffName.getContext());
                builder.setTitle("Confirm Delete ?");
                builder.setMessage("Action can't be undone!");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey()).removeValue();

                        Intent intent = new Intent(view.getContext(), viewStaff.class);
                        view.getContext().startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.staffName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }
    @NonNull
    @Override
    public editStaffAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_staff_layout, parent, false);
        return new editStaffAdapter.viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView staffPic;
        TextView staffName, staffEmail, staffPhone, staffStatus;
        Button btnEditStaff, btnDeleteStaff;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            staffPic = (ImageView) itemView.findViewById(R.id.staffPic);
            staffName = (TextView) itemView.findViewById(R.id.staffName);
            staffEmail = (TextView) itemView.findViewById(R.id.staffEmail);
            staffPhone = (TextView) itemView.findViewById(R.id.staffPhone);
            staffStatus = (TextView) itemView.findViewById(R.id.staffStatus);

            btnEditStaff = (Button) itemView.findViewById(R.id.btnVerifyStaff);
            btnDeleteStaff = (Button) itemView.findViewById(R.id.btnDeleteStaff);
        }
    }

}
