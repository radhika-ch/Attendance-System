package com.example.android.firebase;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class First extends AppCompatActivity {

    Button student;
    Button teacher;
    Button admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

//        String a = getBluetoothMacAddress();
//        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();




        student = (Button) findViewById(R.id.student);
        teacher = (Button) findViewById(R.id.teacher);
        admin = (Button) findViewById(R.id.admin);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(First.this, MainActivity.class);
                i.putExtra("User type", "student");
                startActivity(i);
                finish();
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(First.this, MainActivity.class);
                i.putExtra("User type", "teacher");
                startActivity(i);
                finish();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(First.this, MainActivity.class);
                i.putExtra("User type", "admin");
                startActivity(i);
                finish();
            }
        });
    }


}
