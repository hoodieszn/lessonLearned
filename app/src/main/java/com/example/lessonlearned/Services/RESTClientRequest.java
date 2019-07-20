package com.example.lessonlearned.Services;

import android.util.ArrayMap;
import android.util.Log;

import com.example.lessonlearned.CreateTutorPosting;
import com.example.lessonlearned.DegreesActivity;
import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Models.UserReview;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.Singletons.Context;
import com.example.lessonlearned.StudentProfileActivity;
import com.example.lessonlearned.TutorPostingActivity;
import com.example.lessonlearned.TutorProfileActivity;
import com.example.lessonlearned.TutorsListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class RESTClientRequest {

    // Get user object by firebase logged in
    public static void getUser(final Callable<Void> callback) throws JSONException {
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
                    Log.d("REST_ERROR", responseString);
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
                        context.donePost(statusCode);
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

    public static void postPosting(final int id, final String tutorName, final double lat, final double lon, final int tutorId, final double price, final String postText, final List<Course> courses, final CreateTutorPosting context)
            throws JSONException{
        JSONObject params = new JSONObject();
        params.put("tutorName", tutorName);
        params.put("lat", lat);
        params.put("lon", lon);
        params.put("tutorId", tutorId);
        params.put("price", price);
        params.put("postText", postText);
        params.put("courses", courses);


        try{
            StringEntity entity = new StringEntity(params.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            RESTClient.post(context, id + "/postings", entity, "application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("REVIEWRESPONSE", statusCode + ": " + response.toString());
                    Tutor tutor = (Tutor)Context.getUser();
                    TutorPosting tutorPost = new TutorPosting(id, courses, postText, price, tutorId, tutorName, lat, lon);
                    List<TutorPosting>tutorPostings = tutor.getPostings();
                    tutorPostings.add(tutorPost);
                    tutor.setPostings(tutorPostings);
                    Context.setUser(tutor);
                    JSONParser.parsePostResponse(context.goTutorPage());

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

}
