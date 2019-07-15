package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Tutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TutorsActivity extends BaseActivity implements TutorsViewAdapter.ItemClickListener{
    public ArrayList<String> sortOptions;
    private int sortSelected;

    private String institutionName;
    private int institutionId;

    private String categoryName;
    private int categoryId;

    List<Tutor> tutorList;
    TutorsViewAdapter tutorListAdapter;

    final static int tutorProfileRequest = 0;
    RelativeLayout dimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_tutorlist);

        // For now just getting the institution and category selected
        Intent categoryIntent = getIntent();

        institutionName = categoryIntent.getStringExtra("InstitutionName");
        institutionId = categoryIntent.getIntExtra("InstitutionId", 0);

        categoryName = categoryIntent.getStringExtra("CategoryName");
        categoryId = categoryIntent.getIntExtra("CategoryId", 0);

        TextView tutorsTitle = this.findViewById(R.id.tutorsTitle);
        tutorsTitle.setText(institutionName + " " + categoryName + " Tutors in your area");

        sortOptions = new ArrayList<String>() {{
            add("Price");
            add("Distance");
        }};

        final Spinner sortDropdown = this.findViewById(R.id.sortBtn);

        final ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(this, R.layout.sort_spinner, sortOptions);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortDropdown.setAdapter(sortAdapter);

        sortSelected = 0;
        sortDropdown.setSelection(0);


        sortDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String selected = parentView.getItemAtPosition(position).toString();

                //default sort is by price
                Collections.sort(tutorList, new Comparator<Tutor>() {
                    @Override
                    public int compare(Tutor lhs, Tutor rhs) {
                        if (selected == "Distance")
                            return Double.compare(lhs.getDistance(), rhs.getDistance());
                        else
                            return Double.compare(lhs.getPrice(), rhs.getPrice());
                    }
                });

                tutorListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        // Temp: will be rest client call in future
        tutorList = Arrays.asList(new Tutor(1, "Gill", 2, "Mathematics", 20, "3333333333", 43.731548, -79.762421,
                                    Arrays.asList(new Course(2, "CS350", 1), new Course(3, "CS351", 1))),
                        new Tutor(2, "John", 2, "Mathematics", 10, "4444444444", 43.731548, -73.762421,
                                Arrays.asList(new Course(3, "CS349", 1))));

        RecyclerView recyclerView = findViewById(R.id.tutors);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tutorListAdapter = new TutorsViewAdapter(this, tutorList);
        tutorListAdapter.setClickListener(this);
        recyclerView.setAdapter(tutorListAdapter);

        dimmer = findViewById(R.id.dimTutorList);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent tutorProfile = new Intent(getApplicationContext(), TutorProfileActivity.class);
        Tutor currentTutor = tutorListAdapter.getItem(position);
        tutorProfile.putExtra("name", currentTutor.getName());
        tutorProfile.putExtra("phone", currentTutor.getPhone());
        tutorProfile.putExtra("institution", "University of Waterloo");
        tutorProfile.putExtra("price", currentTutor.getPrice());

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

}
