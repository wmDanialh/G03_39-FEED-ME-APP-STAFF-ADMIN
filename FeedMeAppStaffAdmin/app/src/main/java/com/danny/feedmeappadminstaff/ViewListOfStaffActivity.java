package com.danny.feedmeappadminstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Common.Common;
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

    private FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();

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

    private void loadUser() {

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Staff");

        FirebaseRecyclerOptions<Staff> options = new FirebaseRecyclerOptions.Builder<Staff>().setQuery(ref, Staff.class).build();

        adapter = new FirebaseRecyclerAdapter<Staff, StaffViewHolder>(options) {

            @Override
            public void onBindViewHolder(@NonNull StaffViewHolder holder, int position, @NonNull Staff model) {

                holder.mNumberTv.setText(model.getStaffMobile());
                holder.mNameTv.setText(model.getStaffName());
                holder.mEmailTv.setText(model.getStaffEmail());
                //Picasso.with(getBaseContext()).load(model.getStaffImage()).fit().into(holder.mAvatarIv);

                final Staff clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get Category Id and send to new activity
                        //Because CategoryId is key, so we just get key of this item
                        Toast.makeText(ViewListOfStaffActivity.this, "" + clickItem.getStaffName(), Toast.LENGTH_SHORT).show();
                    }
                });


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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
        {
            deleteStaff(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteStaff(String key) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference staff = firebaseDatabase.getReference("Staff").child(firebaseAuth.getUid());
        //Staff userProfile = new Staff(address,email, mobile, name, uid);
        staff.child(key).removeValue();
    }
}