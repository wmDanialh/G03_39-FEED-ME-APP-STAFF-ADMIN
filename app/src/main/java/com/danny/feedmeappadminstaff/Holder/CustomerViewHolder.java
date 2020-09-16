package com.danny.feedmeappadminstaff.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danny.feedmeappadminstaff.Interface.ItemClickListener;
import com.danny.feedmeappadminstaff.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public CircleImageView mCustomerAvatarIv;
    public TextView mCustomerNameTv, mCustomerEmailTv, mCustomerNumberTv;

    private ItemClickListener itemClickListener;

    public CustomerViewHolder(@NonNull View itemView) {
        super(itemView);

        mCustomerAvatarIv = itemView.findViewById(R.id.customerImg);
        mCustomerNameTv = itemView.findViewById(R.id.nameCustomer);
        mCustomerEmailTv = itemView.findViewById(R.id.emailCustomer);
        mCustomerNumberTv = itemView.findViewById(R.id.numberCustomer);

        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

}
