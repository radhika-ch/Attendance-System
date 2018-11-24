package com.example.android.firebase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PeriodDialog extends AppCompatDialogFragment{
    private EditText name;
    private EditText subject;
    EditText subjectCode;
    Spinner hourStart;
    Spinner minuteStart;
    Spinner AMPMStart;
    Spinner hourEnd;
    Spinner minuteEnd;
    Spinner AMPMEnd;
    private PeriodDialog.PeriodDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.perioddialog, null);
        Spinner spinner = (Spinner) view.findViewById(R.id.hourStart);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(builder.getContext(),
                R.array.HourTimings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = (Spinner) view.findViewById(R.id.minuteStart);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(builder.getContext(),
                R.array.MinuteTimings, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        spinner = (Spinner) view.findViewById(R.id.AMPMStart);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(builder.getContext(),
                R.array.AMPM, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        spinner = (Spinner) view.findViewById(R.id.hourEnd);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(builder.getContext(),
                R.array.HourTimings, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter3);

        spinner = (Spinner) view.findViewById(R.id.minuteEnd);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(builder.getContext(),
                R.array.MinuteTimings, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter4);

        spinner = (Spinner) view.findViewById(R.id.AMPMEnd);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(builder.getContext(),
                R.array.AMPM, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter5);

        builder.setView(view)
                .setTitle("Period")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String subject = subjectCode.getText().toString();
                        String hourStartTime = hourStart.getSelectedItem().toString();
                        String minuteStartTime = minuteStart.getSelectedItem().toString();
                        String AMPMStartTime = AMPMStart.getSelectedItem().toString();
                        String hourEndTime = hourEnd.getSelectedItem().toString();
                        String minuteEndTime = minuteEnd.getSelectedItem().toString();
                        String AMPMEndTime = AMPMEnd.getSelectedItem().toString();
                        if(subject.length() == 0)
                        {
                            Toast.makeText(getContext(), "Please enter the subject code", Toast.LENGTH_SHORT).show();
                        }
//                        if(start.length() == 0) {
//                            Toast.makeText(getContext(), "Please enter the start time", Toast.LENGTH_SHORT).show();
//                        }
//                        if(end.length() == 0)
//                        {
//                            Toast.makeText(getContext(), "Please enter the end time", Toast.LENGTH_SHORT).show();
//
//                        }

                        else
                        {
                            listener.sendText(subject, hourStartTime, minuteStartTime, AMPMStartTime, hourEndTime, minuteEndTime, AMPMEndTime);
                        }

                    }
                });
        subjectCode = view.findViewById(R.id.subject);
        hourStart =  view.findViewById(R.id.hourStart);
        minuteStart = view.findViewById(R.id.minuteStart);
        AMPMStart = view.findViewById(R.id.AMPMStart);
        hourEnd =  view.findViewById(R.id.hourEnd);
        minuteEnd = view.findViewById(R.id.minuteEnd);
        AMPMEnd = view.findViewById(R.id.AMPMEnd);

//        startTime= view.findViewById(R.id.start);
//        endTime = view.findViewById(R.id.end);
        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PeriodDialog.PeriodDialogListener) context;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement TeacherDialogListener");
        }
    }

    public interface PeriodDialogListener
    {
        void sendText(String subjectCode, String hourStartTime, String minuteStartTime, String AMPMStartTime, String hourEndTime, String minuteEndTime, String AMPMEndTime);
    }
}
