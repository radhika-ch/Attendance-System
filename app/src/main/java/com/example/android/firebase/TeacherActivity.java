package com.example.android.firebase;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeacherActivity extends AppCompatActivity implements TeacherDialog.TeacherDialogListener {

    FloatingActionButton addButton;
    LinearLayout container;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Teachers");
    DatabaseReference retrieveDatabase = FirebaseDatabase.getInstance().getReference("Teachers");
    ListView listViewTeachers;
    List<Teacher> teacherList;
    HashMap<String, Integer> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        listViewTeachers = (ListView) findViewById(R.id.listViewTeachers);
        teacherList = new ArrayList<>();
        addButton = (FloatingActionButton) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
                
            }
        });

        registerForContextMenu(listViewTeachers);

    }
    public  void openDialog()
    {
        TeacherDialog teacherDialog = new TeacherDialog();
        teacherDialog.show(getSupportFragmentManager(), "Teacher Dialog");
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teacherList.clear();
                for(DataSnapshot teacherSnapshot : dataSnapshot.getChildren())
                {

                    Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                    teacherList.add(teacher);
                }
                TeacherList adapter = new TeacherList(TeacherActivity.this, teacherList);
                listViewTeachers.setAdapter(adapter);
                listViewTeachers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Teacher teacher = teacherList.get(i);
                        teacher.getName();
                       // deleteTeacher(teacher);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void sendText(String teacherName) {
//        container = (LinearLayout) findViewById(R.id.container);
//        TextView name = new TextView(this);
//        name.setText(teacherName);
//        container.addView(name);
        saveTeacherName(teacherName);

    }

    public void deleteTeacher(Teacher teacher)
    {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Teachers").orderByChild("name").equalTo(teacher.getName());

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


    private void saveTeacherName(String teacherName)
    {
//        String id = myRef.push().getKey();
        subjects = new HashMap<String, Integer>();
        subjects.put("new", 0);
        Teacher teacher = new Teacher(teacherName, subjects);
//        String t = teacherName;
        myRef.child(teacherName).setValue(teacher);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.deleditmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


        switch (item.getItemId())
        {
            case R.id.del :
                Teacher s = teacherList.get(info.position);
                deleteTeacher(s);
                return  true;
            default:
                return super.onContextItemSelected(item);

        }

    }
}
