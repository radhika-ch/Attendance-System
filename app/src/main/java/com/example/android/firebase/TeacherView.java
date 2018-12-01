package com.example.android.firebase;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TeacherView extends AppCompatActivity {


    ListView listViewTeachers;
    String teacherName;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Teachers");
    HashMap<String, Integer> subjects;
    List<String> subjectList;
    String bluetoothAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view);



        listViewTeachers = (ListView) findViewById(R.id.list);
        subjectList = new ArrayList<>();
        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            if(b.getString("name") != null) {
                teacherName = (String) b.getString("name");
            }

        }

        bluetoothAddress = getBluetoothMacAddress();
       // Toast.makeText(getApplicationContext(), bluetoothAddress, Toast.LENGTH_SHORT).show();
//        DatabaseReference myref1 = database.getReference("Bluetooths");
//
//        BluetoothAddress ba = new BluetoothAddress(teacherName, bluetoothAddress);
//        myref1.child(teacherName).setValue(ba);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot teacherSnapshot : dataSnapshot.getChildren())
                {
                    Teacher t = teacherSnapshot.getValue(Teacher.class);
                    if((t.name).equals(teacherName))
                    {
                        subjects = t.subjects;
                        break;
                    }
                }

                Iterator itr = subjects.entrySet().iterator();
                while(itr.hasNext()){
                    Map.Entry pair = (Map.Entry) itr.next();
                    subjectList.add(pair.getKey().toString());
                    //iterator.remove();
                }

                SubjectsAdapter adapter = new SubjectsAdapter(TeacherView.this, subjectList);
                listViewTeachers.setAdapter(adapter);
                listViewTeachers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String s = subjectList.get(position);
                        Intent i = new Intent(TeacherView.this, StudentList.class);
                        i.putExtra("subject", s);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private String getBluetoothMacAddress() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (NoSuchFieldException e) {

            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        } else {
            bluetoothMacAddress = bluetoothAdapter.getAddress();
        }
        return bluetoothMacAddress;
    }
}
