package com.example.lessonlearned.Models;

import java.util.List;

public class Student extends User{
    private List<ContactedTutor> contactedTutors;

    public Student(int id, String firebaseId, int schoolId, String schoolName, String name, String phone, double latitude, double longitude, UserType userType, List<ContactedTutor> contactedTutorIds) {
        super(id, firebaseId, schoolId, schoolName, name, phone, latitude, longitude, userType);
        this.contactedTutors = contactedTutorIds;
    }

    public List<ContactedTutor> getContactedTutors() {
        return contactedTutors;
    }

    public void setContactedTutors(List<ContactedTutor> contactedTutorIds) {
        this.contactedTutors = contactedTutorIds;
    }
}

