package com.example.android.firebase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SubjectDialog extends AppCompatDialogFragment {
    private EditText name;
    private EditText subject;
    EditText subjectCode;
    EditText teacherName;
    EditText batchName;
    private SubjectDialog.SubjectDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.subjectdialog, null);

        builder.setView(view)
                .setTitle("Subject")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = teacherName.getText().toString();
                        String subjectName = subjectCode.getText().toString();
                        String batch = batchName.getText().toString();
                        if(name.length() == 0)
                        {
                            Toast.makeText(getContext(), "Please enter the teacher's name", Toast.LENGTH_SHORT).show();
                        }
                        if(subjectName.length() == 0) {
                            Toast.makeText(getContext(), "Please enter the subject code", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            listener.sendText(subjectName, name, batch);
                        }

                    }
                });
        subjectCode = view.findViewById(R.id.subject);
        teacherName = view.findViewById(R.id.name);
        batchName = view.findViewById(R.id.batch);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SubjectDialog.SubjectDialogListener) context;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement TeacherDialogListener");
        }
    }

    public interface SubjectDialogListener
    {
        void sendText(String subjectCode, String teacherName, String batch);
    }
}



