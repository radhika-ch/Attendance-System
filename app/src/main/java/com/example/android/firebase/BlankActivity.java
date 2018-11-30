package com.example.android.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BlankActivity extends AppCompatActivity {

    String userType;
    String name;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Teachers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        String email = acct.getEmail();
        name = acct.getDisplayName();

        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            if(b.getString("User type") != null) {
                userType = (String) b.getString("User type");
            }

        }

        if(userType.equals("admin")) {
            if (email.equals("radhika12chandak@gmail.com") || email.equals("meharanjan@gmail.com")) {
                Intent i = new Intent(BlankActivity.this, AdminActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(BlankActivity.this, First.class);
                Toast.makeText(getApplicationContext(), "You are not an admin user", Toast.LENGTH_SHORT).show();
                i.putExtra("name", name);
                startActivity(i);

                finish();

            }
        }

        else if(userType.equals("teacher"))
        {
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(name)) {
                        Intent i = new Intent(BlankActivity.this, TeacherView.class);
                        i.putExtra("name", name);
                        startActivity(i);
                        finish();
                        // run some code
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "You are not a teacher", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BlankActivity.this, First.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else if(userType.equals("student"))
        {
            Intent i =  new Intent(BlankActivity.this, RecordActivity.class);
            startActivity(i);
            finish();
        }





    }
}
