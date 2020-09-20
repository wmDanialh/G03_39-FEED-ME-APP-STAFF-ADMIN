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
import com.danny.feedmeappadminstaff.Model.Request;
import com.danny.feedmeappadminstaff.Model.Staff;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminMainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private NavigationView navigationView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase db;
    DatabaseReference requests;

    TextView textFullName, getTextFullName;

    FirebaseRecyclerAdapter<Request, AdminOrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);

        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("Admin").child(firebaseAuth.getUid());

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
                        Intent intent2 = new Intent(AdminMainScreenActivity.this, AdminMenuManagementActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.adminStaffMangement_drawer_myOrderStatus:
                        Intent intent3 = new Intent(AdminMainScreenActivity.this, AdminViewListOfStaffActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.adminProfile_drawer_myProfile:
                        Intent intent5 = new Intent(AdminMainScreenActivity.this, AdminViewProfileActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.adminViewCustomer_drawers:
                        Intent intent4 = new Intent(AdminMainScreenActivity.this, AdminViewListOfCustomerActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.admin_drawer_item_settings:
                        Intent intent7 = new Intent(AdminMainScreenActivity.this, AdminStaffSettingsActivity.class);
                        startActivity(intent7);
                        break;
                    case R.id.admin_drawer_item_tAndC:
                        Intent intent6 = new Intent(AdminMainScreenActivity.this,AdminStaffTermsAndConditions.class);
                        startActivity(intent6);
                        break;
                    case R.id.admin_drawer_items_logout:
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

                textFullName = headerView.findViewById(R.id.fullnameHead);
                getTextFullName = (TextView)findViewById(R.id.name);

                textFullName.setText(userProfile.getStaffName());
                getTextFullName.setText(userProfile.getStaffName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminMainScreenActivity.this, databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("requests");

        //init
        recyclerView = (RecyclerView)findViewById(R.id.listAdminOrdersAtAdminMainScreen);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadCustomerOrders();

    }

    private void loadCustomerOrders() {
        Query searchByPhone = requests.orderByChild("phone");
        // create options with query
        FirebaseRecyclerOptions<Request> foodOptions = new FirebaseRecyclerOptions.Builder<Request>().setQuery(searchByPhone, Request.class).build();

        adapter = new FirebaseRecyclerAdapter<Request, AdminOrderViewHolder>(foodOptions) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final Request model) {

                holder.admintoViewtxtOrderId.setText(adapter.getRef(position).getKey());
                holder.admintoViewtxtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                holder.admintoViewtxtOrderAddress.setText(model.getAddress());
                holder.admintoViewtxtOrderPhone.setText(model.getPhone());
            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_toview_order, parent, false);
                return new AdminOrderViewHolder(itemView);
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