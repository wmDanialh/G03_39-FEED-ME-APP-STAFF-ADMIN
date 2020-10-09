package com.danny.feedmeappadminstaff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danny.feedmeappadminstaff.Common.Common;
import com.danny.feedmeappadminstaff.Holder.MenuViewHolder;
import com.danny.feedmeappadminstaff.Interface.ItemClickListener;
import com.danny.feedmeappadminstaff.Model.FoodCategoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AdminMenuManagementActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference category;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    EditText edtName;
    Button select,upload;

    FoodCategoryModel foodCategoryModel;

    Uri saveUri;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<FoodCategoryModel, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu_management);

        FloatingActionButton floatingActionButton = findViewById(R.id.btnEditCategory);

        Toolbar toolbarProfile = findViewById(R.id.toolbarExplorer);
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setTitle("Menu Management");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbarProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("categories");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Load Menu
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_Menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();


    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminMenuManagementActivity.this);
        alertDialog.setTitle("Add New Category");
        alertDialog.setMessage("Please fulfill information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        select = add_menu_layout.findViewById(R.id.btnSelect);
        upload = add_menu_layout.findViewById(R.id.btnUpload);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });


        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_restaurant_menu_24);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(foodCategoryModel != null)
                {
                    category.push().setValue(foodCategoryModel);
                    Toast.makeText(AdminMenuManagementActivity.this,"New Category " +foodCategoryModel.getName()+ "has been added", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void uploadImage() {
        if(saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(AdminMenuManagementActivity.this,"Picture are uploaded", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new category if image upload and we can get download link
                                    foodCategoryModel = new FoodCategoryModel(edtName.getText().toString(),uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(AdminMenuManagementActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded " +progress+"%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            saveUri = data.getData();
            select.setText("Image Selected");
        }
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select Image"),Common.PICK_IMAGE_REQUEST);

    }

    private void loadMenu() {
        FirebaseRecyclerOptions<FoodCategoryModel> options = new FirebaseRecyclerOptions.Builder<FoodCategoryModel>().setQuery(category, FoodCategoryModel.class).build();

        adapter = new FirebaseRecyclerAdapter<FoodCategoryModel, MenuViewHolder>(options) {

            @Override
            public void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull FoodCategoryModel model) {
                holder.txtMenuName.setText(model.getName());
                Picasso.with(AdminMenuManagementActivity.this).load(model.getImage()).fit().into(holder.imageView);

                final FoodCategoryModel clickItem = model;

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodlist = new Intent(AdminMenuManagementActivity.this, AdminMenuFoodsManagementActivity.class);
                        foodlist.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodlist);
                        //Because CategoryId is key, so we just get key of this item
                        Toast.makeText(AdminMenuManagementActivity.this, "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(view);
            }
        };
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals(Common.DELETE))
        {
            deleteCategory(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        category.child(key).removeValue();
        Toast.makeText(AdminMenuManagementActivity.this,"Category hase been deleted",Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final FoodCategoryModel item) {

        //Just copy code from show
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminMenuManagementActivity.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please fulfill information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        select = add_menu_layout.findViewById(R.id.btnSelect);
        upload = add_menu_layout.findViewById(R.id.btnUpload);

        //Set default name
        edtName.setText(item.getName());

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage(item);
            }
        });


        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_restaurant_menu_24);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                item.setName(edtName.getText().toString());
                category.child(key).setValue(item);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }

    private void changeImage(final FoodCategoryModel item) {
        if(saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(AdminMenuManagementActivity.this,"Picture are uploaded", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new category if image upload and we can get download link
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(AdminMenuManagementActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded " +progress+"%");
                        }
                    });
        }
    }

}