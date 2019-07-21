package com.example.lessonlearned.Services;

import android.util.Log;

import com.example.lessonlearned.CreateTutorPosting;
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
import com.example.lessonlearned.TutorProfileActivity;
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
    public static void parsePostResponse(final Callable<Void> callback){
        try{
            callback.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseUserResponse(String UUID, final Callable<Void> callback, JSONObject response){
        try {
            Log.d("USER", response.toString());

            JSONObject jsonUser = response.getJSONObject("data").getJSONObject("user");

            int id = jsonUser.getInt("id");
            int schoolId = jsonUser.getInt("schoolId");
            String schoolName = jsonUser.getString("schoolName");
            String name = jsonUser.getString("name");
            String phone = jsonUser.getString("phoneNumber");
            double latitude = jsonUser.getDouble("lat");
            double longitude = jsonUser.getDouble("lon");

            String userTypeString = jsonUser.getString("userType");
            UserType userType = userTypeString.equalsIgnoreCase(UserType.student.toString()) ? UserType.student : UserType.tutor;

            User currentUser = null;

            if (userType == UserType.student){

                List<ContactedTutor> contactedTutors = new ArrayList<ContactedTutor>();
                if (jsonUser.has("contactedTutors")){
                    JSONArray jsonContactedTutors = jsonUser.getJSONArray("contactedTutors");
                    for (int i = 0; i < jsonContactedTutors.length(); i++) {
                        JSONObject jsonContactedTutor = jsonContactedTutors.getJSONObject(i);

                        int tutorId = jsonContactedTutor.getJSONObject("tutorInfo").getInt("id");
                        String tutorName = jsonContactedTutor.getJSONObject("tutorInfo").getString("name");
                        String tutorPhone = jsonContactedTutor.getJSONObject("tutorInfo").getString("phoneNumber");
                        boolean tutorReported = jsonContactedTutor.getBoolean("reported");

                        contactedTutors.add(new ContactedTutor(tutorId, tutorName, tutorPhone, tutorReported));
                    }
                }
                currentUser = new Student(id, UUID, schoolId, schoolName, name, phone, latitude, longitude, userType, contactedTutors);
            }
            else if (userType == UserType.tutor){
                double rating = jsonUser.getDouble("avgRating");

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

                    JSONArray jsonCourses = jsonPosting.getJSONArray("courses");

                    for (int j = 0; j < jsonCourses.length(); j++) {
                        JSONObject jsonCourse = jsonCourses.getJSONObject(j);

                        int courseId = jsonCourse.getInt("id");
                        String courseName = jsonCourse.getString("name");

                        courses.add(new Course(courseId, courseName, schoolId));
                    }

                    TutorPosting posting = new TutorPosting(postingId, courses, postText, price, id, name, latitude, longitude, rating);
                    postings.add(posting);
                }

                currentUser = new Tutor(id, UUID, schoolId, schoolName, name, phone, latitude, longitude, UserType.tutor, postings, reviews, rating);
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

    //parse Degrees to Tutor Post
    public static void parseDegreesPostResponse(JSONObject response, CreateTutorPosting context){
        try {
            Log.d("DEGREESJSON", response.toString());

            JSONArray jsonDegrees = response.getJSONObject("data").getJSONArray("degrees");
            List<Degree> degreeList = new ArrayList<>();

            for (int i = 0; i < jsonDegrees.length(); i++) {
                JSONObject jsonDegree = jsonDegrees.getJSONObject(i);

                int id = jsonDegree.getInt("id");
                int schoolId = jsonDegree.getInt("schoolId");
                String name = jsonDegree.getString("name");
                String schoolName = jsonDegree.getString("schoolName");

                degreeList.add(new Degree(id, name, schoolId, schoolName));
            }

            context.populateDegreeList(degreeList);
            context.initDegreeSpinner();
        }
        catch (Exception e) {
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
                double avgRating = jsonPosting.getDouble("avgRating");

                JSONArray jsonCourses = jsonPosting.getJSONArray("courses");
                ArrayList<Course> courses = new ArrayList<Course>();

                for (int j = 0; j < jsonCourses.length(); j++) {
                    JSONObject jsonCourse = jsonCourses.getJSONObject(j);

                    int courseId = jsonCourse.getInt("id");
                    String courseName = jsonCourse.getString("name");

                    courses.add(new Course(courseId, courseName, schoolId));
                }

                TutorPosting posting = new TutorPosting(id, courses, postText, price, tutorId, tutorName, lat, lon, avgRating);
                tutorPostings.add(posting);
            }

            context.setTutorPostings(tutorPostings);
            context.populatePostings();
        }
        catch (Exception e) {
            Log.d("REST_ERROR", e.toString());
        }
    }


    // Parse courses response
    public static void parseCoursesResponse(JSONObject response, TutorsListActivity context){
        final ArrayList<Course> courses = new ArrayList<Course>();

        try {
            Log.d("COURSESJSON", response.toString());

            JSONArray jsonCourses = response.getJSONObject("data").getJSONArray("courses");

            for (int i = 0; i < jsonCourses.length(); i++) {
                JSONObject jsonPosting = jsonCourses.getJSONObject(i);

                int id = jsonPosting.getInt("id");
                String courseName = jsonPosting.getString("name");
                int schoolId = jsonPosting.getInt("schoolId");

                Course course = new Course(id, courseName, schoolId);
                courses.add(course);
            }

            context.setCourses(courses);
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
            String schoolName = jsonUser.getString("schoolName");
            String name = jsonUser.getString("name");
            String phone = jsonUser.getString("phoneNumber");
            double latitude = jsonUser.getDouble("lat");
            double longitude = jsonUser.getDouble("lon");
            double avgRating = jsonUser.getDouble("avgRating");

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

                JSONArray jsonCourses = jsonPosting.getJSONArray("courses");

                for (int j = 0; j < jsonCourses.length(); j++) {
                    JSONObject jsonCourse = jsonCourses.getJSONObject(j);

                    int courseId = jsonCourse.getInt("id");
                    String courseName = jsonCourse.getString("name");

                    courses.add(new Course(courseId, courseName, schoolId));
                }

                TutorPosting posting = new TutorPosting(postingId, courses, postText, price, id, name, latitude, longitude, avgRating);
                postings.add(posting);
            }

            Tutor tutor = new Tutor(id, "", schoolId, schoolName, name, phone, latitude, longitude, UserType.tutor, postings, reviews, avgRating);
            context.setTutor(tutor);
            context.populateTutorInfo();
        }
        catch(Exception e) {
            Log.d("REST_ERROR", e.toString());
        }
    }
}
