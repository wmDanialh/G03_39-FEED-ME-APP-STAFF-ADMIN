package com.danny.feedmeappadminstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Model.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AdminEditProfileActivity extends AppCompatActivity {

    private EditText newUserName, newUserEmail, newUserMobile;
    private Button save;
    private ImageView profilePic;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    private static int PICK_IMAGE = 123;
    Uri imagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbarAdminEditProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        newUserName = (EditText)findViewById(R.id.editAdminProfileName);
        newUserEmail = (EditText)findViewById(R.id.editAdminEmail);
        newUserMobile = (EditText)findViewById(R.id.editAdminMobile);
        save = (Button)findViewById(R.id.btnSaveAdmin);
        profilePic = (ImageView)findViewById(R.id.imgAdminEditProfile);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference databaseReference = firebaseDatabase.getReference("Admin").child(firebaseAuth.getUid());

        storageReference = firebaseStorage.getReference();


        //Display Image from Firebase Storage
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(firebaseAuth.getUid()).child("Profile Pic");
        final long ONE_MEGABYTE = 1024 * 1024;

        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                profilePic.setMinimumHeight(dm.heightPixels);
                profilePic.setMinimumWidth(dm.widthPixels);
                profilePic.setImageBitmap(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Staff userProfile = dataSnapshot.getValue(Staff.class);

                newUserName.setText(userProfile.getStaffName());
                newUserEmail.setText(userProfile.getStaffEmail());
                newUserMobile.setText(userProfile.getStaffMobile());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminEditProfileActivity.this, databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = newUserName.getText().toString();
                String email = newUserEmail.getText().toString();
                String mobile = newUserMobile.getText().toString();

                progressDialog.setMessage("Profile Pic is uploading..");
                progressDialog.show();


                Staff userProfile = new Staff(name, email, mobile);
                databaseReference.setValue(userProfile);

                String userEmailNew = newUserEmail.getText().toString();

                firebaseUser.updateEmail(userEmailNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminEditProfileActivity.this,"Email Update", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(AdminEditProfileActivity.this,"Email Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Profile Pic");
                UploadTask uploadTask = imageReference.putFile(imagePath);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminEditProfileActivity.this,"Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminEditProfileActivity.this,"Upload Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminEditProfileActivity.this, AdminMainScreenActivity.class));
                    }
                });

            }
        });

    }
}