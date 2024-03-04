package com.example.darpafoodordering;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class addMenu extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView itemImage, backBtn;
    EditText itemName, itemPrice;
    Spinner itemCategory, foodtype, foodstatus;
    Button btnConfirmAdd;
    private static final int photo = 6;
    private Uri ImageUri;
    String menuName, menuPrice, addItemCategory, addFoodType, itemKeyRandom, downloadImageUrl, addFoodStatus;
    StorageReference reference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        drawerLayout = findViewById(R.id.drawer);
        itemImage = (ImageView) findViewById(R.id.itemImage);
        backBtn = (ImageView) findViewById(R.id.btnBack);
        itemName = (EditText) findViewById(R.id.itemName);
        itemPrice = (EditText) findViewById(R.id.itemPrice);
        btnConfirmAdd = (Button) findViewById(R.id.btnConfirmAdd);
        itemCategory = (Spinner) findViewById(R.id.itemCategory);
        foodtype = (Spinner) findViewById(R.id.foodtype);
        foodstatus = (Spinner) findViewById(R.id.foodstatus);

        reference = FirebaseStorage.getInstance().getReference().child("Menu Images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Menu");

        //sets the page header name during on create
        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Add Menu");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addMenu.this, manageMenu.class);
                startActivity(intent);
            }
        });

        //onclick calls openPhotos function
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhotos();
            }
        });

        //adapter sets values for item category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.itemCategory, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategory.setAdapter(adapter);

        //adapter sets values for food type spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.foodtype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodtype.setAdapter(adapter1);

        //adapter sets values for food type spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.itemStatus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodstatus.setAdapter(adapter2);

        //confirm button calls validate item data function
        btnConfirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateItemData();
            }
        });
    }
    //function for when hamburger icon is clicked, calls openDrawer function
    public void AdminMenu(View view){
        openDrawer(drawerLayout);
    }

    //openDrawer function, opens the navigation drawer
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    //function for when logo is clicked, calls closeDrawer function
    public void clickLogo(View view){
        closeDrawer(drawerLayout);
    }

    //closeDrawer function, closes the navigation drawer
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //calls function redirectActivity for intent
    public void adminHome(View view){
        redirectActivity(addMenu.this, admin_home.class);
    }
    //calls function redirectActivity for intent
    public void selectUsers(View view){
        redirectActivity(addMenu.this, selectUser.class);
    }
    //calls function redirectActivity for intent
    public void manageMenu(View view){
        redirectActivity(addMenu.this, manageMenu.class);
    }
    public void userHome(View view){redirectActivity(addMenu.this, UserHomepage.class);}
    //function redirectActivity that is called when nav option is clicked
    public static void redirectActivity(Activity activity, Class Class){
        Intent intent = new Intent(activity,Class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
    //openPhoto function opens gallery to select image
    public void openPhotos(){
        Intent intent = new Intent ();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==photo && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            itemImage.setImageURI(ImageUri);
        }
    }
    //validation to prevent empty fields
    public void ValidateItemData(){
        menuName = itemName.getText().toString();
        menuPrice = itemPrice.getText().toString();
        if(ImageUri == null){
            Toast.makeText(this, "Item Image Not Selected!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(menuName)){
            Toast.makeText(this, "Enter Item Name!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(menuPrice)){
            Toast.makeText(this, "Enter Item Price!", Toast.LENGTH_SHORT).show();
        }else{
            StoreItemInformation();
        }
    }
    //
    public void StoreItemInformation(){

        addItemCategory = itemCategory.getSelectedItem().toString();
        addFoodType = foodtype.getSelectedItem().toString();
        addFoodStatus = foodstatus.getSelectedItem().toString();

        StorageReference filePath = reference.child(ImageUri.getLastPathSegment() + itemKeyRandom + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(addMenu.this, "Error" + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            SaveItemInfotoDB();
                        }
                    }
                });
            }
        });
    }
    //function saves all info into firebase database
    public void SaveItemInfotoDB(){
        HashMap<String, Object> itemMap = new HashMap<>();
        itemMap.put("category", addItemCategory);
        itemMap.put("foodtype", addFoodType);
        itemMap.put("status", addFoodStatus);
        itemMap.put("name", menuName);
        itemMap.put("price", menuPrice);
        itemMap.put("image", downloadImageUrl);

        databaseReference.child(addFoodType).child(menuName).updateChildren(itemMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent (addMenu.this, manageMenu.class);
                            startActivity(intent);

                            Toast.makeText(addMenu.this, "Item is added", Toast.LENGTH_SHORT).show();
                        }else{
                            String message = task.getException().toString();
                            Toast.makeText(addMenu.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}