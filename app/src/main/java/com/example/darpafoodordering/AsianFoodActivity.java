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

public class AsianFoodActivity extends AppCompatActivity {

    RecyclerView AsianRecyclerView;
    DrawerLayout drawerLayout;
    ViewUserAsianAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asian_food);

        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Asian");

        drawerLayout = findViewById(R.id.drawer);
        AsianRecyclerView = findViewById(R.id.AsianRecyclerView);

        String available = "Available";

        AsianRecyclerView.setLayoutManager(new LinearLayoutManager(AsianFoodActivity.this));

        FirebaseRecyclerOptions<ViewUserMenuHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ViewUserMenuHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Menu").child("Asian").orderByChild("status").startAt(available).endAt(available), ViewUserMenuHelperClass.class)
                        .build();

        mainAdapter = new ViewUserAsianAdapter(options);
        AsianRecyclerView.setAdapter(mainAdapter);
    }
    public void CartClick(View view){
        Intent intent = new Intent(AsianFoodActivity.this, CartActivity.class);
        startActivity(intent);
    }
    public void BackClick(View view){
        Intent intent = new Intent(AsianFoodActivity.this, MenuActivity.class);
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