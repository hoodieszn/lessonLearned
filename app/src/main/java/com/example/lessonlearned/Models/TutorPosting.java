package com.example.lessonlearned.Models;

import android.location.Location;

import com.example.lessonlearned.Singletons.Context;

import java.util.List;

public class TutorPosting {
    private int id;
    private List<Course> postingCourses;
    private String postText;
    private double price;
    private int tutorId;
    private String tutorName;
    private double latitude;
    private double longitude;
    private double rating;

    public TutorPosting(int id, List<Course> postingCourses, String postText, double price, int tutorId, String tutorName, double latitude, double longitude, double rating) {
        this.id = id;
        this.postingCourses = postingCourses;
        this.postText = postText;
        this.price = price;
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Course> getPostingCourses() {
        return postingCourses;
    }

    public void setPostingCourses(List<Course> courses) {
        this.postingCourses = courses;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDistance() {
        float[] distance = new float[3];
        Location.distanceBetween(Context.getUser().getLatitude(), Context.getUser().getLongitude(), latitude, longitude, distance);
        return distance[0]/1000;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
