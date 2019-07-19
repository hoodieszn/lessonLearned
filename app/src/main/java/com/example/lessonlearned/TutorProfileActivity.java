package com.example.lessonlearned;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.example.lessonlearned.Singletons.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TutorProfileActivity extends AppCompatActivity {

    private User user;
    private List<TutorPosting> userPostings = new ArrayList<>();
    public List<TutorPosting> getTutorPostings() {
        return userPostings;
    }

    public void setTutorPostings(List<TutorPosting> userPostings) {
        this.userPostings = userPostings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        user = Context.getUser();
        // Fetch Postings from server
        try {
            RESTClientRequest.getTutorPostings(user.getId(), this);
        }
        catch (JSONException e){
            Log.d("JSONException", e.toString());
        }
    }

    public void populateTutorProfile() {

    }

}
