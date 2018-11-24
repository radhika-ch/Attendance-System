package com.example.android.firebase;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by radhika on 9/12/17.
 */

public class SubjectsAdapter extends ArrayAdapter<String> {
    private int rId;
    private Activity context;
    private List<String> subjectLists;
    public SubjectsAdapter(Activity context, List<String> subjectLists)
    {
        super(context, R.layout.listlayout, subjectLists);
        this.context = context;
        this.subjectLists = subjectLists;
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.listlayout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.name);
        String  subject = subjectLists.get(position);

        textViewName.setText(subject);

        return  listViewItem;

    }
}
