package com.example.darpafoodordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class editAsian extends AppCompatActivity {

    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    editAsianAdapter mainAdapter;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_asian);

        drawerLayout = findViewById(R.id.drawer);
        btnBack = findViewById(R.id.btnBack);
        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Edit Asian");

        //receiving search value from manageMenu
        Intent intent = getIntent();
        String val = intent.getStringExtra("val");
        //go back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editAsian.this, manageMenu.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.edit_asian_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(editAsian.this));
        //sets view for recycler view; retrieving from DB
        FirebaseRecyclerOptions<viewMenuHelperClass> options =
                new FirebaseRecyclerOptions.Builder<viewMenuHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Menu").child("Asian").orderByChild("name").startAt(val).endAt(val), viewMenuHelperClass.class)
                        .build();

        mainAdapter = new editAsianAdapter(options);
        recyclerView.setAdapter(mainAdapter);
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
        redirectActivity(editAsian.this, admin_home.class);
    }
    //calls function redirectActivity for intent
    public void selectUsers(View view){
        redirectActivity(editAsian.this, selectUser.class);
    }
    //calls function redirectActivity for intent
    public void manageMenu(View view){
        redirectActivity(editAsian.this, manageMenu.class);
    }
    public void userHome(View view){redirectActivity(editAsian.this, UserHomepage.class);}
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
    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}