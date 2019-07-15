package com.example.lessonlearned.Singletons;
import com.example.lessonlearned.Models.Tutor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.*;
import org.json.*;
import cz.msebera.android.httpclient.Header;

public class RESTClient {

    private static final String BASE_URL = "https://lesson-learned-backend.herokuapp.com/api/v1/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}

class RESTClientRequest {

    public static void getUser() throws JSONException{
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentFirebaseUser != null) {
            String UUID = currentFirebaseUser.getUid();

            RESTClient.get("users?firebase_id=" + UUID, null, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    System.out.println(response);

                }
            });
        }
    }

    public static void getSchools() throws JSONException{
        RESTClient.get("schools", null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray schools){
                System.out.println(schools);
            }
        });
    }

    public static void getCourses(int id) throws JSONException{
        String id2 = Integer.toString(id);
        String fullURL = id2+"/courses";
        RESTClient.get(fullURL, null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray courses){
                System.out.println(courses);
            }
        });
    }

    public static void getTutors(int id) throws JSONException{
        String id2 = Integer.toString(id);
        String fullURL = id2+"/tutors";
        RESTClient.get(fullURL, null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray tutors){
                Tutor[] tutorsParsed;
                String s = tutors.toString();
                try {
                    JSONObject reader = new JSONObject(s);
                    JSONArray listOfTutors = reader.getJSONArray("tutors");
                    for (int j = 0; j < listOfTutors.length(); j++){


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getDegrees(int id) throws JSONException{
        String id2 = Integer.toString(id);
        String fullURL = id2+"/degrees";
        RESTClient.get(fullURL, null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray degrees){
                System.out.println(degrees);
            }
        });
    }

    public static void getReviews(int id) throws JSONException{
        String id2 = Integer.toString(id);
        String fullURL = id2+"/reviews";
        RESTClient.get(fullURL, null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray reviews){
                System.out.println(reviews);
            }
        });
    }
}
