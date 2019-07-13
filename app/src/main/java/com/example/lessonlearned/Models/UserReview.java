package com.example.lessonlearned.Models;

public class UserReview {
    private int id;
    private int userId;
    private int tutorId;
    private String comment;
    private double rating;

    public UserReview(int id, int userId, int tutorId, String comment, double rating) {
        this.id = id;
        this.userId = userId;
        this.tutorId = tutorId;
        this.comment = comment;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
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

    public void setId(int id) {
        this.id = id;
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
