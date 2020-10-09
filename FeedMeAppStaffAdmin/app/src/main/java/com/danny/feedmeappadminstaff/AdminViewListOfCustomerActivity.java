package com.danny.feedmeappadminstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Holder.CustomerViewHolder;
import com.danny.feedmeappadminstaff.Holder.StaffViewHolder;
import com.danny.feedmeappadminstaff.Interface.ItemClickListener;
import com.danny.feedmeappadminstaff.Model.Customer;
import com.danny.feedmeappadminstaff.Model.Staff;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminViewListOfCustomerActivity extends AppCompatActivity {

    DatabaseReference category;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    List<Customer> usersList;

    private FirebaseAuth firebaseAuth;

    FirebaseRecyclerAdapter<Customer, CustomerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_list_of_customer);

        Toolbar toolbarProfile = findViewById(R.id.toolbarListOfCustomer);
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setTitle("List of Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbarProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Load Menu
        recycler_menu = (RecyclerView)findViewById(R.id.customer_recyclerView);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        firebaseAuth = FirebaseAuth.getInstance();

        usersList = new ArrayList<>();

        loadUser();


    }

    private void loadUser() {

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseRecyclerOptions<Customer> options = new FirebaseRecyclerOptions.Builder<Customer>().setQuery(ref, Customer.class).build();

        adapter = new FirebaseRecyclerAdapter<Customer, CustomerViewHolder>(options) {

            @Override
            public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position, @NonNull Customer model) {


                holder.mCustomerNumberTv.setText(model.getUserMobile());
                holder.mCustomerNameTv.setText(model.getUserName());
                holder.mCustomerEmailTv.setText(model.getUserEmail());
                //Picasso.with(getBaseContext()).load(model.getStaffImage()).fit().into(holder.mAvatarIv);

                final Customer clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get Category Id and send to new activity
                        //Because CategoryId is key, so we just get key of this item
                        Toast.makeText(AdminViewListOfCustomerActivity.this, "" + clickItem.getUserName(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @NonNull
            @Override
            public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer, parent, false);
                return new CustomerViewHolder(view);
            }
        };
        adapter.startListening();
        recycler_menu.setAdapter(adapter);
    }

}