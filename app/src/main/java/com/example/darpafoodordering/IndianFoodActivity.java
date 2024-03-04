package com.example.darpafoodordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class IndianFoodActivity extends AppCompatActivity {

    RecyclerView IndianRecyclerView;
    DrawerLayout drawerLayout;
    ViewUserIndianAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indian_food);

        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Indian");

        drawerLayout = findViewById(R.id.drawer);
        IndianRecyclerView = findViewById(R.id.IndianRecyclerView);

        String available = "Available";

        IndianRecyclerView.setLayoutManager(new LinearLayoutManager(IndianFoodActivity.this));

        FirebaseRecyclerOptions<ViewUserMenuHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ViewUserMenuHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Menu").child("Indian").orderByChild("status").startAt(available).endAt(available), ViewUserMenuHelperClass.class)
                        .build();

        mainAdapter = new ViewUserIndianAdapter(options);
        IndianRecyclerView.setAdapter(mainAdapter);
    }
    public void CartClick(View view){
        Intent intent = new Intent(IndianFoodActivity.this, CartActivity.class);
        startActivity(intent);
    }
    public void BackClick(View view){
        Intent intent = new Intent(IndianFoodActivity.this, MenuActivity.class);
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