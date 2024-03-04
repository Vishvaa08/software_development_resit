package com.example.darpafoodordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserHomepage extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_homepage);

        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Darpa Foodie");

        drawerLayout = findViewById(R.id.drawer);
    }

    public void LoginRedirect(View view){
        Intent intent = new Intent(UserHomepage.this, LoginActivity.class);
        startActivity(intent);
    }
    public void RegisterRedirect(View view){
        Intent intent = new Intent(UserHomepage.this, RegisterActivity.class);
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
        recreate();
    }
    //calls function redirectActivity for intent
    public void ClickMenu(View view){
        redirectActivity(UserHomepage.this, MenuActivity.class);
    }
    //calls function redirectActivity for intent
    public void ClickNotifications(View view){
        redirectActivity(UserHomepage.this, RegisterActivity.class);
    }
    public void ClickMyAccount(View view){
        redirectActivity(UserHomepage.this, AccountActivity.class);
    }
    public void ClickCart(View view){
        redirectActivity(UserHomepage.this, CartActivity.class);
    }
    public void ClickOrders(View view){redirectActivity(UserHomepage.this, OrdersActivity.class);}
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