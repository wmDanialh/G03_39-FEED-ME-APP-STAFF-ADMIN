package com.danny.feedmeappadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Model.Staff;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StaffAddAddressActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    EditText labelHome, labelAddress;
    Button btnAddressSave;
    Staff userAddress;
    private FirebaseUser firebaseUser;
    DatabaseReference reff;

    private ProgressDialog progressDialog;

    String address, label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_add_address);

        Toolbar toolbarProfile = findViewById(R.id.toolbarUserAddAddress);
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setTitle("Add a new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbarProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        labelAddress = (EditText)findViewById(R.id.labelAddress);
        btnAddressSave = (Button)findViewById(R.id.btnSaveAddress);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);

        final DatabaseReference databaseReference = firebaseDatabase.getReference("Staff").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Staff userProfile = dataSnapshot.getValue(Staff.class);
                labelAddress.setText(userProfile.getStaffAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StaffAddAddressActivity.this, databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        btnAddressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(labelAddress.getText().toString().isEmpty()){
                    Toast.makeText(StaffAddAddressActivity.this,"Address Failed to Update",Toast.LENGTH_LONG).show();
                }
                else {
                    String address = labelAddress.getText().toString();

                    progressDialog.setMessage("Address is updating..");
                    progressDialog.show();

                    Staff userProfile = new Staff(address);
                    databaseReference.setValue(userProfile);

                    Toast.makeText(StaffAddAddressActivity.this,"Address Edited Successfully",Toast.LENGTH_LONG).show();
                }



            }
        });



    }
}
