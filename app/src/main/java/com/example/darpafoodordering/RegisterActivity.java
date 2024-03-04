package com.example.darpafoodordering;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword, name, age, userID, phonenum;
    Spinner identity;
    Button registerBtn;
    FirebaseAuth mAuth;
    TextView textView;
    String val, credit, userStatus, imageUrl;
    DrawerLayout drawerLayout;


    DatabaseReference reference;
    private StorageReference storageReference;
    ImageView userPicture;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("users");

        drawerLayout = findViewById(R.id.drawer);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerBtn);
        textView = findViewById(R.id.loginBtn);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        userID = findViewById(R.id.userID);
        identity = findViewById(R.id.identity);
        phonenum = findViewById(R.id.phoneNum);
        userPicture = findViewById(R.id.user_picture);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
        credit = String.valueOf(0);

        userStatus = "Unverified";

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Identity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        identity.setAdapter(adapter);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            userPicture.setImageURI(uri);
                        }else {
                            Toast.makeText(RegisterActivity.this, "No Image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        userPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
    }
    public void SaveData(){

        storageReference = FirebaseStorage.getInstance().getReference().child("uploads")
                .child(uri.getLastPathSegment());

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                SaveUserDetails();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "No image selected!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void ProcessRegister(View view){

        RegisterUser();
        SaveData();

    }
    public void RegisterUser() {

        String email, password;
        email = String.valueOf(editTextEmail.getText());
        password = String.valueOf(editTextPassword.getText());

        val = editTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Enter an email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Enter a password!", Toast.LENGTH_SHORT).show();
            return;
        }


            // For non-admin users, attempt to create an account
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account Created",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Log.e("RegistrationActivity", "Authentication Failed", task.getException());
                        }
                    });
        }
    public void SaveUserDetails(){

        FirebaseAuth dbAuth = FirebaseAuth.getInstance();
        String Name = Objects.requireNonNull(name.getText()).toString();
        String Age = Objects.requireNonNull(age.getText()).toString();
        String UserID = Objects.requireNonNull(userID.getText()).toString();
        String phoneNum = Objects.requireNonNull(phonenum.getText().toString());
        String userCredit = credit;
        String email = val;
        String role = identity.getSelectedItem().toString();
        String status = userStatus;

        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(RegisterActivity.this, "Enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminViewUserDataClass dataClass = new AdminViewUserDataClass(Name, Age, UserID, phoneNum, userCredit, email, role, status, imageUrl);
        String user_id = Objects.requireNonNull(dbAuth.getCurrentUser()).getUid();

        FirebaseDatabase.getInstance().getReference("users").child(user_id)
                .setValue(dataClass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // Redirect to Me_Fragment after saving
                        Intent intent1 = new Intent(RegisterActivity.this, UserHomepage.class);
                        startActivity(intent1);
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, Objects.requireNonNull(e.getMessage()),
                        Toast.LENGTH_SHORT).show());

    }
    public void GoLogin(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void ClickNav(View view){
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
    public void ClickHome(View view){
        redirectActivity(RegisterActivity.this, UserHomepage.class);
    }
    //calls function redirectActivity for intent
    public void ClickMenu(View view){
        redirectActivity(RegisterActivity.this, MenuActivity.class);
    }
    //calls function redirectActivity for intent
    public void ClickNotifications(View view){
        redirectActivity(RegisterActivity.this, NotificationsActivity.class);
    }
    public void ClickMyAccount(View view){
        redirectActivity(RegisterActivity.this, AccountActivity.class);
    }
    public void ClickCart(View view){
        redirectActivity(RegisterActivity.this, CartActivity.class);
    }
    public void ClickOrders(View view){redirectActivity(RegisterActivity.this, OrdersActivity.class);}
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
}