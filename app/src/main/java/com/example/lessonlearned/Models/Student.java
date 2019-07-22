package com.example.lessonlearned.Models;

import java.util.List;

public class Student extends User{
    private List<ContactedTutor> contactedTutors;

    public Student(int id, String firebaseId, int schoolId, String schoolName, String name, String phone, double latitude, double longitude, UserType userType, List<ContactedTutor> contactedTutors) {
        super(id, firebaseId, schoolId, schoolName, name, phone, latitude, longitude, userType);
        this.contactedTutors = contactedTutors;
    }

    public List<ContactedTutor> getContactedTutors() {
        return contactedTutors;
    }

    public void setContactedTutors(List<ContactedTutor> contactedTutors) {
        this.contactedTutors = contactedTutors;
    }

    public void setReportedFlag(int tutorId){
        for (int i=0; i < contactedTutors.size(); i++){
            if (contactedTutors.get(i).getTutorId() == tutorId){
                contactedTutors.get(i).setReported(true);
            }
        }
    }
}

