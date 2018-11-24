package com.example.android.firebase;

import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectActivity extends AppCompatActivity implements SubjectDialog.SubjectDialogListener {

    FloatingActionButton addButton;
    LinearLayout container;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Subjects");
    DatabaseReference retrieveDatabase = FirebaseDatabase.getInstance().getReference("Subjects");
    ListView listViewSubjects;
    List<Subject> subjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        listViewSubjects = (ListView) findViewById(R.id.listViewSubjects);
        subjectList = new ArrayList<>();
        addButton = (FloatingActionButton) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

    }
    public  void openDialog()
    {
        SubjectDialog subjectDialog = new SubjectDialog();
        subjectDialog.show(getSupportFragmentManager(), "Subject Dialog");
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subjectList.clear();
                for(DataSnapshot teacherSnapshot : dataSnapshot.getChildren())
                {
                    Subject subject = teacherSnapshot.getValue(Subject.class);
                    subjectList.add(subject);
                }
                SubjectList adapter = new SubjectList(SubjectActivity.this, subjectList);
                listViewSubjects.setAdapter(adapter);
                listViewSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Subject subject = subjectList.get(i);
                        subject.getSubjectCode();
                        deleteSubject(subject);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void sendText(String subjectCode, String teacherName, String batch) {
//        container = (LinearLayout) findViewById(R.id.container);
//        TextView name = new TextView(this);
//        name.setText(teacherName);
//        container.addView(name);
        saveSubject(subjectCode, teacherName, batch);

    }

    public void deleteSubject(Subject subject)
    {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Subjects").orderByChild("subjectCode").equalTo(subject.getSubjectCode());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }


    private void saveSubject(final String subjectCode, final String teacherName, String batch)
    {
//        String id = myRef.push().getKey();
        Subject subject = new Subject(subjectCode, teacherName, batch);
        myRef.child(subjectCode).setValue(subject);
        final DatabaseReference myRef1 = database.getReference("Teachers");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int foundTeacher = 0;
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Teacher t = teacherSnapshot.getValue(Teacher.class);
                    Toast.makeText(getApplicationContext(), t.name, Toast.LENGTH_SHORT).show();
                    if((t.name).equals(teacherName)) {
                        t.subjects.put(subjectCode, 0);
                        t.subjects.remove("new");
                        myRef1.child(teacherName).setValue(t);
                        foundTeacher = 1;
                    }
//                    foundTeacher = 1;


                }
                if(foundTeacher == 0)
                {
                    HashMap<String , Integer> s = new HashMap<>();
                    s.put(subjectCode, 0);
                    Teacher t = new Teacher(teacherName, s);
                    myRef1.child(teacherName).setValue(t);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

