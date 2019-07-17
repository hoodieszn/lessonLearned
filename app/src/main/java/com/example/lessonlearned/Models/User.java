package com.example.lessonlearned.Models;

import java.util.List;

public class User {
    protected int id;
    protected String firebaseId;
    protected int schoolId;
    protected String schoolName;
    protected String name;
    protected String phone;
    protected double latitude;
    protected double longitude;
    protected UserType userType;

    // Constructor

    public User(int id, String firebaseId, int schoolId, String schoolName, String name, String phone, double latitude, double longitude, UserType userType) {
        this.id = id;
        this.firebaseId = firebaseId;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userType = userType;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getSchoolName() {
        return schoolName;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
