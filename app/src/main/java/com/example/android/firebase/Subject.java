package com.example.android.firebase;

public class Subject {

    String subjectCode;
    String teacherName;
    String batch;


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

    public  Subject(String subjectCode, String teacherName, String batch){
        this.subjectCode = subjectCode;
        this.teacherName = teacherName;
        this.batch = batch;
    }


}
