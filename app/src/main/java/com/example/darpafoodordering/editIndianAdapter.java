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

public class editIndianAdapter  extends FirebaseRecyclerAdapter<viewMenuHelperClass,editIndianAdapter.viewHolder> {

    public editIndianAdapter(@NonNull FirebaseRecyclerOptions<viewMenuHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull editIndianAdapter.viewHolder holder, final int position, @NonNull viewMenuHelperClass model) {
        holder.itemName.setText(model.getName());
        holder.itemPrice.setText(model.getPrice());
        holder.itemSubCategory.setText(model.getfoodtype());
        holder.itemStatus.setText(model.getStatus());

        Glide.with(holder.itemPic.getContext())
                .load(model.getImage())
                .error(R.drawable.img)
                .into(holder.itemPic);

        holder.btnEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.itemPic.getContext())
                        .setContentHolder(new ViewHolder(R.layout.edit_menu_container))
                        .setExpanded(true, 1500)
                        .create();

                //dialogPlus.show();

                View v = dialogPlus.getHolderView();

                EditText name = v.findViewById(R.id.etName);
                EditText price = v.findViewById(R.id.etPrice);
                //EditText category = v.findViewById(R.id.etCategory);
                Spinner itemCategory = v.findViewById(R.id.spCategory);
                Spinner itemStatus = v.findViewById(R.id.spStatus);
                Button btnConfirmEdit = v.findViewById(R.id.btnConfirmEdit);

                //adapter sets values for item category spinner
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.foodtype, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemCategory.setAdapter(adapter);

                //adapter sets values for item category spinner
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(view.getContext(), R.array.itemStatus, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemStatus.setAdapter(adapter1);

                name.setText(model.getName());
                price.setText(model.getPrice());
                //category.setText(model.getCategory());

                dialogPlus.show();

                btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("price", price.getText().toString());
                        map.put("foodtype", itemCategory.getSelectedItem().toString());
                        map.put("status", itemStatus.getSelectedItem().toString());

                        FirebaseDatabase.getInstance().getReference().child("Menu").child("Indian")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.itemName.getContext(), "Data Edited!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();

                                        Intent intent = new Intent(view.getContext(), manageMenu.class);
                                        v.getContext().startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.itemName.getContext(), "Error Editing Data!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemName.getContext());
                builder.setTitle("Confirm Delete ?");
                builder.setMessage("Action can't be undone!");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Menu")
                                .child(getRef(position).getKey()).removeValue();

                        Intent intent = new Intent(view.getContext(), manageMenu.class);
                        view.getContext().startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.itemName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public editIndianAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_menu_layout, parent, false);
        return new editIndianAdapter.viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder{

        ImageView itemPic;
        TextView itemName, itemPrice, itemSubCategory, itemStatus;
        Button btnEditItem, btnDeleteItem;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            itemPic = (ImageView) itemView.findViewById(R.id.itemPic);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            itemSubCategory = (TextView) itemView.findViewById(R.id.itemSubCategory);
            itemStatus = (TextView) itemView.findViewById(R.id.itemStatus);

            btnEditItem = (Button) itemView.findViewById(R.id.btnEditItem);
            btnDeleteItem = (Button) itemView.findViewById(R.id.btnDeleteItem);
        }
    }

}
