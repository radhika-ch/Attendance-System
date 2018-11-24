package com.example.android.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class AdminActivity extends AppCompatActivity {

    Button manageTeachers;
    Button manageTimetables;
    Button manageSubjects;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        manageTeachers = (Button) findViewById(R.id.teachers);
        manageTimetables = (Button) findViewById(R.id.timetables);
        manageSubjects = (Button) findViewById(R.id.subjects) ;

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        String name = acct.getDisplayName();
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

        manageTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, TeacherActivity.class));

            }
        });
        manageSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, SubjectActivity.class));
            }
        });

        manageTimetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, TimeTableActivity.class));
            }
        });
    }
}
