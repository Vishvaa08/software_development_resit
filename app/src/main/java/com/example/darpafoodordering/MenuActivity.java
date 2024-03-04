package com.example.darpafoodordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    ImageView drinks, indian, western, asian;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Menu");

        drawerLayout = findViewById(R.id.drawer);
        drinks = findViewById(R.id.drinks);
        indian = findViewById(R.id.indian);
        western = findViewById(R.id.western);
        asian = findViewById(R.id.asian);

        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, DrinksActivity.class);
                startActivity(intent);
            }
        });

        indian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, IndianFoodActivity.class);
                startActivity(intent);
            }
        });

        western.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, WesternFoodActivity.class);
                startActivity(intent);
            }
        });

        asian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AsianFoodActivity.class);
                startActivity(intent);
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
        redirectActivity(MenuActivity.this, UserHomepage.class);
    }
    //calls function redirectActivity for intent
    public void ClickMenu(View view){
        redirectActivity(MenuActivity.this, MenuActivity.class);
    }
    //calls function redirectActivity for intent
    public void ClickNotifications(View view){
        redirectActivity(MenuActivity.this, NotificationsActivity.class);
    }
    public void ClickMyAccount(View view){
        redirectActivity(MenuActivity.this, AccountActivity.class);
    }
    public void ClickCart(View view){
        redirectActivity(MenuActivity.this, CartActivity.class);
    }
    public void ClickOrders(View view){redirectActivity(MenuActivity.this, OrdersActivity.class);}
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