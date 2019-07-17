package com.example.lessonlearned.Models;

import java.util.List;

public class TutorPosting {
    private int id;
    private List<Course> postingCourses;
    private String postText;
    private double price;
    private Tutor tutor;

    public TutorPosting(int id, List<Course> postingCourses, String postText, double price, Tutor tutor) {
        this.id = id;
        this.postingCourses = postingCourses;
        this.postText = postText;
        this.price = price;
        this.tutor = tutor;
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

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
