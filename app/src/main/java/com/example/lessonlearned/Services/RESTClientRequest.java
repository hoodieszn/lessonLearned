package com.example.lessonlearned.Services;

import android.content.Intent;
import android.util.Log;

import com.example.lessonlearned.ActivePostsViewAdapter;
import com.example.lessonlearned.DegreesActivity;
import com.example.lessonlearned.Dialogs.CreatePostingDialog;
import com.example.lessonlearned.MainActivity;
import com.example.lessonlearned.Models.ContactedTutor;
import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Student;
import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.SignUpActivity;
import com.example.lessonlearned.Singletons.Context;
import com.example.lessonlearned.StudentProfileActivity;
import com.example.lessonlearned.TutorPostingActivity;
import com.example.lessonlearned.TutorProfileActivity;
import com.example.lessonlearned.TutorsListActivity;
import com.example.lessonlearned.VerifyPhoneActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class RESTClientRequest {

    // Get user object by firebase logged in
    public static void getUser(final Callable<Void> callback, final VerifyPhoneActivity context) throws JSONException {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentFirebaseUser != null) {
            final String UUID = currentFirebaseUser.getUid();

            RESTClient.get("users?firebaseId=" + UUID, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONParser.parseUserResponse(UUID, callback, response);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Intent degreeIntent = new Intent(context, SignUpActivity.class);
                    context.startActivity(degreeIntent);

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                    Intent degreeIntent = new Intent(context, SignUpActivity.class);
                    context.startActivity(degreeIntent);
                }
            });
        }
    }

    // Get user object by firebase logged in
    public static void getUser(final Callable<Void> callback, final MainActivity context) throws JSONException {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentFirebaseUser != null) {
            final String UUID = currentFirebaseUser.getUid();

            RESTClient.get("users?firebaseId=" + UUID, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONParser.parseUserResponse(UUID, callback, response);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Intent degreeIntent = new Intent(context, SignUpActivity.class);
                    context.startActivity(degreeIntent);

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                    context.stopLoadingState();
                }
            });
        }
    }

    // Get degrees by school id
    public static void getDegrees(final DegreesActivity context) throws JSONException{
        User currentUser = Context.getUser();

        if (currentUser != null){
            int schoolId = currentUser.getSchoolId();

            RESTClient.get(schoolId + "/degrees", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONParser.parseDegreesResponse(response, context);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("REST_ERROR", responseString);
                }
            });

        }
    }
    // Get all schools
    public static void getSchools(final SignUpActivity context) throws JSONException{
            RESTClient.get("schools", null, new JsonHttpResponseHandler(){
                @Override
               public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                   JSONParser.parseSchoolsResponse(response, context);
               }
               @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                    Log.d("REST_ERROR", responseString);
               }
            });


    }
    //Get courses by degree id
    public static void getCoursesByDegreeAndCourse(final CreatePostingDialog context, final int degreeId, final String courseCode) throws JSONException{
        RESTClient.get(degreeId + "/courses" + "?subject=" + courseCode, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("COURSEGET", courseCode);
                JSONParser.parseCoursesByDegreeAndCourse(response, context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("REST_ERROR", responseString);
            }
        });
    }
    // Get degrees by school id
    public static void getDegreesList(final CreatePostingDialog context, int schoolId) throws JSONException{
        RESTClient.get(schoolId + "/degrees", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONParser.parseDegreesPostResponse(response, context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("REST_ERROR", responseString);
            }
        });
    }

    // Get postings by degree id
    public static void getPostingsForDegree(final int degreeId, final TutorsListActivity context) throws JSONException{
        if (Context.getUser() != null) {

            RESTClient.get(degreeId + "/postings", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONParser.parsePostingsResponse(response, context);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("REST_ERROR", responseString);
                }
            });
        }
    }


    // Get postings filtered by a course
    public static void getPostingsForCourse(final int degreeId, final int courseId, final TutorsListActivity context) throws JSONException{
        if (Context.getUser() != null) {

            RESTClient.get(degreeId + "/postings?code="+courseId, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONParser.parsePostingsResponse(response, context);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("REST_ERROR", responseString);
                }
            });
        }
    }


    public static void getCoursesForDegree(final int degreeId, final TutorsListActivity context) throws JSONException {
        RESTClient.get(degreeId + "/courses", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONParser.parseCoursesResponse(response, context);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("REST_ERROR", responseString);
            }
        });
    }

    // Get tutor object by id
    public static void getTutorById(final int tutorId, final TutorPostingActivity context) throws JSONException{
        if (Context.getUser() != null) {

            RESTClient.get("users/" + tutorId, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONParser.parseTutorByIdResponse(response, context);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("REST_ERROR", responseString);
                }
            });
        }
    }

    // Post tutor abuse
    public static void postTutorAbuse(final int tutorId, final String reason, final StudentProfileActivity context)throws JSONException {
        if (Context.getUser() != null) {

            JSONObject params = new JSONObject();
            params.put("userId", Context.getUser().getId());
            params.put("tutorId", tutorId);
            params.put("reportReason", reason);

            try {
                StringEntity entity = new StringEntity(params.toString());
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                RESTClient.post(context, "reportabuse", entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("ABUSERESPONSE", statusCode + ": " + response.toString());

                        ((Student)Context.getUser()).setReportedFlag(tutorId);
                        context.donePost(statusCode);
                        context.updateReported();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("REST_ERROR", responseString);
                        context.donePost(statusCode);
                    }
                });
            }
            catch (UnsupportedEncodingException e){ }
        }
    }
    public static void postPosting(final int degreeId, final Tutor tutor, final double price, final String postText, final List<Course> courses, final CreatePostingDialog context)
    throws JSONException{
        JSONObject params = new JSONObject();

        List<Integer>arr2 = new ArrayList<Integer>();
        for (int i = 0; i < courses.size(); i++){
            arr2.add(courses.get(i).getId());
        }

        params.put("tutorName", tutor.getName());
        params.put("lat", tutor.getLatitude());
        params.put("lon", tutor.getLongitude());
        params.put("userId", tutor.getId());
        params.put("price", price);
        params.put("postText", postText);
        params.put("courses", new JSONArray(arr2));

        try{
            StringEntity entity = new StringEntity(params.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            RESTClient.post(context.getActivity(),   Integer.toString(degreeId) + "/postings", entity, "application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("ADDPOSTINGRESPONSE", statusCode + ": " + response.toString());

                    try {
                        JSONObject jsonPosting = response.getJSONObject("data").getJSONObject("posting");
                        int postingId = jsonPosting.getInt("id");

                        TutorPosting tutorPost = new TutorPosting(postingId, courses, postText, price, tutor.getId(), tutor.getName(), tutor.getLatitude(), tutor.getLongitude(), tutor.getAverageRating());
                        ((Tutor) Context.getUser()).getPostings().add(0, tutorPost);

                        context.refreshPostings();
                    }
                    catch (JSONException e) { }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("REST_ERROR", responseString);
                    System.out.println(statusCode);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                    Log.d("REST_ERROR", response.toString());
                    context.onErrorPosting();
                    System.out.println(statusCode);
                }

            });

        }
        catch (UnsupportedEncodingException e){ }
    }
    public static void postAccount(int id, final String firebaseID, int schoolid, String schoolname, String name, String phone, double lat, double longg, UserType userType, List<ContactedTutor> contactedTutors
    , final SignUpActivity context) throws JSONException{
            JSONObject params = new JSONObject();
            params.put("userType", userType);
            params.put("schoolName", schoolname);
            params.put("schoolId", schoolid);
            params.put("firebaseId", firebaseID);
            params.put("phoneNumber", phone);
            params.put("lat", lat);
            params.put("lon", longg);
            params.put("name", name);
            params.put("contactedTutors", contactedTutors);
            try{
                StringEntity entity = new StringEntity(params.toString());
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                RESTClient.post(context, "users?=firebaseId=" + firebaseID, entity, "application/json", new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONParser.parseUserResponse(firebaseID, context.handleRegister(), response);
                        Log.d("REVIEWRESPONSE", statusCode + ": " + response.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("REST_ERROR", responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                        Log.d("REST_ERROR", response.toString());
                    }

                });
            } catch (UnsupportedEncodingException e){

            }

    }
    // Post tutor review
    public static void postTutorReview(final int tutorId, final String reviewText, final double rating, final StudentProfileActivity context)throws JSONException {
        if (Context.getUser() != null) {

            JSONObject params = new JSONObject();
            params.put("userId", Context.getUser().getId());
            params.put("rating", rating);
            params.put("reviewText", reviewText);

            try {
                StringEntity entity = new StringEntity(params.toString());
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                RESTClient.post(context, tutorId + "/reviews", entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("REVIEWRESPONSE", statusCode + ": " + response.toString());
                        context.donePost(statusCode);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("REST_ERROR", responseString);
                        context.donePost(statusCode);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                        Log.d("REST_ERROR", response.toString());
                        context.donePost(statusCode);
                    }

                });
            }
            catch (UnsupportedEncodingException e){ }
        }
    }

    public static void putLocation(final double lat, final double lon, final int userId, final TutorProfileActivity context)throws JSONException{
        String id = Integer.toString(userId);

        JSONObject params = new JSONObject();
        params.put("lat", lat);
        params.put("lon", lon);

        try {
            StringEntity entity = new StringEntity(params.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            RESTClient.put(context, "users/" + id, entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("TASK", "COMPLETED");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable error) {
                    Log.d("REST_ERROR", responseString);
                }
            });
        }catch (UnsupportedEncodingException e){

        }
    }

    public static void postContactedTutor(final String phone, final int tutorId, final String tutorName, final boolean reported, final TutorPostingActivity context)throws JSONException{
        JSONObject params = new JSONObject();

        params.put("userId", Context.getUser().getId());
        params.put("tutorId", tutorId);

        try{
            StringEntity entity = new StringEntity(params.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            RESTClient.post(context, "contacts?userId=" + Context.getUser().getId() , entity, "application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    ContactedTutor contactedTutor = new ContactedTutor(tutorId, tutorName, phone, reported);
                    ((Student)Context.getUser()).getContactedTutors().add(contactedTutor);

                    Log.d("POSTCONTACTEDRESPONSE", statusCode + ": " + response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("REST_ERROR", responseString);
                    System.out.println(statusCode);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                    Log.d("REST_ERROR", response.toString());
                    System.out.println(statusCode);
                }
            });

        }
        catch (UnsupportedEncodingException e){

        }

    }


    public static void deletePosting(final int postId, final ActivePostsViewAdapter context) throws JSONException{
        RequestParams params = new RequestParams();
        String postingId = Integer.toString(postId);
        RESTClient.delete("postings/" + postingId, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ((Tutor)Context.getUser()).deletePosting(postId);
                context.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable error) {
                Log.d("REST_ERROR", responseString);
            }
        });

    }

}
