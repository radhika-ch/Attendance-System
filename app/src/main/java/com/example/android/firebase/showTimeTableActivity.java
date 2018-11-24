package com.example.android.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class showTimeTableActivity extends AppCompatActivity {

    String batch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_time_table);

        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            if(b.getString("Batch") != null) {
                batch = (String) b.getString("Batch");
            }

        }

    }
}
