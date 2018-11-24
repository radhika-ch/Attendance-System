package com.example.android.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StudentList extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Students");
    DatabaseReference myRef1 = database.getReference("Subjects");
    List<Student> students;
    String subjectCode;
    String batch;
    ListView studentView;
    Button mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        studentView = (ListView) findViewById(R.id.list);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            if (b.getString("subject") != null) {
                subjectCode = (String) b.getString("subject");
            }

        }
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    Subject s = studentSnapshot.getValue(Subject.class);
                    if ((s.subjectCode).equals(subjectCode)) {
                        batch = s.batch;
                        myRef.child(batch).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                students = new ArrayList<>();
                                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                    Student student = studentSnapshot.getValue(Student.class);
                                    if ((student.batch).equals(batch)) {
                                        students.add(student);
                                    }
                                }

                                StudentListAdapter adapter = new StudentListAdapter(StudentList.this, students);
                                studentView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mail = (Button) findViewById(R.id.mail);

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                            Subject s = studentSnapshot.getValue(Subject.class);
                            if ((s.subjectCode).equals(subjectCode)) {
                                batch = s.batch;
                                myRef.child(batch).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int index = 0;
                                        List<String> l = new ArrayList<>();

                                        for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                            Student s = studentSnapshot.getValue(Student.class);
                                            HashMap<String, studentSubject> subs = s.subjectMap;
                                            Iterator itr = subs.entrySet().iterator();
                                            while (itr.hasNext()) {
                                                Map.Entry pair = (Map.Entry) itr.next();
                                                if ((pair.getKey()).equals(subjectCode)) {
                                                    studentSubject ss = (studentSubject) pair.getValue();
                                                    float percent = ss.percentage;
                                                    if (percent < 75) {
                                                        l.add(s.studentEmail);
                                                        index = index + 1;
                                                    }
                                                }
                                            }
                                        }
                                        String[] emails = new String[index];
                                        for(int i=0; i<index; i++)
                                        {
                                            emails[i] = l.get(i);
                                        }

                                        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
                                        i.setType("message/rfc822");
                                        i.putExtra(Intent.EXTRA_EMAIL  , emails);
                                        i.putExtra(Intent.EXTRA_SUBJECT, "You attendance is below 75%!");
                                        i.putExtra(Intent.EXTRA_TEXT   , "Dear student "  + ",\n" + "Your attendance for " + subjectCode + " has fallen below 75%. Kindly consider this as an alert message and cover you attendance in time.");

                                        try {
                                            startActivity(i);
                                        } catch (android.content.ActivityNotFoundException ex) {
                                            Toast.makeText(StudentList.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                        }


                                                //iterator.remove();



                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }
}
