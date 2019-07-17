package com.example.lessonlearned.Models;

public class ContactedTutor {
    private int tutorId;
    private String tutorName;
    private String tutorPhone;

    public ContactedTutor(int tutorId, String tutorName, String tutorPhone) {
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.tutorPhone = tutorPhone;
    }

    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getTutorPhone() {
        return tutorPhone;
    }

    public void setTutorPhone(String tutorPhone) {
        this.tutorPhone = tutorPhone;
    }
}
