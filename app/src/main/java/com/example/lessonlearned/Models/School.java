package com.example.lessonlearned.Models;

public class School {
    private int id;
    private String name;

    // Constructor

    public School(int id, String name){
        this.id = id;
        this.name = name;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
