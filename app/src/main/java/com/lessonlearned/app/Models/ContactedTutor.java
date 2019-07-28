package com.lessonlearned.app.Models;

public class ContactedTutor {
    private int tutorId;
    private String tutorName;
    private String tutorPhone;
    private boolean reported;

    public ContactedTutor(int tutorId, String tutorName, String tutorPhone, boolean reported) {
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.tutorPhone = tutorPhone;
        this.reported = reported;
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

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }
}
