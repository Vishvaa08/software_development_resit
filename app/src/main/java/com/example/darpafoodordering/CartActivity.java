package com.example.darpafoodordering;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    TextView totalLastCost;
    Button btnCheckout;
    RecyclerView cartRecyclerView;
    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    String UID, custName;
    ViewCartAdapter mainAdapter;
    int lastTotalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Cart");

        drawerLayout = findViewById(R.id.drawer);
        totalLastCost = findViewById(R.id.totalCost);
        btnCheckout = findViewById(R.id.btnCheckout);

        String pending = "pending";

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        UID = auth.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(UID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Use the UserInfoClass to map the data from the database
                    AdminViewUserDataClass userInfo = dataSnapshot.getValue(AdminViewUserDataClass.class);

                    if (userInfo != null) {
                        custName = userInfo.getName();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });


        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String totalCost = intent.getStringExtra("total");
                totalLastCost.setText(totalCost);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("perItem"));



        cartRecyclerView = findViewById(R.id.CartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        FirebaseRecyclerOptions<ViewCartHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ViewCartHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("cart").child("userView").child(currentUser.getUid()).child("orders").orderByChild("status").startAt(pending).endAt(pending), ViewCartHelperClass.class)
                        .build();

        mainAdapter = new ViewCartAdapter(options);
        cartRecyclerView.setAdapter(mainAdapter);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("total", totalLastCost.getText().toString());
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
        redirectActivity(CartActivity.this, UserHomepage.class);
    }
    //calls function redirectActivity for intent
    public void ClickMenu(View view){
        redirectActivity(CartActivity.this, MenuActivity.class);
    }
    //calls function redirectActivity for intent
    public void ClickNotifications(View view){
        redirectActivity(CartActivity.this, NotificationsActivity.class);
    }
    public void ClickMyAccount(View view){
        redirectActivity(CartActivity.this, AccountActivity.class);
    }
    public void ClickCart(View view){
        redirectActivity(CartActivity.this, CartActivity.class);
    }
    public void ClickOrders(View view){redirectActivity(CartActivity.this, OrdersActivity.class);}
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