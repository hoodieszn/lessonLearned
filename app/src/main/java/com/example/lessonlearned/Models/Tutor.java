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

    public void deletePosting(int postingId){
        int toDelete = -1;

        for (int i=0; i< postings.size(); i++){
            if (postings.get(i).getId()== postingId){
                toDelete = i;
                break;
            }
        }

        if (toDelete != -1) postings.remove(toDelete);
    }
}
