package com.example.android.firebase;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
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
import java.util.Random;

public class StudentList extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Students");
    DatabaseReference myRef1 = database.getReference("Subjects");
    List<Student> students;
    String subjectCode;
    String batch;
    ListView studentView;
    Button mail;
    Button plus;
    TextView number;
    Button start;
    Button minus;
    int total;
    String num;
    Button stop;

    String bluetoothAddress;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        studentView = (ListView) findViewById(R.id.list);
        stop = (Button) findViewById(R.id.end);



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
                                       // students.add(student);
                                    }
                                }

//                                StudentListAdapter adapter = new StudentListAdapter(StudentList.this, students);
//                                studentView.setAdapter(adapter);
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


        plus = (Button) findViewById(R.id.plus);
        number = (TextView) findViewById(R.id.number);
        start = (Button) findViewById(R.id.start);
        minus = (Button) findViewById(R.id.minus);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = number.getText().toString();
                number.setText(((Integer.parseInt(n))+1) + "");
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = number.getText().toString();
                if(Integer.parseInt(n) <= 0)
                {
                    Toast.makeText(getApplicationContext(), "Number of classes cannot be negative", Toast.LENGTH_LONG).show();
                }
                else
                    number.setText(((Integer.parseInt(n))-1) + "");
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                }

//                Intent i = new Intent(StudentList.this, CheckProxy.class);
//                i.putExtra("subject", subjectCode);
//                i.putExtra("number", number.getText().toString());
//                startActivity(i);


                DatabaseReference d = FirebaseDatabase.getInstance().getReference("Students");
                d.child(batch).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int max = 2;
                        int min = 1;
                        int random = new Random().nextInt((max - min) + 1) + min;
                        Toast.makeText(getApplicationContext(), random+"", Toast.LENGTH_SHORT).show();

                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        {
                            if(random > 0)
                            {
                                final int random1 = new Random().nextInt((1 - 0) + 1) + 0;
                                if(random1 == 1)
                                {
                                  students.add(dataSnapshot1.getValue(Student.class));
                                  random = random -1;
                                }
                            }
                        }

                        StudentListAdapter adapter = new StudentListAdapter(StudentList.this, students);
                        studentView.setAdapter(adapter);
//                        students.clear();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number.getText().toString().equals("0"))
                {
                    Toast.makeText(getApplicationContext(), "Please add the number of classes", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);
                        SystemClock.sleep(4000);
                    }

                    if(mBluetoothAdapter.isEnabled()) {
                        bluetoothAddress = getBluetoothMacAddress();
                       // Toast.makeText(getApplicationContext(), bluetoothAddress, Toast.LENGTH_SHORT).show();
                        DatabaseReference myref1 = database.getReference("Bluetooths");

                        BluetoothAddress ba = new BluetoothAddress(subjectCode, bluetoothAddress);
                        myref1.child(subjectCode).setValue(ba);
                        Toast.makeText(getApplicationContext(), "System open for attendance responses", Toast.LENGTH_SHORT).show();
                        final DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Subjects");
                        myref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {
                                    if (subjectSnapshot.getKey().equals(subjectCode)) {
                                        Subject s = subjectSnapshot.getValue(Subject.class);
                                        s.total = s.total + Integer.parseInt(number.getText().toString());
                                        myref.child(subjectCode).setValue(s);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Subjects");
                        dr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                    Subject s = studentSnapshot.getValue(Subject.class);
                                    if ((s.subjectCode).equals(subjectCode)) {
                                        batch = s.batch;
                                        total = s.total;

                                        break;
                                    }
                                }
                                final DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Students");
                                myref2.child(batch).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds : dataSnapshot.getChildren())
                                        {
                                            Student student = ds.getValue(Student.class);
                                            studentSubject ss = student.subjectMap.get(subjectCode);
                                            ss.total = ss.total + Integer.parseInt(number.getText().toString());
                                            ss.currentNumber = Integer.parseInt(number.getText().toString());
                                            student.subjectMap.put(subjectCode, ss);
                                            myref2.child(batch).child(student.studentRollNumber).setValue(student);
                                        }

                                        number.setText("0");
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                    }

                    else {
                        Toast.makeText(getApplicationContext(), "Please turn the bluetooth on", Toast.LENGTH_SHORT).show();
                    }
                }

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
