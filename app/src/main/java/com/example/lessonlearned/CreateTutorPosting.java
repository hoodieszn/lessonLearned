package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Models.Tutor;
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
    private List<Degree> degrees = new ArrayList<>();
    private Tutor tutor;
    public void populateDegreeList(List<Degree> degreesList) {
        this.degrees = degreesList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tutor_posting);

        tutor = (Tutor)Context.getUser();
        // Fetch Degrees from server
        try {
            RESTClientRequest.getDegreesList(this, tutor.getSchoolId());
        }
        catch (JSONException e){
            Log.d("JSONException", e.toString());
        }
        
        //set the popup width and height based on the device's dimensions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.7), (int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
        initCloseButton();
    }

    public void initDegreeSpinner() {
        Spinner degreesSpinner = findViewById(R.id.degrees);
        List<String> degreeNames = new ArrayList<>();

        for(Degree degree : degrees) {
            degreeNames.add(degree.getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateTutorPosting.this, android.R.layout.simple_spinner_item, degreeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degreesSpinner.setAdapter(adapter);

        degreesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String degree = parent.getItemAtPosition(position).toString();
                Toast.makeText(CreateTutorPosting.this, degree, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }
    private void initCloseButton() {
        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
    /*
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
     */
}