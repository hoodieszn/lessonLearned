package com.lessonlearned.app.Models;

public class UserReview {
    private int userId;
    private String studentName;
    private int tutorId;
    private String comment;
    private double rating;

    public UserReview(int userId, String studentName, int tutorId, String comment, double rating) {
        this.userId = userId;
        this.studentName = studentName;
        this.tutorId = tutorId;
        this.comment = comment;
        this.rating = rating;
    }

    public int getUserId() {
        return userId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getTutorId() {
        return tutorId;
    }

    public String getComment() {
        return comment;
    }

    public double getRating() {
        return rating;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
