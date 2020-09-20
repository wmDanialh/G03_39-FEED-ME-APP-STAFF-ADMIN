package com.danny.feedmeappadminstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class AdminChangePasswordActivity extends AppCompatActivity {

    Button btnSavePass;
    EditText newPassword, confPassword;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_password);

        Toolbar toolbar = findViewById(R.id.toolbarAdminPassword);
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

        newPassword = findViewById(R.id.adminnewPass);
        btnSavePass = findViewById(R.id.btnSavePassAdmin);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userPasswordNew = newPassword.getText().toString();
                final String userConfirmNew= confPassword.getText().toString();

                firebaseUser.updatePassword(userPasswordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminChangePasswordActivity.this,"Password Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminChangePasswordActivity.this, StaffViewProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(AdminChangePasswordActivity.this,"Password Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}