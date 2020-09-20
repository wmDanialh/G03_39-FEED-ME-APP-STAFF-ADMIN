package com.danny.feedmeappadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Model.Staff;
import com.google.firebase.database.DatabaseReference;

public class StaffAddAddressActivity extends AppCompatActivity {

    EditText labelHome, labelAddress;
    Button btnAddressSave;
    Staff userAddress;
    DatabaseReference reff;

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

        labelAddress = (EditText)findViewById(R.id.labelAddress);
        labelHome = (EditText)findViewById(R.id.labelHome);

        btnAddressSave = (Button)findViewById(R.id.btnSaveAddress);

        btnAddressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StaffAddAddressActivity.this,"Address Added Successfuly",Toast.LENGTH_LONG).show();
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
