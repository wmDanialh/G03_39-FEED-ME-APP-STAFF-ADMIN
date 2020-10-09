package com.danny.feedmeappadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.danny.feedmeappadminstaff.Common.Common;
import com.danny.feedmeappadminstaff.Holder.OrderDetailAdapter;

public class OrderDetailActivity extends AppCompatActivity {

    TextView order_id, order_phone, order_address, order_total, order_comment;
    String order_id_value;
    RecyclerView listfoods;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id = (TextView)findViewById(R.id.Admin_order_info_id);
        order_phone = (TextView)findViewById(R.id.Admin_order_phone);
        order_address = (TextView)findViewById(R.id.Admin_order_info_address);
        order_total = (TextView)findViewById(R.id.Admin_order_info_total);
        order_comment = (TextView)findViewById(R.id.Admin_order_info_comment);

        listfoods = (RecyclerView)findViewById(R.id.listFoodAdminDetails);
        listfoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listfoods.setLayoutManager(layoutManager);

        if(getIntent() != null)
        {
            order_id_value = getIntent().getStringExtra("OrderId");

            order_id.setText(order_id_value);
            order_phone.setText(Common.currentRequest.getPhone());
            order_address.setText(Common.currentRequest.getAddress());
            order_total.setText(Common.currentRequest.getTotal());
            order_comment.setText(Common.currentRequest.getComment());

            OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getFoods());
            adapter.notifyDataSetChanged();
            listfoods.setAdapter(adapter);

        }
    }
}