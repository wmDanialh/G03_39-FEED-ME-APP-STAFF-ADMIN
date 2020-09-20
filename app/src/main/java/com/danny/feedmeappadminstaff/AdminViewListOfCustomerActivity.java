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

import com.danny.feedmeappadminstaff.Holder.CustomerViewHolder;
import com.danny.feedmeappadminstaff.Model.Customer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminViewListOfCustomerActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference category;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    Customer customer;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<Customer, CustomerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_list_of_customer);

        Toolbar toolbarProfile = findViewById(R.id.toolbarListOfCustomer);
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setTitle("View List of Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbarProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        database = FirebaseDatabase.getInstance();
        category = database.getReference("User");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Load Menu
        recycler_menu = (RecyclerView)findViewById(R.id.customer_recyclerView);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadCustomer();


    }

    private void loadCustomer() {

        FirebaseRecyclerOptions<Customer> options = new FirebaseRecyclerOptions.Builder<Customer>().setQuery(category, Customer.class).build();

        adapter = new FirebaseRecyclerAdapter<Customer, CustomerViewHolder>(options) {

            @Override
            public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position, @NonNull Customer model) {

                holder.mCustomerNameTv.setText(model.getUserName());
                holder.mCustomerEmailTv.setText(model.getUserEmail());
                holder.mCustomerNumberTv.setText(model.getUserMobile());

            }

            @NonNull
            @Override
            public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer, parent, false);
                return new CustomerViewHolder(view);
            }
        };
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
        adapter.startListening();

    }
}