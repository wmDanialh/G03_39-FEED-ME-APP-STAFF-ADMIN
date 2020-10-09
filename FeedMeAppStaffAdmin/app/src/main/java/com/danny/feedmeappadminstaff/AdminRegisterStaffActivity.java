package com.danny.feedmeappadminstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Model.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminRegisterStaffActivity extends AppCompatActivity {

    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri ;
    private ProgressDialog progressDialog;

    private EditText staffName, staffPassword, staffEmail, staffMobile, staffAddress;
    private Button regStaffButton;

    private FirebaseAuth firebaseAuth;
    FirebaseAuth firebaseAuthForStaff;
    FirebaseUser firebaseUser;

    String email, name, password, mobile, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register_staff);

        Toolbar toolbar = findViewById(R.id.toolbarRegisterStaff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register Staff");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseAuthForStaff = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        regStaffButton = findViewById(R.id.btnRegisterStaff);
        staffAddress = findViewById(R.id.etStaffAddress);
        staffPassword = findViewById(R.id.etStaffPassword);
        staffEmail = findViewById(R.id.etStaffEmail);
        staffName = findViewById(R.id.etStaffName);
        staffMobile = findViewById(R.id.etStaffMobile);

        regStaffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.show();

                    String user_email = staffEmail.getText().toString().trim();
                    String user_password = staffPassword.getText().toString().trim();

                    firebaseAuthForStaff.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUserData();
                                progressDialog.dismiss();
                                Toast.makeText(AdminRegisterStaffActivity.this, "New Staff Account has been created!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AdminRegisterStaffActivity.this, StaffMainScreenActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(AdminRegisterStaffActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                Toast.makeText(AdminRegisterStaffActivity.this, "Staff Account cannot be create", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
    // simple method to show toast message

    private Boolean validate(){
        Boolean result = false;

        name = staffName.getText().toString();
        password = staffPassword.getText().toString();
        email = staffEmail.getText().toString();
        mobile = staffMobile.getText().toString();
        address = staffAddress.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || mobile.isEmpty() || address.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            takeInput();
        }else{
            result = true;
        }

        return result;
    }

    private void takeInput() {
        String name = staffName.getText().toString();
        String password = staffPassword.getText().toString();
        String email = staffEmail.getText().toString();
        String mobile = staffMobile.getText().toString();
        String address = staffAddress.getText().toString();
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("Staff").child(firebaseAuthForStaff.getUid());
        Staff userProfile = new Staff(address,email, mobile, name);
        myRef.setValue(userProfile);
    }
}