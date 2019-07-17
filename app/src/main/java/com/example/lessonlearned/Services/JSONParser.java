package com.example.lessonlearned.Services;

import android.util.Log;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Models.UserReview;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.Singletons.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class JSONParser {


    // Parse JSON to User Object

    public static void parseUserResponse(String UUID, final Callable<Void> callback, JSONObject response){
        try {
            Log.d("USER", response.toString());

            JSONObject jsonUser = response.getJSONObject("data").getJSONObject("user");

            int id = jsonUser.getInt("id");
            int schoolId = jsonUser.getInt("schoolId");
            String name = jsonUser.getString("name");
            String phone = jsonUser.getString("phoneNumber");
            double latitude = jsonUser.getDouble("lat");
            double longitude = jsonUser.getDouble("lon");

            String userTypeString = jsonUser.getString("userType");
            UserType userType = userTypeString.equalsIgnoreCase(UserType.STUDENT.toString()) ? UserType.STUDENT : UserType.TUTOR;

            User currentUser = new User(id, UUID, schoolId, name, phone, latitude, longitude, userType);
            Context.setUser(currentUser);

            try {
                callback.call();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            Log.d("REST_ERROR", e.toString());
        }
    }

    // Parse JSON to degrees

    public static void parseDegreesResponse(final Callable<Void> callback, JSONObject response){
        try {
            Log.d("DEGREESJSON", response.toString());

            JSONArray jsonDegrees = response.getJSONObject("data").getJSONArray("degrees");

            Map<Integer, Degree> degreeMap = new HashMap<Integer, Degree>();

            for (int i = 0; i < jsonDegrees.length(); i++) {
                JSONObject jsonDegree = jsonDegrees.getJSONObject(i);

                int id = jsonDegree.getInt("id");
                int schoolId = jsonDegree.getInt("schoolId");
                String name = jsonDegree.getString("name");
                String schoolName = jsonDegree.getString("schoolName");

                degreeMap.put(id, new Degree(id, name, schoolId, schoolName));
            }

            Context.setDegrees(degreeMap);

            try {
                callback.call();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            Log.d("REST_ERROR", e.toString());
        }
    }


    // Parse Postings to tutorPostings

    public static void parsePostingsResponse(int degreeId, final Callable<Void> callback, JSONObject response){
        final int schoolId = Context.getUser().getSchoolId();
        final String degreeName = Context.getDegrees().get(degreeId).getName();

        final ArrayList<TutorPosting> tutorPostings = new ArrayList<TutorPosting>();

        try {
            Log.d("POSTINGSJSON", response.toString());

            JSONArray jsonPostings = response.getJSONObject("data").getJSONArray("tutorPostings");

            for (int i = 0; i < jsonPostings.length(); i++) {
                JSONObject jsonPosting = jsonPostings.getJSONObject(i);

                int id = jsonPosting.getInt("id");

                int tutorId = jsonPosting.getInt("tutorId");
                String tutorName = jsonPosting.getString("tutorName");
                String tutorPhone = jsonPosting.getString("phoneNumber");

                double price = jsonPosting.getDouble("price");
                double rating = jsonPosting.getDouble("rating");

                double lat = jsonPosting.getDouble("lat");
                double lon = jsonPosting.getDouble("lon");

                String postText = jsonPosting.getString("postText");

                JSONArray jsonCourses = jsonPosting.getJSONArray("courses");
                ArrayList<Course> courses = new ArrayList<Course>();

                for (int j = 0; j < jsonCourses.length(); j++) {
                    JSONObject jsonCourse = jsonCourses.getJSONObject(j);

                    int courseId = jsonCourse.getInt("id");
                    String courseName = jsonCourse.getString("name");

                    courses.add(new Course(courseId, courseName, schoolId));
                }

                JSONArray jsonReviews = jsonPosting.getJSONArray("reviews");
                ArrayList<UserReview> reviews = new ArrayList<UserReview>();

                for (int j = 0; j < jsonReviews.length(); j++) {
                    JSONObject jsonReview = jsonReviews.getJSONObject(j);

                    int reviewUserId = jsonReview.getInt("userId");
                    int reviewTutorId = jsonReview.getInt("tutorId");
                    String reviewText = jsonReview.getString("reviewText");
                    double reviewRating = jsonReview.getInt("rating");

                    reviews.add(new UserReview(reviewUserId, reviewTutorId, reviewText, reviewRating));
                }

                Tutor tutor = new Tutor(tutorId, tutorName, degreeId, degreeName, price, tutorPhone, lat, lon, courses, rating, reviews);
                TutorPosting posting = new TutorPosting(id, courses, postText, tutor);
                tutorPostings.add(posting);
            }

            Context.setTutorPostings(tutorPostings);

            try {
                callback.call();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            Log.d("REST_ERROR", e.toString());
        }
    }
}
