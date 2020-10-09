package com.danny.feedmeappadminstaff.Holder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danny.feedmeappadminstaff.Interface.ItemClickListener;
import com.danny.feedmeappadminstaff.R;

    public class StaffOrderViewHolder extends RecyclerView.ViewHolder{

        public TextView txtOrderId, txtOrderStatus, txtOrderAddress, txtOrderPhone;

        public TextView stafftoviewtxtOrderId, stafftoviewtxtOrderStatus, stafftoviewtxtOrderAddress, stafftoviewtxtOrderPhone;

        public Button btnEdit, btnRemove,btnDetail,btnDirection;

        private ItemClickListener itemClickListener;

        public StaffOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderAddress = (TextView) itemView.findViewById(R.id.Staff_order_address);
            txtOrderStatus = (TextView) itemView.findViewById(R.id.Staff_order_status);
            txtOrderId = (TextView) itemView.findViewById(R.id.Staff_order_id);
            txtOrderPhone = (TextView) itemView.findViewById(R.id.Staff_order_phone);

            stafftoviewtxtOrderAddress = (TextView) itemView.findViewById(R.id.staff_order_address_toview);
            stafftoviewtxtOrderStatus = (TextView) itemView.findViewById(R.id.staff_order_status_toview);
            stafftoviewtxtOrderId = (TextView) itemView.findViewById(R.id.staff_order_id_toView);
            stafftoviewtxtOrderPhone = (TextView) itemView.findViewById(R.id.staff_order_phone_toview);

            btnEdit = (Button)itemView.findViewById(R.id.btnStaffEdit);
            btnRemove = (Button)itemView.findViewById(R.id.btnStaffRemove);
            btnDetail = (Button)itemView.findViewById(R.id.btnStaffDetail);


        }
    }
