package com.example.lessonlearned.Services;

import android.util.Log;

import com.example.lessonlearned.DegreesActivity;
import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Models.UserReview;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.Singletons.Context;
import com.example.lessonlearned.TutorPostingActivity;
import com.example.lessonlearned.TutorsListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import cz.msebera.android.httpclient.Header;

public class RESTClientRequest {

    public static void getUser(final Callable<Void> callback) throws JSONException {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentFirebaseUser != null) {
            final String UUID = currentFirebaseUser.getUid();

            RESTClient.get("users?firebaseId=" + UUID, null, new JsonHttpResponseHandler() {
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

    public static void getDegrees(final DegreesActivity context) throws JSONException{
        User currentUser = Context.getUser();

        if (currentUser != null){
            int schoolId = currentUser.getSchoolId();

            RESTClient.get(schoolId + "/degrees", null, new JsonHttpResponseHandler() {
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

    public static void getPostingsForDegree(final int degreeId, final TutorsListActivity context) throws JSONException{
        if (Context.getUser() != null) {

            RESTClient.get(degreeId + "/postings", null, new JsonHttpResponseHandler() {
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


    public static void getTutorById(final int tutorId, final TutorPostingActivity context) throws JSONException{
        if (Context.getUser() != null) {

            RESTClient.get("users/" + tutorId, null, new JsonHttpResponseHandler() {
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
}
