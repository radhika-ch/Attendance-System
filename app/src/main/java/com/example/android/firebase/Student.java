package com.example.android.firebase;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {

    String studentName;
    String studentEmail;
    String studentRollNumber;
    String batch;
    HashMap<String, studentSubject> subjectMap=new HashMap<String, studentSubject>();

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public String getBatch() {
        return batch;
    }

    public HashMap<String, studentSubject> getSubjectMap() {
        return subjectMap;
    }

    public  Student()
    {

    }

    public Student(String studentName, String studentEmail, String studentRollNumber, String batch, HashMap<String, studentSubject> subjectMap)
    {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentRollNumber = studentRollNumber;
        this.batch = batch;
        this.subjectMap = subjectMap;
    }


}
