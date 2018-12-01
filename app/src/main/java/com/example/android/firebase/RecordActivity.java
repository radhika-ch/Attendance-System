package com.example.android.firebase;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RecordActivity extends AppCompatActivity {

    // GUI Components

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference retrieveDatabase = FirebaseDatabase.getInstance().getReference("Periods");
    final DatabaseReference retrieveDatabaseStudent = FirebaseDatabase.getInstance().getReference("Students");
    String currentSubject;
    private Button mDiscoverBtn;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;
    private int inProximity = 0;
    private final String TAG = MainActivity.class.getSimpleName();
    private Handler mHandler; // Our main handler that will receive callback notifications
    //    private ContextCompatnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier


    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    TextView nameView;
    TextView emailView;
    TextView rollNoView;
    String batch;
    String rollNumber;
    String subjectCode;
    ArrayList<String> subjects = new ArrayList<String>();
    HashMap<String, studentSubject> subjectMap = new HashMap<String, studentSubject>();
    int record;
    String personName;
    String personEmail;
    int found;
    studentSubject value;
    String key;
    Button logoutBtn;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    ArrayList<String> subject = new ArrayList<String>();
    ArrayList<Integer> start = new ArrayList<Integer>();
    ArrayList<Integer> end = new ArrayList<Integer>();
    int sTime;
    int eTime;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        nameView = (TextView) findViewById(R.id.name);
        emailView = (TextView) findViewById(R.id.email);
        rollNoView = (TextView) findViewById(R.id.rollNo);
        mDiscoverBtn = (Button) findViewById(R.id.discover);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(RecordActivity.this, First.class));
                }

            }
        };

        // mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        mDevicesListView = (ListView) findViewById(R.id.devicesListView);
        mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef;




        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            found = 0;
            personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            nameView.setText("Name: " + personName);
            emailView.setText("Email: " + personEmail);
            String items[] = personEmail.split("@");
            rollNoView.setText("Roll number: " + items[0]);
            rollNumber = items[0];
            batch = items[0].substring(3, 7);



            //Toast.makeText(getApplicationContext(), subjects.get(0), Toast.LENGTH_SHORT).show();

            myRef = FirebaseDatabase.getInstance().getReference("Students");
            DatabaseReference r = retrieveDatabaseStudent.child(batch);
            found = 0;
            r.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot periodSnapshot : dataSnapshot.getChildren()) {
                        Student s = periodSnapshot.getValue(Student.class);
                        String roll = s.studentRollNumber;
                        if(roll.equals(rollNumber))
                        {
                            found = 1;
                        }

                    }

                    if(found == 0)
                    {
                        final DatabaseReference sub = FirebaseDatabase.getInstance().getReference("Subjects");


                        sub.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren())
                                {

                                    Subject subject = subjectSnapshot.getValue(Subject.class);
                                    //Toast.makeText(getApplicationContext(), subject.subjectCode, Toast.LENGTH_SHORT).show();
                                    String b = subject.batch;
                                    if(b.equals(batch))
                                    {
                                        subjects.add(subject.subjectCode);
                                        key = subject.subjectCode;
                                        //Toast.makeText(getApplicationContext(), subject.subjectCode + subject.total + "", Toast.LENGTH_SHORT).show();

                                        value = new studentSubject(key, 0, subject.total, 0, 0);
                                        subjectMap.put(key, value);
                                    }

                                }
                                Student student = new Student(personName, personEmail, rollNumber, batch, subjectMap);

                                myRef.child(batch).child(rollNumber).setValue(student);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }




                        });








                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);



            else {
                mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        discover(v);

                    }
                });
            }

        }
    }



    private void discover(View view){
        // Check if the device is already discovering
        if(mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(),"Discovery stopped",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBTAdapter.isEnabled()) {
                // mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery();
                Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();

            }
        }

    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            Toast.makeText(getApplicationContext(), "M", Toast.LENGTH_SHORT).show();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

//                Toast.makeText(getApplicationContext(), "L", Toast.LENGTH_SHORT).show();
//                 add the name to the list
//                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                mBTArrayAdapter.notifyDataSetChanged();
//                if(device.getAddress().equals("98:D3:51:F5:C1:D7"))
                //{
//                    Toast.makeText(getApplicationContext(), "Found device", Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
                final String strTime =  mdformat.format(calendar.getTime());
                String hour = strTime.substring(0, 2);
                String minute = strTime.substring(3, 5);
                String AMPM = strTime.substring(6, 8);
