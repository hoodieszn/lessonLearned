package com.example.lessonlearned.Singletons;

import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Context {
    private User user;
    private Map<Integer, Degree> degrees;
    private List<TutorPosting> tutorPostings;

    private static final Context ourInstance = new Context();

    public static Context getInstance() {
        return ourInstance;
    }

    private Context() {
    }

    public static void setUser(User user){
        ourInstance.user = user;
    }

    public static User getUser(){
        return ourInstance.user;
    }

    public static Map<Integer, Degree> getDegrees() {
        return ourInstance.degrees;
    }

    public static void setDegrees(Map<Integer, Degree> degrees) {
        ourInstance.degrees = degrees;
    }

    public static List<TutorPosting> getTutorPostings() {
        return ourInstance.tutorPostings;
    }

    public static void setTutorPostings(List<TutorPosting> tutorPostings) {
        ourInstance.tutorPostings = tutorPostings;
    }
}
