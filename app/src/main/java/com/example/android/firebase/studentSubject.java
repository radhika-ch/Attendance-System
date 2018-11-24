package com.example.android.firebase;

public class studentSubject {

    String subjectCode;
    int present;
    int total;
    float percentage;

    public studentSubject()
    {

    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public int getPresent() {
        return present;
    }

    public int getTotal() {
        return total;
    }

    public float getPercentage() {
        return percentage;
    }

    public studentSubject(String subjectCode, int present, int total, float percentage)
    {
        this.subjectCode = subjectCode;
        this.present = present;
        this.total = total;
        this.percentage = percentage;
    }
}
