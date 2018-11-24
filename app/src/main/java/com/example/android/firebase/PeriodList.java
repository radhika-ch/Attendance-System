package com.example.android.firebase;

import android.app.Activity;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PeriodList extends ArrayAdapter {

        private Activity context;
        private List<PeriodClass> periodLists;

        public PeriodList(Activity context, List<PeriodClass> periodLists)
        {
            super(context, R.layout.periodlist, periodLists);
            this.context = context;
            this.periodLists = periodLists;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();

            View listViewItem = inflater.inflate(R.layout.periodlist, null, true);

            TextView textViewSubject = (TextView) listViewItem.findViewById(R.id.subject);

            TextView textViewStart = (TextView) listViewItem.findViewById(R.id.start);

            TextView textViewEnd = (TextView) listViewItem.findViewById(R.id.end);





            PeriodClass period = periodLists.get(position);

            textViewSubject.setText(period.getSubjectCode());
            textViewStart.setText(period.getStartTime());
            textViewEnd.setText(period.getEndTime());

            return  listViewItem;

        }
    }