//


                calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
               // Toast.makeText(getApplicationContext(), day, Toast.LENGTH_SHORT).show();




                final int mins;
                int h = Integer.parseInt(hour);
                int m = Integer.parseInt(minute);

                if(AMPM.equals("PM"))
                {
                    mins = (h + 12)*60 + m;
                }

                else
                {
                    if(h == 12)
                    {
                        h = 0;
                    }
                    mins = (h*60) + m;
                }
                String s = Integer.toString(mins);

                retrieveDatabase.child(batch).child(day).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot periodSnapshot : dataSnapshot.getChildren()) {
                            //  Toast.makeText(getApplicationContext(), "KJ", Toast.LENGTH_SHORT).show();

                            PeriodClass period = periodSnapshot.getValue(PeriodClass.class);
                            subjectCode = period.subjectCode;
                            String startTime = period.startTime;
                            String endTime = period.endTime;

                            //Toast.makeText(getApplicationContext(), "Subject is" + subjectCode, Toast.LENGTH_SHORT).show();


                            String startHour = startTime.substring(0, 2);
                            String startMinute = startTime.substring(3, 5);
                            String startAMPM = startTime.substring(6, 8);
                            int startMins;

                            String endHour = endTime.substring(0, 2);
                            String endMinute = endTime.substring(3, 5);
                            String endAMPM = endTime.substring(6, 8);
                            int endMins;

                            int h1 = Integer.parseInt(startHour);
                            int m1 = Integer.parseInt(startMinute);

                            if (startAMPM.equals("PM")) {
                                startMins = (h1 + 12) * 60 + m1;
                            } else {
                                if (h1 == 12) {
                                    h1 = 0;
                                }
                                startMins = (h1 * 60) + m1;
                            }


                            h1 = Integer.parseInt(endHour);
                            m1 = Integer.parseInt(endMinute);

                            if (endAMPM.equals("PM")) {
                                endMins = (h1 + 12) * 60 + m1;
                            } else {
                                if (h1 == 12) {
                                    h1 = 0;
                                }
                                endMins = (h1 * 60) + m1;
                            }
                            subject.add(subjectCode);
                            start.add(startMins);
                            end.add(endMins);
                          //  Toast.makeText(getApplicationContext(), startMins + " " + mins + " " + endMins, Toast.LENGTH_LONG).show();

                        }
                        for(int i=0; i<subject.size(); i++)
                        {
                            //Toast.makeText(getApplicationContext(), subject.get(i), Toast.LENGTH_LONG).show();

                            if(start.get(i) <= mins && end.get(i) >=  mins)
                            {
                                subjectCode = subject.get(i);
                                sTime = start.get(i);
                                eTime = end.get(i);
                               // Toast.makeText(getApplicationContext(), subject.get(i), Toast.LENGTH_LONG).show();
                                break;

                            }
                        }
                        //subjectCode = cs;
                       // Toast.makeText(getApplicationContext(), mins + " " + startMins + " " + endMins, Toast.LENGTH_SHORT).show();
//
//                            if(mins >= startMins && mins <= endMins)
//                            {
//                                Toast.makeText(getApplicationContext(), "Period found: "+subjectCode, Toast.LENGTH_SHORT).show();
                                DatabaseReference bluetoothref = FirebaseDatabase.getInstance().getReference("Bluetooths");
                                bluetoothref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot bs : dataSnapshot.getChildren())
                                        {
                                            BluetoothAddress ba = bs.getValue(BluetoothAddress.class);
                                           // Toast.makeText(getApplicationContext(), "Subject is" + ba.subjectCode, Toast.LENGTH_SHORT).show();

                                            if(ba.subjectCode.equals(subjectCode)) {
                                                String address = ba.address;
                                               // Toast.makeText(getApplicationContext(), subjectCode + address, Toast.LENGTH_SHORT).show();

                                                if (device.getAddress().equals(address)) {
                                                    //Toast.makeText(getApplicationContext(), "FOUNDDD", Toast.LENGTH_SHORT).show();

                                                    currentSubject = subjectCode;

                                                    retrieveDatabaseStudent.child(batch).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot studentSnapshot : dataSnapshot.getChildren())
                                                            {
                                                                Student student= studentSnapshot.getValue(Student.class);
                                                                if(student.studentRollNumber.equals(rollNumber)) {
                                                                    HashMap<String, studentSubject> subjectMap = student.subjectMap;
                                                                    studentSubject s = subjectMap.get(currentSubject);
                                                                   // Toast.makeText(getApplicationContext(), s.currentNumber + "", Toast.LENGTH_SHORT).show();
                                                                    if(s.currentNumber == 0)
                                                                    {
                                                                        Toast.makeText(getApplicationContext(), "You cannot mark your attendance right now", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else {

                                                                        s.present = s.present + s.currentNumber;
                                                                        s.percentage = ((float) s.present / (float) s.total) * 100;
                                                                        subjectMap.put(s.subjectCode, s);
                                                                        student.subjectMap = subjectMap;
                                                                        student.subjectMap.get(subjectCode).currentNumber = 0;

                                                                        // studentSnapshot.getValue(Student.class).subjectMap.get(currentSubject).present = studentSnapshot.getValue(Student.class).subjectMap.get(currentSubject).present + 1;
                                                                        FirebaseDatabase.getInstance().getReference("Students").child(batch).child(student.studentRollNumber).setValue(student);
                                                                        student.subjectMap.get(subjectCode).currentNumber = 0;
                                                                        Toast.makeText(getApplicationContext(), "Attendance for " + currentSubject + " recorded", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }


                                                            }

                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            Toast.makeText(getApplicationContext(), "FAIL", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                    mBTAdapter.cancelDiscovery();
                                                }


                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }

//
//

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                inProximity = 1;
               // mBTAdapter.cancelDiscovery();
//                 }

            }


        }

    };


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection",e);
        }
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }


















}