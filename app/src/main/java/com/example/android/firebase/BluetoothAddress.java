package com.example.android.firebase;

public class BluetoothAddress {
    String address;
    String subjectCode;

    public  void BluetoothAddress()
    {

    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getAddress()
    {
        return address;
    }

    public BluetoothAddress(String subjectCode, String address)
    {
        this.address = address;
        this.subjectCode = subjectCode;
    }
}

