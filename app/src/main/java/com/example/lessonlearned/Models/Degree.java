package com.example.lessonlearned.Models;

public class Degree {
    private int id;
    private String name;
    private int schoolId;
    private String schoolName;

    // Constructor

    public Degree(int id, String name, int schoolId, String schoolName){
        this.id = id;
        this.name = name;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() { return schoolName; }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
}
