package com.example.darpafoodordering;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class editStudentsAdapter  extends FirebaseRecyclerAdapter<viewStudentsHelperClass,editStudentsAdapter.viewHolder> {

    public editStudentsAdapter(@NonNull FirebaseRecyclerOptions<viewStudentsHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull editStudentsAdapter.viewHolder holder, final int position, @NonNull viewStudentsHelperClass model) {
        holder.studentName.setText(model.getName());
        holder.studentEmail.setText(model.getEmail());
        holder.studentPhone.setText(model.getPhoneNum());
        holder.studentCredit.setText(model.getUserCredit());
        holder.studentStatus.setText(model.getStatus());

        Glide.with(holder.studentPic.getContext())
                .load(model.getUser_picture())
                .error(R.drawable.img)
                .into(holder.studentPic);

        holder.btnEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.studentPic.getContext())
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
                                        Toast.makeText(holder.studentName.getContext(), "Data Edited!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();

                                        Intent intent = new Intent(view.getContext(), viewStudents.class);
                                        v.getContext().startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.studentName.getContext(), "Error Editing Data!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnAddCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.studentPic.getContext())
                        .setContentHolder(new ViewHolder(R.layout.credit_students_container))
                        .setExpanded(true, 1000)
                        .create();

                dialogPlus.show();

                View v = dialogPlus.getHolderView();

                Button btnConfirmAddCredit = v.findViewById(R.id.btnConfirmAddCredit);
                TextView labelCurrentCredit = v.findViewById(R.id.tvCurrentCredit);
                EditText addCreditValue = v.findViewById(R.id.addCreditValue);

                dialogPlus.show();

                labelCurrentCredit.setText(model.getUserCredit());

                btnConfirmAddCredit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        double topUp = Double.valueOf(addCreditValue.getText().toString());
                        double oldBal = Double.valueOf(labelCurrentCredit.getText().toString());
                        String lastBal = String.valueOf(topUp + oldBal);

                        Map<String, Object> map = new HashMap<>();
                        map.put("userCredit", lastBal);

                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.studentName.getContext(), "Data Edited!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();

                                        Intent intent = new Intent(view.getContext(), viewStudents.class);
                                        v.getContext().startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.studentName.getContext(), "Error Editing Data!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.studentName.getContext());
                builder.setTitle("Confirm Delete ?");
                builder.setMessage("Action can't be undone!");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey()).removeValue();

                        Intent intent = new Intent(view.getContext(), viewStudents.class);
                        view.getContext().startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.studentName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }
    @NonNull
    @Override
    public editStudentsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_students_layout, parent, false);
        return new editStudentsAdapter.viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView studentPic;
        TextView studentName, studentEmail, studentPhone, studentCredit, studentStatus;
        Button btnEditStudent, btnAddCredit, btnDeleteStudent;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            studentPic = (ImageView) itemView.findViewById(R.id.studentPic);
            studentName = (TextView) itemView.findViewById(R.id.studentName);
            studentEmail = (TextView) itemView.findViewById(R.id.studentEmail);
            studentPhone = (TextView) itemView.findViewById(R.id.studentPhone);
            studentCredit = (TextView) itemView.findViewById(R.id.studentCredit);
            studentStatus = (TextView) itemView.findViewById(R.id.studentStatus);

            btnEditStudent = (Button) itemView.findViewById(R.id.btnVerifyStudent);
            btnAddCredit = (Button) itemView.findViewById(R.id.btnAddCredit);
            btnDeleteStudent = (Button) itemView.findViewById(R.id.btnDeleteStudent);
        }
    }

}
