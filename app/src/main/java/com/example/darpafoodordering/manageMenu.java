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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class manageMenu extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView drinks, indian, western, asian;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        title = findViewById(R.id.page_title);
        title.setText("Menu");

        drawerLayout = findViewById(R.id.drawer);
        drinks = findViewById(R.id.drinks);
        indian = findViewById(R.id.indian);
        western = findViewById(R.id.western);
        asian = findViewById(R.id.asian);

        FloatingActionButton btnAddMenu = (FloatingActionButton) findViewById(R.id.btnAddMenu);

        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (manageMenu.this, addMenu.class);
                startActivity(intent);
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manageMenu.this, adminViewDrinks.class);
                startActivity(intent);
            }
        });

        indian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manageMenu.this, adminViewIndian.class);
                startActivity(intent);
            }
        });

        western.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manageMenu.this, adminViewWestern.class);
                startActivity(intent);
            }
        });

        asian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manageMenu.this, adminViewAsian.class);
                startActivity(intent);
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
        redirectActivity(manageMenu.this, admin_home.class);
    }
    //calls function redirectActivity for intent
    public void selectUsers(View view){
        redirectActivity(manageMenu.this, selectUser.class);
    }
    //recreates page
    public void manageMenu(View view){
        recreate();
    }
    public void userHome(View view){redirectActivity(manageMenu.this, UserHomepage.class);}
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
    //.setQuery(FirebaseDatabase.getInstance().getReference().child("Menu").orderByChild("category").startAt("Drink").endAt("Drink\uf8ff"), viewMenuHelperClass.class)
}