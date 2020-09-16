package com.danny.feedmeappadminstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.danny.feedmeappadminstaff.Common.Common;
import com.danny.feedmeappadminstaff.Holder.AdminOrderViewHolder;
import com.danny.feedmeappadminstaff.Interface.ItemClickListener;
import com.danny.feedmeappadminstaff.Model.Order;
import com.danny.feedmeappadminstaff.Model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class AdminCustomerOrdersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, AdminOrderViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;

    MaterialSpinner materialSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_orders);

        Toolbar toolbar = findViewById(R.id.toolbarAdminOrderStatus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customer Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("requests");

        //init
        recyclerView = (RecyclerView)findViewById(R.id.listAdminOrders);
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

                holder.txtOrderId.setText(adapter.getRef(position).getKey());
                holder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                holder.txtOrderAddress.setText(model.getAddress());
                holder.txtOrderPhone.setText(model.getPhone());

                holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUpdateDialog(adapter.getRef(position).getKey(),adapter.getItem(position));
                    }
                });

                holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteOrder(adapter.getRef(position).getKey());

                    }
                });

                holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent orderDetail = new Intent(AdminCustomerOrdersActivity.this, OrderDetailActivity.class);
                        Common.currentRequest = model;
                        orderDetail.putExtra("OrderId",adapter.getRef(position).getKey());
                        startActivity(orderDetail);
                    }
                });

                holder.btnDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_layout, parent, false);
                return new AdminOrderViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminCustomerOrdersActivity.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.admin_update_order_layout,null);

        materialSpinner = (MaterialSpinner)view.findViewById(R.id.statusAdminSpinner);
        materialSpinner.setItems("Placed","On My Way","Shipped");

        alertDialog.setView(view);

        final String localkey = key;
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(materialSpinner.getSelectedIndex()));

                requests.child(localkey).setValue(item);
                adapter.notifyDataSetChanged();

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.show();

    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }
}
