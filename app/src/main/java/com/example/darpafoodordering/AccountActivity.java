package com.example.darpafoodordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView name, myName, myEmail, myUID, myStatus, myRole, myPhone, myAge, myCredit;
    FirebaseUser user;
    String UID;
    DatabaseReference databaseReference;
    ImageView profPic;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("My Account");

        drawerLayout = findViewById(R.id.drawer);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.btnLogout);
        name = findViewById(R.id.name);
        profPic = findViewById(R.id.profilePic);
        myName = findViewById(R.id.userName);
        myEmail = findViewById(R.id.email);
        myUID = findViewById(R.id.UID);
        myStatus = findViewById(R.id.status);
        myRole = findViewById(R.id.role);
        myPhone = findViewById(R.id.phone);
        myAge = findViewById(R.id.age);
        myCredit = findViewById(R.id.credit);
        user = auth.getCurrentUser();

        UID = auth.getUid();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Log.d("Firebase", "User ID: " + UID);

            // Reference to "User Details" under the current user
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(UID);

            // Add a ValueEventListener to retrieve and update data
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Use the UserInfoClass to map the data from the database
                        AdminViewUserDataClass userInfo = dataSnapshot.getValue(AdminViewUserDataClass.class);

                        if (userInfo != null) {
                            // Update the TextViews with the retrieved data
                            name.setText(userInfo.getName());
                            myName.setText(userInfo.getName());
                            myEmail.setText(userInfo.getEmail());
                            myAge.setText(userInfo.getAge());
                            myCredit.setText(userInfo.getUserCredit());
                            myPhone.setText(userInfo.getPhoneNum());
                            myRole.setText(userInfo.getRole());
                            myStatus.setText(userInfo.getStatus());
                            myUID.setText(userInfo.getUserID());

                            Glide.with(profPic.getContext())
                                    .load(userInfo.getUser_picture())
                                    .error(R.drawable.img)
                                    .into(profPic);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error: " + databaseError.getMessage());
                }
            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
        redirectActivity(AccountActivity.this, UserHomepage.class);
    }
    //calls function redirectActivity for intent
    public void ClickMenu(View view){
        redirectActivity(AccountActivity.this, MenuActivity.class);
    }
    //calls function redirectActivity for intent
    public void ClickNotifications(View view){
        redirectActivity(AccountActivity.this, NotificationsActivity.class);
    }
    public void ClickMyAccount(View view){
        recreate();
    }
    public void ClickCart(View view){
        redirectActivity(AccountActivity.this, CartActivity.class);
    }
    public void ClickOrders(View view){redirectActivity(AccountActivity.this, OrdersActivity.class);}
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