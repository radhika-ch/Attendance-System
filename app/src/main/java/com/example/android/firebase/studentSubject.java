package com.example.android.firebase;

public class studentSubject {

    String subjectCode;
    int present;
    int total;
    float percentage;
    int currentNumber;

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

    public int getCurrentNumber() { return currentNumber;}

    public float getPercentage() {
        return percentage;
    }

    public studentSubject(String subjectCode, int present, int total, float percentage, int currentNumber)
    {
        this.subjectCode = subjectCode;
        this.present = present;
        this.total = total;
        this.percentage = percentage;
        this.currentNumber = currentNumber;
    }
}
