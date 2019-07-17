package com.example.lessonlearned.Models;

import java.util.List;

public class Student extends User{
    private List<Integer> contactedTutorIds;

    public Student(int id, String firebaseId, int schoolId, String name, String phone, double latitude, double longitude, UserType userType, List<Integer> contactedTutorIds) {
        super(id, firebaseId, schoolId, name, phone, latitude, longitude, userType);
        this.contactedTutorIds = contactedTutorIds;
    }

    public List<Integer> getContactedTutorIds() {
        return contactedTutorIds;
    }

    public void setContactedTutorIds(List<Integer> contactedTutorIds) {
        this.contactedTutorIds = contactedTutorIds;
    }
}

