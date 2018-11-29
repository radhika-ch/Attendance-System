package com.example.android.firebase;

public class Subject {

    String subjectCode;
    String teacherName;
    String batch;
    int total;


    public  Subject()
    {

    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getBatch() {
        return batch;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getTotal()
    {
        return total;
    }

    public  Subject(String subjectCode, String teacherName, String batch, int total){
        this.subjectCode = subjectCode;
        this.teacherName = teacherName;
        this.batch = batch;
        this.total = total;
    }


}
