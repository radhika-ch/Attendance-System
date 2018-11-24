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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherDialog extends AppCompatDialogFragment {

    private EditText name;
    EditText teacherName;
    private TeacherDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.teacherdialog, null);

        builder.setView(view)
                .setTitle("Teacher's name")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = teacherName.getText().toString();
                        if(name.length() == 0)
                        {
                            Toast.makeText(getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            listener.sendText(name);
                        }

                    }
                });
        teacherName = view.findViewById(R.id.name);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (TeacherDialogListener) context;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement TeacherDialogListener");
        }
        }

    public interface TeacherDialogListener
    {
        void sendText(String teacherName);
    }
}

