package com.example.lessonlearned.Models;

import java.util.List;

public class Tutor extends User{
    private List<TutorPosting> postings;
    private List<UserReview> reviews;
    private double averageRating;


    public Tutor(int id, String firebaseId, int schoolId, String schoolName, String name, String phone, double latitude, double longitude, UserType userType, List<TutorPosting> postings, List<UserReview> reviews, double averageRating) {
        super(id, firebaseId, schoolId, schoolName, name, phone, latitude, longitude, userType);
        this.postings = postings;
        this.reviews = reviews;
        this.averageRating = averageRating;
    }

    public List<TutorPosting> getPostings() {
        return postings;
    }

    public void setPostings(List<TutorPosting> postings) {
        this.postings = postings;
    }

    public List<UserReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<UserReview> reviews) {
        this.reviews = reviews;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
