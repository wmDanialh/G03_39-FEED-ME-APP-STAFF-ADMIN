package com.danny.feedmeappadminstaff;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AdminMainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private NavigationView navigationView;

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);

        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        //Init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //DatabaseReference databaseReference = firebaseDatabase.getReference("User").child(firebaseAuth.getUid());

        //textFullName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.fullnameHead);

        DrawerLayout drawer = findViewById(R.id.admin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.adminOrder_drawer_explorer:
                        Intent intent1 = new Intent(AdminMainScreenActivity.this, AdminCustomerOrdersActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.adminMenuManagement_drawer_myOrder:
                        Intent intent2 = new Intent(AdminMainScreenActivity.this,MenuManagementActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.adminStaffMangement_drawer_myOrderStatus:
                        Intent intent = new Intent(AdminMainScreenActivity.this, ViewListOfStaffActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.adminProfile_drawer_myProfile:
                        break;
                    case R.id.adminViewCustomer_drawer_myAddresses:
                        break;
                    case R.id.staff_drawer_myHelp:
                        break;
                    case R.id.staff_drawer_item_settings:
                        break;
                    case R.id.staff_drawer_item_tAndC:
                        break;
                    case R.id.staff_drawer_items_logout:
                        logOut();
                        return true;
                }
                return false;
            }
        });


        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //textFullName.setText(dataSnapshot.child("userName").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminMainScreenActivity.this, databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

         */

    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Log Out");
        builder.setMessage("Are you sure you would like to log out the app?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                firebaseAuth.signOut();
                startActivity(new Intent(AdminMainScreenActivity.this,LoginActivity.class));
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
        if(mFirebaseUser != null){
            startActivity(new Intent(this,AdminMainScreenActivity.class));
            finish();
        }
        else {

        }
    }

     */

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}