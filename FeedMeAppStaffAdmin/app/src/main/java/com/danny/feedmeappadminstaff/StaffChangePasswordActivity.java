package com.danny.feedmeappadminstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class StaffChangePasswordActivity extends AppCompatActivity {

    Button btnSavePass;
    EditText newPassword, confPassword;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_change_password);

        Toolbar toolbar = findViewById(R.id.toolbarStaffPassword);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Staff Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setTitleTextColor(getResources().getColor(android.R.color.holo_red_dark));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog =new ProgressDialog(this);

        newPassword = findViewById(R.id.staffnewPass);
        btnSavePass = findViewById(R.id.btnSavePassStaff);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Password is updating..");
                progressDialog.show();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user!=null){
                    user.updatePassword(newPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Your Password has been changes ",Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                        finish();
                                        Intent intent = new Intent(StaffChangePasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(),"Please login as your new password to update ",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Password Update Failed ",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });

    }
}