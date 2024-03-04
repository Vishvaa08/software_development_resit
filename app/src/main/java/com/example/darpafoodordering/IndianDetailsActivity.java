package com.example.darpafoodordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class IndianDetailsActivity extends AppCompatActivity {

    TextView menuName, menuPrice, menuQuantity;
    ImageView menuPic;
    Button btnAdd, btnMinus, btnAddCart;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    String UID, custName, custID;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);

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
                        custID = userInfo.getUserID();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });

        drawerLayout = findViewById(R.id.drawer);
        menuName = findViewById(R.id.menuName);
        menuPrice = findViewById(R.id.menuPrice);
        menuPic = findViewById(R.id.menuPic);
        menuQuantity = findViewById(R.id.quantity);
        btnAdd = findViewById(R.id.addOn);
        btnMinus = findViewById(R.id.delete);
        btnAddCart = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int plus = 1;
                int newValue;
                int s = Integer.parseInt(menuQuantity.getText().toString());

                newValue = s + plus;
                String value = String.valueOf(newValue);

                menuQuantity.setText(value);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minus = 1;
                String lowest = String.valueOf(0);
                int newValue;
                int s = Integer.parseInt(menuQuantity.getText().toString());

                newValue = s - minus;
                String value = String.valueOf(newValue);

                if(newValue < 1){
                    menuQuantity.setText(lowest);
                }else{
                    menuQuantity.setText(value);
                }
            }
        });

        retrieveMenuDetails();

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String totalQuantity = menuQuantity.getText().toString();
                int quantity1 = Integer.parseInt(totalQuantity);

                if(quantity1 == 0){
                    Toast.makeText(IndianDetailsActivity.this, "Invalid Quantity!", Toast.LENGTH_SHORT).show();
                }else{
                    AddToCart();
                }
            }
        });

    }
    public void AddToCart(){

        String saveTime, saveDate, saveTimeStamp, status, order;
        int quantity;
        double price, totalPrice;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyyy");
        saveDate = currentDate.format(cal.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveTime = currentTime.format(cal.getTime());

        saveTimeStamp = saveTime + ';' + saveDate;

        status = "pending";
        order = "order";

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("name", menuName.getText().toString());
        cartMap.put("price", menuPrice.getText().toString());
        cartMap.put("quantity", menuQuantity.getText().toString());
        cartMap.put("status", status);
        cartMap.put("admin", order);
        cartMap.put("userID", custID);

        cartListRef.child("userView").child(currentUser.getUid()).child("orders").child(saveTimeStamp)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartListRef.child("adminView").child(currentUser.getUid()).child("orders").child(saveTimeStamp)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(IndianDetailsActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(IndianDetailsActivity.this, MenuActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
    public void retrieveMenuDetails(){
        Intent intent = getIntent();
        String val = intent.getStringExtra("name");

        DatabaseReference menu = FirebaseDatabase.getInstance().getReference().child("Menu").child("Indian");
        menu.child(val).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ViewUserMenuHelperClass menuItems = dataSnapshot.getValue(ViewUserMenuHelperClass.class);

                    menuName.setText(menuItems.getName());
                    menuPrice.setText(menuItems.getPrice());
                    Picasso.get().load(menuItems.getImage()).into(menuPic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        redirectActivity(IndianDetailsActivity.this, UserHomepage.class);
    }
    //calls function redirectActivity for intent
    public void ClickMenu(View view){
        redirectActivity(IndianDetailsActivity.this, MenuActivity.class);
    }
    //calls function redirectActivity for intent
    public void ClickNotifications(View view){
        redirectActivity(IndianDetailsActivity.this, NotificationsActivity.class);
    }
    public void ClickMyAccount(View view){
        redirectActivity(IndianDetailsActivity.this, AccountActivity.class);
    }
    public void ClickCart(View view){
        redirectActivity(IndianDetailsActivity.this, CartActivity.class);
    }
    public void ClickOrders(View view){redirectActivity(IndianDetailsActivity.this, OrdersActivity.class);}
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