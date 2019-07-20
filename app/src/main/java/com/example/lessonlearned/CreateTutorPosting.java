package com.example.lessonlearned;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.example.lessonlearned.Singletons.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CreateTutorPosting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tutor_posting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button submit = findViewById(R.id.button);
        final List<Course> courses = new ArrayList<Course>();
        Course newCourse = new Course(1, "CS 454", 1);
        courses.add(newCourse);
        final String postText = "LOL!";
        final String price = "5.0";
        final int tutorId = Context.getUser().getId();
        final String tutorName = "kandana";
        final double lat = 0;
        final double lon = 0;
        final int id = newCourse.getId();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    RESTClientRequest.postPosting(id, tutorName, lat, lon, tutorId, price, postText, courses, CreateTutorPosting.this);
                } catch (JSONException e){
                    Log.d("JSONException", e.toString());
                }
            }
        });
    }
    public Callable<Void> goTutorPage(){
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (Context.getUser().getUserType() == UserType.tutor) {
                    Intent degreeIntent = new Intent(CreateTutorPosting.this, TutorProfileActivity.class);
                    degreeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(degreeIntent);
                }
                return null;
            }
        };
    }
}

