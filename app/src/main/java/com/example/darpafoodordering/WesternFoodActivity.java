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
import com.google.firebase.database.FirebaseDatabase;

public class WesternFoodActivity extends AppCompatActivity {

    RecyclerView WesternRecyclerView;
    DrawerLayout drawerLayout;
    ViewUserWesternAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_western_food);


        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Western");

        drawerLayout = findViewById(R.id.drawer);
        WesternRecyclerView = findViewById(R.id.WesternRecyclerView);

        String available = "Available";

        WesternRecyclerView.setLayoutManager(new LinearLayoutManager(WesternFoodActivity.this));

        FirebaseRecyclerOptions<ViewUserMenuHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ViewUserMenuHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Menu").child("Western").orderByChild("status").startAt(available).endAt(available), ViewUserMenuHelperClass.class)
                        .build();

        mainAdapter = new ViewUserWesternAdapter(options);
        WesternRecyclerView.setAdapter(mainAdapter);
    }
    public void CartClick(View view){
        Intent intent = new Intent(WesternFoodActivity.this, CartActivity.class);
        startActivity(intent);
    }
    public void BackClick(View view){
        Intent intent = new Intent(WesternFoodActivity.this, MenuActivity.class);
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