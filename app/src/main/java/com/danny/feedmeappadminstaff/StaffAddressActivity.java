package com.danny.feedmeappadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Model.Staff;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StaffAddressActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FloatingActionButton floatingActionButton;
    TextView sstaff_address;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_address);

        Toolbar toolbarProfile = findViewById(R.id.toolbarAddress);
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setTitle("My Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sstaff_address = findViewById(R.id.staff_address);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        floatingActionButton = (FloatingActionButton)findViewById(R.id.add_address);

        DatabaseReference databaseReference = firebaseDatabase.getReference("Staff").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Staff userProfile = dataSnapshot.getValue(Staff.class);
                sstaff_address.setText(userProfile.getStaffAddress());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StaffAddressActivity.this, databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffAddressActivity.this, StaffAddAddressActivity.class);
                startActivity(intent);
            }
        });

        toolbarProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}