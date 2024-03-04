package com.example.darpafoodordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView orderRecyclerView;
    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    OrdersActivityAdapter mainAdapter;
    String custID, UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        TextView title = (TextView) findViewById(R.id.page_title);
        title.setText("Orders");

        drawerLayout = findViewById(R.id.drawer);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        UID = auth.getUid();

        String order = "ordered";

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(UID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Use the UserInfoClass to map the data from the database
                    AdminViewUserDataClass userInfo = dataSnapshot.getValue(AdminViewUserDataClass.class);

                    if (userInfo != null) {
                        custID = userInfo.getUserID();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });

        orderRecyclerView = findViewById(R.id.OrdersRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));

        FirebaseRecyclerOptions<ViewOrdersHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ViewOrdersHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("cart").child("adminView").child(currentUser.getUid()).child("orders").orderByChild("admin").startAt(order).endAt(order), ViewOrdersHelperClass.class)
                        .build();

        mainAdapter = new OrdersActivityAdapter(options);
        orderRecyclerView.setAdapter(mainAdapter);
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
        redirectActivity(OrdersActivity.this, UserHomepage.class);
    }
    //calls function redirectActivity for intent
    public void ClickMenu(View view){
        redirectActivity(OrdersActivity.this, MenuActivity.class);
    }
    //calls function redirectActivity for intent
    public void ClickNotifications(View view){
        redirectActivity(OrdersActivity.this, NotificationsActivity.class);
    }
    public void ClickMyAccount(View view){
        redirectActivity(OrdersActivity.this, AccountActivity.class);
    }
    public void ClickCart(View view){
        redirectActivity(OrdersActivity.this, CartActivity.class);
    }
    public void ClickOrders(View view){redirectActivity(OrdersActivity.this, OrdersActivity.class);}
    //function redirectActivity that is called when nav option is clicked
    public static void redirectActivity(Activity activity, Class Class){
        Intent intent = new Intent(activity,Class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
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
    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
}