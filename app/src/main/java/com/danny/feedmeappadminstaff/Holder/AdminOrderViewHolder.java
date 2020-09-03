package com.danny.feedmeappadminstaff.Holder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danny.feedmeappadminstaff.Interface.ItemClickListener;
import com.danny.feedmeappadminstaff.R;

public class AdminOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView txtOrderId, txtOrderStatus, txtOrderAddress,txtOrderPhone;

    private ItemClickListener itemClickListener;

    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress = (TextView)itemView.findViewById(R.id.Admin_order_address);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.Admin_order_status);
        txtOrderId = (TextView)itemView.findViewById(R.id.Admin_order_id);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.Admin_order_phone);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.add(0,0,getAdapterPosition(),"Update");
        contextMenu.add(0, 1,getAdapterPosition(),"Delete");
    }
}
