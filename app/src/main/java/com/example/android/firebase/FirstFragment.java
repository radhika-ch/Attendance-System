package com.example.android.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;


public class FirstFragment extends Fragment{
    //   setContentView(R.layout.words_layout);
    FloatingActionButton button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timetable_layout, container, false);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        final ArrayList<Days> days = new ArrayList<Days>();
        days.add(new Days("Monday"));
        days.add(new Days("Tuesday"));
        days.add(new Days("Wednesday"));
        days.add(new Days("Thursday"));
        days.add(new Days("Friday"));
        days.add(new Days("Saturday"));
        days.add(new Days("Sunday"));

        DayAdapter adapter = new DayAdapter(getActivity(), days);

        ListView listView = (ListView) rootView.findViewById(R.id.list);



        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Days day = days.get(position);
                String currentDay = day.getDay();
               // Toast.makeText(getContext(), day.getDay(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), PeriodActivity.class);
                i.putExtra("Batch", "2018");
                i.putExtra("Day", currentDay);
                startActivity(i);

            }
        });
        return  rootView;
    }


}


