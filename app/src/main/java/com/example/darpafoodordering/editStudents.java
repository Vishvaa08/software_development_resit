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

public class editStudents extends AppCompatActivity {

    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    editStudentsAdapter mainAdapter;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_students);

        drawerLayout = findViewById(R.id.drawer);
        btnBack = findViewById(R.id.btnBack);
        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Edit Students");

        //receiving search value from manageMenu
        Intent intent = getIntent();
        String val = intent.getStringExtra("val");
        //go back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editStudents.this, viewStudents.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.edit_students_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(editStudents.this));
        //sets view for recycler view; retrieving from DB
        FirebaseRecyclerOptions<viewStudentsHelperClass> options =
                new FirebaseRecyclerOptions.Builder<viewStudentsHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("name").startAt(val).endAt(val), viewStudentsHelperClass.class)
                        .build();

        mainAdapter = new editStudentsAdapter(options);
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
        redirectActivity(editStudents.this, admin_home.class);
    }
    //calls function redirectActivity for intent
    public void selectUsers(View view){
        redirectActivity(editStudents.this, selectUser.class);
    }
    //calls function redirectActivity for intent
    public void manageMenu(View view){
        redirectActivity(editStudents.this, manageMenu.class);
    }
    public void userHome(View view){redirectActivity(editStudents.this, UserHomepage.class);}
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