package com.example.android.firebase;

public class PeriodClass {

    public String subjectCode;
    public String batchYear;
    public String startTime;
    public String endTime;

    public PeriodClass()
    {

    }
    public String getSubjectCode() {
        return subjectCode;
    }

    public String getBatchYear() {
        return batchYear;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public  PeriodClass(String subjectCode, String batchYear, String startTime, String endTime)
    {
        this.subjectCode = subjectCode;
        this.batchYear = batchYear;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
