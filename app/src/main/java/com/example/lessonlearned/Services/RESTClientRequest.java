package com.example.lessonlearned.Services;

import android.content.Intent;
import android.util.Log;

import com.example.lessonlearned.DegreesActivity;
import com.example.lessonlearned.MainActivity;
import com.example.lessonlearned.Models.ContactedTutor;
import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.SignUpActivity;
import com.example.lessonlearned.Singletons.Context;
import com.example.lessonlearned.StudentProfileActivity;
import com.example.lessonlearned.TutorPostingActivity;
import com.example.lessonlearned.TutorsListActivity;
import com.example.lessonlearned.VerifyPhoneActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
    public static void postAccount(int id, String firebaseID, int schoolid, String schoolname, String name, String phone, double lat, double longg, UserType userType, List<ContactedTutor> contactedTutors
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
            params.put("postings", contactedTutors);
            try{
                StringEntity entity = new StringEntity(params.toString());
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                RESTClient.post(context, "users?=firebaseId=" + firebaseID, entity, "application/json", new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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

}
