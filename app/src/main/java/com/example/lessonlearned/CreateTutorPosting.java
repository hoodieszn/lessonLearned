package com.example.lessonlearned;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.example.lessonlearned.Singletons.Context;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CreateTutorPosting extends AppCompatActivity {
    private int degreeSelected;
    private List<Course> courses = new ArrayList<>();
    private List<Course> selectedCourses = new ArrayList<>();
    private Tutor tutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tutor_posting);

        tutor = (Tutor)Context.getUser();
        
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
        Bundle extras = getIntent().getExtras();
        degreeSelected = extras.getInt("id");
        String degreeName = extras.getString("name");

        TextView degreeCourses = findViewById(R.id.degreeCourses);

        degreeCourses.setText("Courses for " + degreeName + ":");

        //get courses for the degree
        try {
            RESTClientRequest.getCoursesByDegree(this, degreeSelected);
        }
        catch (JSONException e){
            Log.d("JSONException", e.toString());
        }
        initCloseButton();
        initSubmitButton();
    }

    public void setCourseList(List<Course> coursesForThisDegree) {
        this.courses = coursesForThisDegree;
    }

    public void initCourseAutofill() {
        List<String> courseNamesForDegree = new ArrayList<>();
        final List<Integer> courseIdsForDegree = new ArrayList<>();
        for(Course c : courses) {
            courseNamesForDegree.add(c.getName());
            courseIdsForDegree.add(c.getId());
        }

        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_courses);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, courseNamesForDegree);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String courseName = parent.getItemAtPosition(position).toString();
                int courseId = courseIdsForDegree.get(position);
                textView.getText().clear();
                Course selectedCourse = new Course(courseId, courseName, tutor.getSchoolId());
                addSelectedCourse(selectedCourse);
            }
        });
    }
    private void addSelectedCourse(Course course) {
        //add course to view
        selectedCourses.add(course);
        final Course currentCourse = course;
        FlexboxLayout courseContainer = findViewById(R.id.courseContainer);
        ViewGroup.LayoutParams params = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((RelativeLayout.LayoutParams) params).setMargins(5, 5, 5, 5);
        final Button courseName = new Button(this);
        courseName.setLayoutParams(params);
        courseName.setText(course.getName() + "  X");
        courseName.setBackgroundResource(R.drawable.course_code_bg);
        courseName.setTextSize(14);
        courseName.setTextColor(Color.WHITE);
        courseContainer.addView(courseName);
        courseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove course from selected courses list
                selectedCourses.remove(currentCourse);
                courseName.setVisibility(View.GONE);
            }
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
                    Intent tutorProfileIntent = new Intent(CreateTutorPosting.this, TutorProfileActivity.class);
                    tutorProfileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(tutorProfileIntent);
                }
                return null;
            }
        };
    }
    private void initSubmitButton() {
        Button submit = findViewById(R.id.submit);
        final EditText aboutText = findViewById(R.id.aboutText);
        final EditText priceText = findViewById(R.id.price);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postText = aboutText.getText().toString();
                String price = priceText.getText().toString();
                try{
                    RESTClientRequest.postPosting(
                            tutor.getId(),
                            tutor.getName(),
                            tutor.getLatitude(),
                            tutor.getLongitude(),
                            tutor.getId(),
                            price,
                            postText,
                            selectedCourses,
                            CreateTutorPosting.this);
                } catch (JSONException e){
                    Log.d("JSONException", e.toString());
                    Toast.makeText(CreateTutorPosting.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}