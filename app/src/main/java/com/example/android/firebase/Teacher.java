package com.example.android.firebase;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.StatementEvent;

public class Teacher {
    public String name;
    HashMap<String, Integer> subjects;

    public  Teacher()
    {

    }

    public String getName() {
        return name;
    }

    public HashMap<String, Integer> getSubjects() {
        return subjects;
    }

    public  Teacher(String name, HashMap<String, Integer> subjects)
    {
        this.name =  name;
        this.subjects = subjects;
    }
}
