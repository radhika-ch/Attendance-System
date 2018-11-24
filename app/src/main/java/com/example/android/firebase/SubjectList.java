package com.example.android.firebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.List;

public class SubjectList extends ArrayAdapter {

        private Activity context;
        private List<Subject> subjectLists;

        public SubjectList(Activity context, List<Subject> subjectLists)
        {
            super(context, R.layout.subjectlistlayout, subjectLists);
            this.context = context;
            this.subjectLists = subjectLists;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();

            View listViewItem = inflater.inflate(R.layout.subjectlistlayout, null, true);

            TextView textViewSubject = (TextView) listViewItem.findViewById(R.id.subject);

            TextView textViewName = (TextView) listViewItem.findViewById(R.id.name);

            TextView textViewBatch = (TextView) listViewItem.findViewById(R.id.batch);



            Subject subject = subjectLists.get(position);

            textViewSubject.setText(subject.getSubjectCode());
            textViewName.setText(subject.getTeacherName());
            textViewBatch.setText(subject.getBatch());

            return  listViewItem;

        }
    }


