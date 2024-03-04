package com.example.darpafoodordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DrinksActivity extends AppCompatActivity {

    RecyclerView DrinksRecyclerView;
    DrawerLayout drawerLayout;
    ViewUserDrinksAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Drinks");

        drawerLayout = findViewById(R.id.drawer);
        DrinksRecyclerView = findViewById(R.id.DrinksRecyclerView);

        String available = "Available";

        DrinksRecyclerView.setLayoutManager(new LinearLayoutManager(DrinksActivity.this));

        FirebaseRecyclerOptions<ViewUserMenuHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ViewUserMenuHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Menu").child("Drinks").orderByChild("status").startAt(available).endAt(available), ViewUserMenuHelperClass.class)
                        .build();

        mainAdapter = new ViewUserDrinksAdapter(options);
        DrinksRecyclerView.setAdapter(mainAdapter);
    }
    public void CartClick(View view){
        Intent intent = new Intent(DrinksActivity.this, CartActivity.class);
        startActivity(intent);
    }
    public void BackClick(View view){
        Intent intent = new Intent(DrinksActivity.this, MenuActivity.class);
        startActivity(intent);
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