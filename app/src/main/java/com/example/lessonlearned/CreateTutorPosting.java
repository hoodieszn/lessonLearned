package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        initDegreeSpinner();
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (Context.getUser() != null){
//
//            // Fetch Degrees from server
//            try {
//                RESTClientRequest.getDegreesList(this);
//            }
//            catch (JSONException e){
//                Log.d("JSONException", e.toString());
//            }
//        }
//    }
    private void initDegreeSpinner() {
        Spinner degreesSpinner = findViewById(R.id.degrees);
//        Degree test = new Degree(1, "Engineering", 1, "University of Waterloo");
//        Degree test2 = new Degree(2, "Math", 1, "University of Waterloo");
//        Degree test3 = new Degree(2, "Arts", 1, "University of Waterloo");
//
//        degrees.add(test);
//        degrees.add(test2);
//        degrees.add(test3);

        List<String> degreeNames = new ArrayList<>();

        for(Degree degree : degrees) {
            degreeNames.add(degree.getName());
        }
//        degreeNames.add(degrees.get(0).getName());
//        degreeNames.add(degrees.get(1).getName());
//        degreeNames.add(degrees.get(2).getName());


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
        closeButton.setOnClickListener(new View.OnClickListener(){

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
}
