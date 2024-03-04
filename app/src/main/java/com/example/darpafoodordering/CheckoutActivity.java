package com.example.darpafoodordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    TextView total, username, email, UID, status, role, phone, age, credit;
    Button btnConfirmPayment;
    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        pageTitle.setText("Checkout");

        Intent intent = getIntent();
        String total1 = intent.getStringExtra("total");

        auth = FirebaseAuth.getInstance();
        total = findViewById(R.id.total);
        username = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        UID = findViewById(R.id.UID);
        status = findViewById(R.id.status);
        role = findViewById(R.id.role);
        phone = findViewById(R.id.phone);
        age = findViewById(R.id.age);
        credit = findViewById(R.id.credit);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        drawerLayout = findViewById(R.id.drawer);

        total.setText(total1);

        user = auth.getCurrentUser();
        userID = auth.getUid();

        if(user == null){
            Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent1);
            finish();
        }else{
            Log.d("Firebase", "User ID: " + UID);

            // Reference to "User Details" under the current user
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);

            // Add a ValueEventListener to retrieve and update data
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Use the UserInfoClass to map the data from the database
                        AdminViewUserDataClass userInfo = dataSnapshot.getValue(AdminViewUserDataClass.class);

                        if (userInfo != null) {
                            // Update the TextViews with the retrieved data
                            username.setText(userInfo.getName());
                            email.setText(userInfo.getEmail());
                            age.setText(userInfo.getAge());
                            credit.setText(userInfo.getUserCredit());
                            phone.setText(userInfo.getPhoneNum());
                            role.setText(userInfo.getRole());
                            status.setText(userInfo.getStatus());
                            UID.setText(userInfo.getUserID());
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error: " + databaseError.getMessage());
                }
            });
        }
        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userStatus = status.getText().toString();
                double total = Double.valueOf(total1);
                double balance = Double.valueOf(credit.getText().toString());

                if(userStatus.equals("Unverified")){
                    Toast.makeText(CheckoutActivity.this, "You are Unverified!", Toast.LENGTH_SHORT).show();
                }else if(total > balance){
                    Toast.makeText(CheckoutActivity.this, "Insufficient Credit Balance!", Toast.LENGTH_SHORT).show();
                }else{

                    String saveTime, saveDate, saveTimeStamp, custID, custName;
                    custID = UID.getText().toString();
                    custName = username.getText().toString();

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyyy");
                    saveDate = currentDate.format(cal.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    saveTime = currentTime.format(cal.getTime());

                    saveTimeStamp = saveTime + ';' + saveDate;

                    double oldBal = Double.valueOf(credit.getText().toString());
                    double sum = Double.valueOf(total1);
                    double newBal = oldBal - sum;
                    String lastBal = String.valueOf(newBal);

                    DatabaseReference userCreditBalance = FirebaseDatabase.getInstance().getReference().child("users");

                    final HashMap<String, Object> balanceMap = new HashMap<>();
                    balanceMap.put("userCredit", lastBal);

                    userCreditBalance.child(user.getUid()).updateChildren(balanceMap);

                    DatabaseReference orderListRef = FirebaseDatabase.getInstance().getReference().child("orders");
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("cart").child("adminView");

                    String status = "pending";
                    String order = "order";

                    final HashMap<String, Object> orderMap = new HashMap<>();
                    orderMap.put("custName", custName);
                    orderMap.put("userID", custID);
                    orderMap.put("finalTotal", total);

                    orderListRef.child(user.getUid()).child("orders").child(saveTimeStamp)
                            .updateChildren(orderMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FirebaseDatabase.getInstance().getReference().child("cart").child("userView")
                                                .child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(CheckoutActivity.this, "Ordered!", Toast.LENGTH_SHORT).show();

                                                            updateOrderStatus(db, status);
                                                            updateOrder1Status(db, order);

                                                            Intent intent = new Intent(CheckoutActivity.this, UserHomepage.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
    }
    private void updateOrderStatus(DatabaseReference db, String status){
        Query query = db.child(user.getUid()).child("orders").orderByChild("status").equalTo(status);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot orderSnapshot : snapshot.getChildren()){
                    orderSnapshot.child("status").getRef().setValue("processing");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    public void updateOrder1Status(DatabaseReference db, String order){
        Query query = db.child(user.getUid()).child("orders").orderByChild("admin").equalTo(order);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot1) {
                for(DataSnapshot orderSnapshot : snapshot1.getChildren()){
                    orderSnapshot.child("admin").getRef().setValue("ordered");
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
        redirectActivity(CheckoutActivity.this, UserHomepage.class);
    }
    //calls function redirectActivity for intent
    public void ClickMenu(View view){
        redirectActivity(CheckoutActivity.this, MenuActivity.class);
    }
    //calls function redirectActivity for intent
    public void ClickNotifications(View view){
        redirectActivity(CheckoutActivity.this, NotificationsActivity.class);
    }
    public void ClickMyAccount(View view){
        redirectActivity(CheckoutActivity.this, AccountActivity.class);
    }
    public void ClickCart(View view){
        redirectActivity(CheckoutActivity.this, CartActivity.class);
    }
    public void ClickOrders(View view){redirectActivity(CheckoutActivity.this, OrdersActivity.class);}
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