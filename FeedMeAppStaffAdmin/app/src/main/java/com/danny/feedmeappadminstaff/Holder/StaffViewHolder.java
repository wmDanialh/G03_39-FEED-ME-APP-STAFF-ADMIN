package com.danny.feedmeappadminstaff.Holder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danny.feedmeappadminstaff.Common.Common;
import com.danny.feedmeappadminstaff.Interface.ItemClickListener;
import com.danny.feedmeappadminstaff.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    public CircleImageView mAvatarIv;
    public TextView mNameTv, mEmailTv, mNumberTv;

    private ItemClickListener itemClickListener;

    public StaffViewHolder(@NonNull View itemView) {
        super(itemView);

        mAvatarIv = itemView.findViewById(R.id.avatarImg);
        mNameTv = itemView.findViewById(R.id.nameStaff);
        mEmailTv = itemView.findViewById(R.id.emailStaff);
        mNumberTv = itemView.findViewById(R.id.numberStaff);

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
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}


