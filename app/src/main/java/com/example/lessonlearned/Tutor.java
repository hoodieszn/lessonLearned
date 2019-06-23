package com.example.lessonlearned;

import java.util.List;

public class Tutor {
    // This is temporary, we will really be getting this user object from firebase
    String phone;
    String name;
    String institution;
    List<String> courses;
    double price;

    public Tutor(String phone, String name, String institution, List<String> courses, double price){
        this.phone = phone;
        this.name = name;
        this.institution = institution;
        this.courses = courses;
        this.price = price;
    }
}
