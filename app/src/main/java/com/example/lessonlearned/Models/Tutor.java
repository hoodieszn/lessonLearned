package com.example.lessonlearned.Models;

import java.util.List;

public class Tutor extends User{
    private int degreeId;
    private String degree;
    private List<Course> courses;
    private double rating;
    private List<UserReview> reviews;

    // Calculated field
    private double distance;

    // Constructor

    public Tutor(int id, String firebaseId, int schoolId, String name, String phone, double latitude, double longitude, UserType userType,
                 int degreeId, String degree, List<Course> courses, double rating, List<UserReview> reviews) {
        super(id, firebaseId, schoolId, name, phone, latitude, longitude, userType);
        this.degreeId = degreeId;
        this.degree = degree;
        this.courses = courses;
        this.rating = rating;
        this.reviews = reviews;
    }


    // Getters and Setters

    public int getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(int degreeId) {
        this.degreeId = degreeId;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<UserReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<UserReview> reviews) {
        this.reviews = reviews;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
