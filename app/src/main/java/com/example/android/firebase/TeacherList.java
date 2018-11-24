package com.example.android.firebase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TeacherList extends ArrayAdapter<Teacher> {
    private Activity context;
    private List<Teacher> teacherLists;

    public TeacherList(Activity context, List<Teacher> teacherLists)
    {
        super(context, R.layout.listlayout, teacherLists);
        this.context = context;
        this.teacherLists = teacherLists;
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.listlayout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.name);

        Teacher teacher = teacherLists.get(position);

        textViewName.setText(teacher.getName());

        return  listViewItem;

    }
}
