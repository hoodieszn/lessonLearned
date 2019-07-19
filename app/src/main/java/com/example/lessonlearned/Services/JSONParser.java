package com.example.lessonlearned.Services;

import android.util.Log;

import com.example.lessonlearned.DegreesActivity;
import com.example.lessonlearned.Models.ContactedTutor;
import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Models.School;
import com.example.lessonlearned.Models.Student;
import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Models.UserReview;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.SignUpActivity;
import com.example.lessonlearned.Singletons.Context;
import com.example.lessonlearned.TutorPostingActivity;
import com.example.lessonlearned.TutorsListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
            String schoolName = "University of Waterloo"; // jsonUser.getInt("schoolName"); TODO: once rav adds school name uncomment
            String name = jsonUser.getString("name");
            String phone = jsonUser.getString("phoneNumber");
            double latitude = jsonUser.getDouble("lat");
            double longitude = jsonUser.getDouble("lon");

            String userTypeString = jsonUser.getString("userType");
            UserType userType = userTypeString.equalsIgnoreCase(UserType.STUDENT.toString()) ? UserType.STUDENT : UserType.TUTOR;

            User currentUser = null;

            if (userType == UserType.STUDENT){

                List<ContactedTutor> contactedTutors = new ArrayList<ContactedTutor>();

                JSONArray jsonContactedTutors = jsonUser.getJSONArray("contactedTutors");

                for (int i = 0; i < jsonContactedTutors.length(); i++) {
                    JSONObject jsonContactedTutor = jsonContactedTutors.getJSONObject(i);

                    int tutorId = jsonContactedTutor.getJSONObject("tutorInfo").getInt("id");
                    String tutorName = jsonContactedTutor.getJSONObject("tutorInfo").getString("name");
                    String tutorPhone = jsonContactedTutor.getJSONObject("tutorInfo").getString("phoneNumber");
                    boolean tutorReported = jsonContactedTutor.getBoolean("reported");

                    contactedTutors.add(new ContactedTutor(tutorId, tutorName, tutorPhone, tutorReported));
                }

                currentUser = new Student(id, UUID, schoolId, schoolName, name, phone, latitude, longitude, userType, contactedTutors);
            }
            else if (userType == UserType.TUTOR){
                //TODO: Tutors have reviews and postings that should be parsed here. This part is exactly the same as getTutorById
            }

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

    public static void parseDegreesResponse(JSONObject response, DegreesActivity context){
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

            context.populateDegreeList(degreeMap);
        }
        catch (Exception e) {
            Log.d("REST_ERROR", e.toString());
        }
    }
    public static void parseSchoolsResponse(JSONObject response, SignUpActivity context){
        final ArrayList<School> schoolList = new ArrayList<School>();
        try{
            Log.d("SCHOOLSJSON", response.toString());
            JSONArray jsonSchools = response.getJSONObject("data").getJSONArray("schools");
            for (int i = 0; i < jsonSchools.length(); i++){
                JSONObject jsonSchool = jsonSchools.getJSONObject(i);
                int id = jsonSchool.getInt("id");
                String name = jsonSchool.getString("name");
                School newSchool = new School(id, name);
                schoolList.add(newSchool);
            }
            context.setSchoolList(schoolList);
            context.populateSignUp();
        } catch (Exception e){
            Log.d("REST_ERROR", e.toString());
        }
    }


    // Parse Postings to tutorPostings

    public static void parsePostingsResponse(JSONObject response, TutorsListActivity context){
        final int schoolId = Context.getUser().getSchoolId();

        final ArrayList<TutorPosting> tutorPostings = new ArrayList<TutorPosting>();

        try {
            Log.d("POSTINGSJSON", response.toString());

            JSONArray jsonPostings = response.getJSONObject("data").getJSONArray("tutorPostings");

            for (int i = 0; i < jsonPostings.length(); i++) {
                JSONObject jsonPosting = jsonPostings.getJSONObject(i);

                int id = jsonPosting.getInt("id");
                int tutorId = jsonPosting.getInt("tutorId");
                String tutorName = jsonPosting.getString("tutorName");
                double lat = jsonPosting.getDouble("lat");
                double lon = jsonPosting.getDouble("lon");
                double price = jsonPosting.getDouble("price");
                String postText = jsonPosting.getString("postText");

                JSONArray jsonCourses = jsonPosting.getJSONArray("courses");
                ArrayList<Course> courses = new ArrayList<Course>();

                for (int j = 0; j < jsonCourses.length(); j++) {
                    JSONObject jsonCourse = jsonCourses.getJSONObject(j);

                    int courseId = jsonCourse.getInt("id");
                    String courseName = jsonCourse.getString("name");

                    courses.add(new Course(courseId, courseName, schoolId));
                }

                TutorPosting posting = new TutorPosting(id, courses, postText, price, tutorId, tutorName, lat, lon);
                tutorPostings.add(posting);
            }

            context.setTutorPostings(tutorPostings);
            context.populatePostings();
        }
        catch (Exception e) {
            Log.d("REST_ERROR", e.toString());
        }
    }

    // Parse tutor response

    public static void parseTutorByIdResponse(JSONObject response, TutorPostingActivity context) {
        try {
            JSONObject jsonUser = response.getJSONObject("data").getJSONObject("user");

            int id = jsonUser.getInt("id");
            int schoolId = jsonUser.getInt("schoolId");
            String schoolName = "University of Waterloo"; //TODO: Make this schoolName not hardcoded
            String name = jsonUser.getString("name");
            String phone = jsonUser.getString("phoneNumber");
            double latitude = jsonUser.getDouble("lat");
            double longitude = jsonUser.getDouble("lon");
            double rating = 3.0; // jsonUser.getDouble("avgRating"); TODO: Uncomment this once rav adds avgRating

            JSONArray jsonReviews = jsonUser.getJSONArray("reviews");
            ArrayList<UserReview> reviews = new ArrayList<UserReview>();

            for (int j = 0; j < jsonReviews.length(); j++) {
                JSONObject jsonReview = jsonReviews.getJSONObject(j);

                int reviewUserId = jsonReview.getInt("userId");
                int reviewTutorId = jsonReview.getInt("tutorId");
                String reviewerName = jsonReview.getString("studentName");
                String reviewText = jsonReview.getString("reviewText");
                double reviewRating = jsonReview.getInt("rating");

                reviews.add(new UserReview(reviewUserId, reviewerName, reviewTutorId, reviewText, reviewRating));
            }

            ArrayList<TutorPosting> postings = new ArrayList<TutorPosting>();
            JSONArray jsonPostings = jsonUser.getJSONArray("postings");

            for (int i = 0; i < jsonPostings.length(); i++) {
                JSONObject jsonPosting = jsonPostings.getJSONObject(i);

                int postingId = jsonPosting.getInt("id");
                double price = jsonPosting.getDouble("price");
                String postText = jsonPosting.getString("postText");

                ArrayList<Course> courses = new ArrayList<Course>();

                //TODO: Commented out until this endpoint also returns courses
                /*
                JSONArray jsonCourses = jsonPosting.getJSONArray("courses");

                for (int j = 0; j < jsonCourses.length(); j++) {
                    JSONObject jsonCourse = jsonCourses.getJSONObject(j);

                    int courseId = jsonCourse.getInt("id");
                    String courseName = jsonCourse.getString("name");

                    courses.add(new Course(courseId, courseName, schoolId));
                }
                */

                TutorPosting posting = new TutorPosting(postingId, courses, postText, price, id, name, latitude, longitude);
                postings.add(posting);
            }

            Tutor tutor = new Tutor(id, "", schoolId, schoolName, name, phone, latitude, longitude, UserType.TUTOR, postings, reviews, rating);
            context.setTutor(tutor);
            context.populateTutorInfo();
        }
        catch(Exception e) {
            Log.d("REST_ERROR", e.toString());
        }
    }
}
