package com.example.lessonlearned.Models;

public class Course {
    private int id;
    private String name;
    private int schoolId;

    // Constructor

    public Course(int id, String name, int schoolId){
        this.id = id;
        this.name = name;
        this.schoolId = schoolId;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}
