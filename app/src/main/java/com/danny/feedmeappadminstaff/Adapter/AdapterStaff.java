package com.danny.feedmeappadminstaff.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danny.feedmeappadminstaff.Model.Staff;
import com.danny.feedmeappadminstaff.R;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterStaff  {
/*
    Context context;
    List<Staff> staffList;

    //constructor


    public AdapterStaff(ValueEventListener valueEventListener, List<Staff> usersList) {
    }

    public AdapterStaff(Context context, List<Staff> staffList) {
        this.context = context;
        this.staffList = staffList;
    }

    @NonNull
    @Override
    public StaffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //infiate layout (row_users.xml)

        View view = LayoutInflater.from(context).inflate(R.layout.row_staff, parent);

        return new StaffHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffHolder staffholder, int position) {

        String staffImage = staffList.get(position).getStaffImage();
        String staffname = staffList.get(position).getStaffName();
        final String staffEmail = staffList.get(position).getStaffEmail();

        staffholder.mNameTv.setText(staffname);
        staffholder.mEmailTv.setText(staffname);
        try {
            //Picasso.with().load(staffImage).place;
        } catch (Exception e) {
        }

        staffholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + staffEmail, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    //view holder class

 */

}