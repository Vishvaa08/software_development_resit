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
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class viewStaff extends AppCompatActivity {

    DrawerLayout drawerLayout;
    viewStaffAdapter mainAdapter;
    EditText searchbar;
    ImageView btnSearch;
    String searchValue;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_staff);

        drawerLayout = findViewById(R.id.drawer);
        searchbar = findViewById(R.id.searchbar);
        btnSearch = findViewById(R.id.btnSearch);
        //search test
        searchValue = searchbar.getText().toString();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchValue = searchbar.getText().toString();
                Intent intent = new Intent(viewStaff.this, editStaff.class);
                intent.putExtra("val", searchValue);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.view_staff_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewStaff.this));

        String Staff = "Staff";

        FirebaseRecyclerOptions<viewStaffHelperClass> options =
                new FirebaseRecyclerOptions.Builder<viewStaffHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("role").startAt(Staff).endAt(Staff), viewStaffHelperClass.class)
                        .build();

        mainAdapter = new viewStaffAdapter(options);
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
        redirectActivity(viewStaff.this, admin_home.class);
    }
    //calls function redirectActivity for intent
    public void selectUsers(View view){
        redirectActivity(viewStaff.this, selectUser.class);
    }
    //recreates page
    public void manageMenu(View view){
        redirectActivity(viewStaff.this, manageMenu.class);
    }
    public void userHome(View view){redirectActivity(viewStaff.this, UserHomepage.class);}
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