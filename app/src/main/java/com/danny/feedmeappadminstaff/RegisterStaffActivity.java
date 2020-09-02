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
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Model.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class RegisterStaffActivity extends AppCompatActivity {

    CircleImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri ;

    private EditText staffName, staffPassword, staffEmail, staffMobile, staffAddress;
    private Button regStaffButton;

    private FirebaseAuth firebaseAuth;

    String email, name, password, mobile, address, image, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_staff);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

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

        regStaffButton = findViewById(R.id.btnRegisterStaff);
        staffAddress = findViewById(R.id.etStaffAddress);
        staffPassword = findViewById(R.id.etStaffPassword);
        staffEmail = findViewById(R.id.etStaffEmail);
        staffName = findViewById(R.id.etStaffName);
        staffMobile = findViewById(R.id.etStaffMobile);

        regStaffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    //Upload data to the database
                    String staff_name = staffName.getText().toString();
                    String staff_email = staffEmail.getText().toString().trim();
                    String staff_password = staffPassword.getText().toString().trim();
                    String staff_address = staffAddress.getText().toString();
                    String staff_mobile = staffMobile.getText().toString();

                    if( staff_name.isEmpty() || staff_email.isEmpty() || staff_password.isEmpty()  || staff_address.isEmpty() || staff_mobile.isEmpty()) {
                        // something goes wrong : all fields must be filled
                        // we need to display an error message
                        Toast.makeText(RegisterStaffActivity.this,"Please Verify all fields",Toast.LENGTH_SHORT).show();
                        //regBtn.setVisibility(View.VISIBLE);
                        //loadingProgress.setVisibility(View.INVISIBLE);
                    }
                    else {
                        // everything is ok and all fields are filled now we can start creating user account
                        // CreateUserAccount method will try to create the user if the email is valid
                        CreateUserAccount(email,name,password);
                    }

                }

            }
        });
        ImgUserPhoto = findViewById(R.id.imgRegisterStaff) ;

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                }
                else
                {
                    openGallery();
                }

            }
        });
    }
    private void CreateUserAccount(String email, final String name, String password) {
        // this method create user account with specific email and password

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // user account created successfully
                            showMessage("Account created");
                            // after we created user account we need to update his profile picture and name
                            updateUserInfo(name, pickedImgUri, firebaseAuth.getCurrentUser());
                            sendUserData();
                        } else {
                            // account creation failed
                            showMessage("account creation failed" + task.getException().getMessage());
                            //regBtn.setVisibility(View.VISIBLE);
                            //loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Staff_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded succesfully
                // now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contain user image url
                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            showMessage("Register Complete");
                                            updateUI();
                                        }
                                    }
                                });

                    }
                });

            }
        });


    }

    private void updateUI() {
        //Intent homeActivity = new Intent(getApplicationContext(),AdminMainScreenActivity.class);
        //startActivity(homeActivity);
        finish();
    }

    // simple method to show toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(RegisterStaffActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterStaffActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegisterStaffActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(RegisterStaffActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);
        }
    }

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
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("Staff").child(firebaseAuth.getUid());
        Staff userProfile = new Staff(name, email, mobile ,address, image, uid);
        myRef.setValue(userProfile);
    }


}