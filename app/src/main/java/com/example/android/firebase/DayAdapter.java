package com.example.android.firebase;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by radhika on 9/12/17.
 */

public class DayAdapter extends ArrayAdapter<Days> {
    private int rId;
    public DayAdapter(Context context, ArrayList<Days> days) {
        super(context,0, days);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listlayout, parent, false);
        }

        // Get the {@link word} object located at this position in the list
        Days currentDay = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView dayName = (TextView) listItemView.findViewById(R.id.name);

        dayName.setText(currentDay.getDay());
        return listItemView;
    }
}
