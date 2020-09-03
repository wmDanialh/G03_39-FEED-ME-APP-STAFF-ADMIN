package com.danny.feedmeappadminstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Adapter.AdapterStaff;
import com.danny.feedmeappadminstaff.Holder.StaffViewHolder;
import com.danny.feedmeappadminstaff.Interface.ItemClickListener;
import com.danny.feedmeappadminstaff.Model.Staff;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewListOfStaffActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<Staff> usersList;

    FirebaseRecyclerAdapter<Staff, StaffViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_of_staff);

        Toolbar toolbar = findViewById(R.id.toolbarListOfStaff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Of Staff");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab = findViewById(R.id.btnAddStaff);
        recyclerView = findViewById(R.id.staff_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersList = new ArrayList<>();

        //getAllUsers();
        loadUser();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewListOfStaffActivity.this,RegisterStaffActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getAllUsers() {

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Staff");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Staff staff = ds.getValue(Staff.class);

                    if(!staff.getUid().equals(fUser.getUid()))
                        usersList.add(staff);
                }
                //adapterStaff = new AdapterStaff(this,usersList);
                //recyclerView.setAdapter(adapterStaff);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadUser() {

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Staff");

        FirebaseRecyclerOptions<Staff> options = new FirebaseRecyclerOptions.Builder<Staff>().setQuery(ref, Staff.class).build();

        adapter = new FirebaseRecyclerAdapter<Staff, StaffViewHolder>(options) {

            @Override
            public void onBindViewHolder(@NonNull StaffViewHolder holder, int position, @NonNull Staff model) {
                /*
                holder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).fit().into(holder.imageView);

                final Staff clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get Category Id and send to new activity
                        //Because CategoryId is key, so we just get key of this item
                        Toast.makeText(ViewListOfStaffActivity.this, "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

                 */
            }
            @NonNull
            @Override
            public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_staff, parent, false);
                return new StaffViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}