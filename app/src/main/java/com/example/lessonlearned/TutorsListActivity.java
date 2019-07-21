package com.example.lessonlearned;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Models.Student;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Services.RESTClient;
import com.example.lessonlearned.Singletons.Context;
import com.example.lessonlearned.Services.RESTClientRequest;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;



public class TutorsListActivity extends BaseActivity implements TutorsViewAdapter.ItemClickListener {

    // Sort Options
    private ArrayList<String> sortOptions;
    private int sortSelected;
    private Spinner sortDropdown;
    private FloatingActionButton fab;

    // List of Tutor Postings
    List<TutorPosting> tutorPostings;

    //List of Courses
    List<Course> courses;
    String selectedCourse;

    // View Elements and Layouts
    TutorsViewAdapter tutorPostingAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    TextView filterText;

    // Indicator for if a Posting has been clicked
    final static int tutorProfileRequest = 0;
    RelativeLayout dimmer;

    // Intent information
    private int degreeId;
    private String degreeName;

    private static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_tutorlist);

        // Find out which degree was selected
        Intent categoryIntent = getIntent();
        degreeId = categoryIntent.getIntExtra("degreeId", 0);
        degreeName = categoryIntent.getStringExtra("degreeName");
        String schoolName = Context.getUser().getSchoolName();

        // Get view/layout elements on screen
        dimmer = findViewById(R.id.dimTutorList);
        recyclerView = findViewById(R.id.tutors);
        filterText = findViewById(R.id.filterText);

        // Set title for page
        TextView tutorsTitle = this.findViewById(R.id.tutorsTitle);
        tutorsTitle.setText(schoolName + " " + degreeName + " Tutors in your area");

        // Load Sort Options and Link Sort Adapter
        sortOptions = new ArrayList<String>() {{
            add("Price");
            add("Distance");
        }};

        sortDropdown = this.findViewById(R.id.sortBtn);

        //init FAB
        fab = this.findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new SimpleSearchDialogCompat(TutorsListActivity.this, "Search...",
                        "What course do you need help with...?", null, getCourseData(),
                        new SearchResultListener<Course>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   Course item, int position) {
                                selectedCourse = item.getTitle();

                                if(tutorPostingAdapter != null){
                                    tutorPostingAdapter.getFilter().filter(selectedCourse);
                                }

                                if(selectedCourse.length() != 0){
                                    filterText.setVisibility(View.VISIBLE);
                                    filterText.setText("Searching for " +  selectedCourse);
                                }else {
                                    filterText.setVisibility(View.GONE);
                                }

                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        // Check for location Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request it if we dont have it
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_MULTIPLE_REQUEST);
        }
        else {
            handleLocation();
        }

        // Fetch Postings, Courses from server
        try {
            RESTClientRequest.getPostingsForDegree(degreeId, this);
            RESTClientRequest.getCoursesForDegree(degreeId, this);
        }
        catch (JSONException e){
            Log.d("JSONException", e.toString());
        }
    }

    // Get and set postings object
    public List<TutorPosting> getTutorPostings() {
        return tutorPostings;
    }

    public void setTutorPostings(List<TutorPosting> tutorPostings) {
        // Filter out any postings from a user we have reported

        List<Integer> reportedTutors = ((Student)Context.getUser()).getReportedTutors();
        List<Integer> removeIndicies = new ArrayList<>();

        for (int i = 0; i < tutorPostings.size(); i++){
            TutorPosting posting = tutorPostings.get(i);
            if(reportedTutors.contains(posting.getTutorId())){
                removeIndicies.add(i);
            }
        }

        for(int i : removeIndicies){
            tutorPostings.remove(i);
        }
        this.tutorPostings = tutorPostings;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        fab.show();
    }


    // Populate postings with http response
    public void populatePostings(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            layoutManager = new LinearLayoutManager(TutorsListActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            tutorPostingAdapter = new TutorsViewAdapter(TutorsListActivity.this, tutorPostings);
            tutorPostingAdapter.setClickListener(TutorsListActivity.this);

            recyclerView.setAdapter(tutorPostingAdapter);

            // Listeners for different sort options
            initSortListeners();
        }
    }

    private void initSortListeners(){
        final ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(this, R.layout.sort_spinner, sortOptions);

        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortDropdown.setAdapter(sortAdapter);

        // Default Sort Option Selected
        sortSelected = 0;
        sortDropdown.setSelection(sortSelected);

        sortDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String selected = parentView.getItemAtPosition(position).toString();

                // Default sort is by price

                Collections.sort(tutorPostings, new Comparator<TutorPosting>() {
                    @Override
                    public int compare(TutorPosting lhs, TutorPosting rhs) {
                        if (selected == "Distance")
                            return Double.compare(lhs.getDistance(), rhs.getDistance());
                        else
                            return Double.compare(lhs.getPrice(), rhs.getPrice());
                    }
                });

                if (tutorPostingAdapter != null) tutorPostingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }

        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent tutorProfile = new Intent(getApplicationContext(), TutorPostingActivity.class);

        TutorPosting currentPosting = tutorPostingAdapter.getItem(position);
        tutorProfile.putExtra("tutorId", currentPosting.getTutorId());
        tutorProfile.putExtra("postingId", currentPosting.getId());

        dimmer.setVisibility(View.VISIBLE);
        startActivityForResult(tutorProfile, tutorProfileRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == tutorProfileRequest) {
            if (resultCode == RESULT_CANCELED) {
                dimmer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleLocation();
    }

    private void handleLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            try {
                LocationManager lm = (LocationManager)getSystemService(android.content.Context.LOCATION_SERVICE);

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        Context.getUser().setLatitude(location.getLatitude());
                        Context.getUser().setLongitude(location.getLongitude());
                        // Toast.makeText(TutorsListActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
            catch (SecurityException e){
                Toast.makeText(TutorsListActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }

            if (tutorPostings != null && tutorPostings.size() != 0) populatePostings();
        }
    }


    private ArrayList<Course> getCourseData(){
        return new ArrayList<Course>(courses);
    }
}
