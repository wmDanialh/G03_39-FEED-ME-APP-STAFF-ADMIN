package com.danny.feedmeappadminstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdminStaffTermsAndConditions extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_staff_terms_and_conditions);

        Toolbar toolbar = findViewById(R.id.toolbarTcAndDt);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Terms & Conditions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setTitleTextColor(getResources().getColor(android.R.color.holo_red_dark));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String []listviewitemsTcAndDt = new String[]{
                "Terms & Conditions","Data Policy"
        };

        listView = (ListView)findViewById(R.id.listviewTcAndDt);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,listviewitemsTcAndDt);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(position == 0){
                    Intent intent = new Intent(AdminStaffTermsAndConditions.this,AdminStaffTermsConditionsActivity.class);
                    startActivity(intent);
                }
                else if(position == 1){
                    Intent intent2 = new Intent(AdminStaffTermsAndConditions.this,AdminStaffDataPolicyActivity.class);
                    startActivity(intent2);
                }
            }
        });

    }
}