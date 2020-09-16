package com.danny.feedmeappadminstaff.Holder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danny.feedmeappadminstaff.R;

public class AdminOrderViewHolder extends RecyclerView.ViewHolder{

    public TextView txtOrderId, txtOrderStatus, txtOrderAddress,txtOrderPhone;
    public Button btnEdit, btnRemove,btnDetail,btnDirection;


    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress = (TextView)itemView.findViewById(R.id.Admin_order_address);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.Admin_order_status);
        txtOrderId = (TextView)itemView.findViewById(R.id.Admin_order_id);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.Admin_order_phone);

        btnEdit = (Button)itemView.findViewById(R.id.btnAdminEdit);
        btnRemove = (Button)itemView.findViewById(R.id.btnAdminRemove);
        btnDetail = (Button)itemView.findViewById(R.id.btnAdminDetail);
        btnDirection = (Button)itemView.findViewById(R.id.btnAdminDirection);
    }
}
