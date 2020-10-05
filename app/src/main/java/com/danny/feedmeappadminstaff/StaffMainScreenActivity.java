package com.danny.feedmeappadminstaff;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danny.feedmeappadminstaff.Common.Common;
import com.danny.feedmeappadminstaff.Holder.AdminOrderViewHolder;
import com.danny.feedmeappadminstaff.Holder.StaffOrderViewHolder;
import com.danny.feedmeappadminstaff.Model.Request;
import com.danny.feedmeappadminstaff.Model.Staff;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StaffMainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private NavigationView navigationView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase db;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request, StaffOrderViewHolder> adapter;

    TextView textFullName, getTextFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main_screen);

        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        //Call Service

        //Init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("Staff").child(firebaseAuth.getUid());

        //textFullName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.fullnameHead);

        DrawerLayout drawer = findViewById(R.id.staff_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.staff_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.staffOrder_drawer_explorer:
                        Intent intent = new Intent(StaffMainScreenActivity.this, StaffCustomerOrdersActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.staffProfile_drawer_myProfile:
                        Intent intent2 = new Intent(StaffMainScreenActivity.this, StaffViewProfileActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.staff_drawer_item_settings:
                        Intent intent4 = new Intent(StaffMainScreenActivity.this,AdminStaffSettingsActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.staff_drawer_item_tAndC:
                        Intent intent6 = new Intent(StaffMainScreenActivity.this,AdminStaffTermsAndConditions.class);
                        startActivity(intent6);
                        break;
                    case R.id.staff_drawer_items_logout:
                        logOut();
                        return true;
                }
                return false;
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                View headerView = navigationView.getHeaderView(0);
                Staff userProfile = dataSnapshot.getValue(Staff.class);

                textFullName = headerView.findViewById(R.id.staffNamehead);
                getTextFullName = (TextView)findViewById(R.id.staffName);

                textFullName.setText(userProfile.getStaffName());
                getTextFullName.setText(userProfile.getStaffName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StaffMainScreenActivity.this, databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("requests");

        //init
        recyclerView = (RecyclerView)findViewById(R.id.listStaffOrdersAtStaffMainScreen);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadCustomerOrders();

    }

    private void loadCustomerOrders() {
        Query searchByPhone = requests.orderByChild("phone");
        // create options with query
        FirebaseRecyclerOptions<Request> foodOptions = new FirebaseRecyclerOptions.Builder<Request>().setQuery(searchByPhone, Request.class).build();

        adapter = new FirebaseRecyclerAdapter<Request, StaffOrderViewHolder>(foodOptions) {
            @Override
            protected void onBindViewHolder(@NonNull StaffOrderViewHolder holder, final int position, @NonNull final Request model) {

                holder.stafftoviewtxtOrderId.setText(adapter.getRef(position).getKey());
                holder.stafftoviewtxtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                holder.stafftoviewtxtOrderAddress.setText(model.getAddress());
                holder.stafftoviewtxtOrderPhone.setText(model.getPhone());
            }

            @NonNull
            @Override
            public StaffOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_toview_order, parent, false);
                return new StaffOrderViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Log Out");
        builder.setMessage("Are you sure you would like to log out the app?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                firebaseAuth.signOut();
                startActivity(new Intent(StaffMainScreenActivity.this,LoginActivity.class));
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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
        if(mFirebaseUser != null){

        }
        else {
            startActivity(new Intent(this, StaffMainScreenActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.staff_drawer_layout);
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