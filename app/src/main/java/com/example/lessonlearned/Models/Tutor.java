package com.example.lessonlearned.Models;

import java.util.List;

public class Tutor {
    private int id;
    private String name;
    private int degreeId;
    private String degree;
    private double price;
    private String phone;
    private double latitude;
    private double longitude;
    private List<Course> courses;
    private double rating;
    private List<UserReview> reviews;

    // Calculated field
    private double distance;

    // Constructor

    public Tutor(int id, String name, int degreeId, String degree, double price, String phone, double latitude,
                 double longitude, List<Course> courses, double rating, List<UserReview> reviews) {
        this.id = id;
        this.name = name;
        this.degreeId = degreeId;
        this.degree = degree;
        this.price = price;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courses = courses;
        this.rating = rating;
        this.reviews = reviews;
    }

    // Getters

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getPrice() {
        return price;
    }

    public int getDegreeId() {
        return degreeId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public String getDegree() {
        return degree;
    }

    public String getPhone() {
        return phone;
    }

    public double getRating() {
        return rating;
    }

    public List<UserReview> getReviews() {
        return reviews;
    }

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setDegreeId(int degreeId) {
        this.degreeId = degreeId;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReviews(List<UserReview> reviews) {
        this.reviews = reviews;
    }
}
