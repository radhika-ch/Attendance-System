package com.example.android.firebase;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by radhika on 9/12/17.
 */

public class StudentListAdapter extends ArrayAdapter<Student> {
    private int rId;
    private Activity context;
    private List<Student> studentList;
    private String subject;
    Button yes;
    Button no;
    public StudentListAdapter(Activity context, List<Student> studentList)
    {
        super(context, R.layout.studentlistlayout, studentList);
        this.context = context;
        this.studentList = studentList;
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        final View listViewItem = inflater.inflate(R.layout.studentlistlayout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.name);
        TextView textViewRoll = (TextView) listViewItem.findViewById(R.id.roll);
        TextView textViewPercent = (TextView) listViewItem.findViewById(R.id.percent);
        Student student = studentList.get(position);


        textViewName.setText(student.studentName);
        textViewRoll.setText(student.studentRollNumber);




        return  listViewItem;

    }
}
