package com.example.lessonlearned.Models;

public class School {
    private int schoolId;
    private String name;

    // Constructor

    public School(int schoolId, String name){
        this.schoolId = schoolId;
        this.name = name;
    }

    // Getters and Setters

    public int getSchoolId() {
        return schoolId;
    }

    public String getName() {
        return name;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
