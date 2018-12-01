package com.example.android.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    Button manageTeachers;
    Button manageTimetables;
    Button manageSubjects;
    String userType;
    int set = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        manageTeachers = (Button) findViewById(R.id.teachers);
        manageTimetables = (Button) findViewById(R.id.timetables);
        manageSubjects = (Button) findViewById(R.id.subjects) ;

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        String name = acct.getDisplayName();
       // Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

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


        final DatabaseReference a = FirebaseDatabase.getInstance().getReference("Total Students");
        a.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(d.hasChildren())
                    {
                       // Toast.makeText(getApplicationContext(), "HI", Toast.LENGTH_SHORT).show();
                        set = 0;
                        break;
                    }
                }
                if(set ==1 )
                {
                    a.child("2015").setValue(new Number(0));
                    a.child("2016").setValue(new Number(0));
                    a.child("2017").setValue(new Number(0));
                    a.child("2018").setValue(new Number(0));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
