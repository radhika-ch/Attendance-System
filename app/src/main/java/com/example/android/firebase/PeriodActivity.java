package com.example.android.firebase;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PeriodActivity extends AppCompatActivity implements PeriodDialog.PeriodDialogListener{

    FloatingActionButton addButton;
    LinearLayout container;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Periods");
    DatabaseReference retrieveDatabase = FirebaseDatabase.getInstance().getReference("Periods");
    ListView listViewPeriods;
    List<PeriodClass> periodList;
    TextView start;
    TextView end;
    TextView subject;
    String batch;
    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            if(b.getString("Batch") != null) {
                batch = (String) b.getString("Batch");
                day = (String) b.getString("Day");
              //  Toast.makeText(getApplicationContext(), day, Toast.LENGTH_SHORT).show();
            }

        }
        listViewPeriods = (ListView) findViewById(R.id.listViewPeriods);
        periodList = new ArrayList<>();
        addButton = (FloatingActionButton) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        subject = (TextView) findViewById(R.id.subject);







    }
    public  void openDialog()
    {
        PeriodDialog periodDialog = new PeriodDialog();
        periodDialog.show(getSupportFragmentManager(), "Period Dialog");
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveDatabase.child(batch).child(day).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                periodList.clear();
                for(DataSnapshot teacherSnapshot : dataSnapshot.getChildren())
                {
                    PeriodClass period = teacherSnapshot.getValue(PeriodClass.class);
                    periodList.add(period);
                }
                PeriodList adapter = new PeriodList(PeriodActivity.this, periodList);
                listViewPeriods.setAdapter(adapter);
                listViewPeriods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        PeriodClass period = periodList.get(i);
                        period.getSubjectCode();
                        deleteSubject(period);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void sendText(String subjectCode, String hourStartTime, String minuteStartTime, String AMPMStartTime, String hourEndTime, String minuteEndTime, String AMPMEndTime) {
//        container = (LinearLayout) findViewById(R.id.container);
//        TextView name = new TextView(this);
//        name.setText(teacherName);
//        container.addView(name);
        String startTime = hourStartTime + ":" + minuteStartTime + " " + AMPMStartTime;
        String endTime = hourEndTime + ":" + minuteEndTime + " " + AMPMEndTime;
        saveSubject(subjectCode, startTime, endTime);

    }

    public void deleteSubject(PeriodClass period)
    {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Periods").orderByChild("subjectCode").equalTo(period.getSubjectCode());

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


    private void saveSubject(String subjectCode, String startTime, String endTime)
    {
//        String id = myRef.push().getKey();
        PeriodClass period = new PeriodClass(subjectCode, batch, startTime, endTime);
        myRef.child(batch).child(day).child(subjectCode).setValue(period);

    }
}



