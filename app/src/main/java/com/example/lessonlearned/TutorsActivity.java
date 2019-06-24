package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TutorsActivity extends BaseActivity implements TutorViewAdapter.ItemClickListener{
    public ArrayList<String> sortOptions;
    private int sortSelected;

    private String institutionName;
    private int institutionId;

    private String categoryName;
    private int categoryId;

    List<Tutor> tutorList;
    TutorViewAdapter tutorListAdapter;

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
                            return Double.compare(lhs.distance, rhs.distance);
                        else
                            return Double.compare(lhs.price, rhs.price);
                    }
                });

                tutorListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        // Temporarily initializing tutorList with hardcoding tutors
        tutorList = Arrays.asList(new Tutor("2222222222", "John", "University of Waterloo", Arrays.asList("CS449", "CS370"), 10, 3),
                new Tutor("3333333333", "Gill", "University of Waterloo", Arrays.asList("CS136", "MATH128"), 20, 5),
                new Tutor("4444444444", "Bob", "University of Waterloo", Arrays.asList("MATH239", "CS240", "STAT231", "STAT232", "STAT233", "STAT234", "STAT235"), 5, 7),
                new Tutor("5555555555", "Erin", "University of Waterloo", Arrays.asList("MATH330", "CS250", "STAT241"), 5, 1),
                new Tutor("6666666666", "Mike", "University of Waterloo", Arrays.asList("MATH249", "CS270", "STAT331"), 15, 0.5),
                new Tutor("7777777777", "Samantha", "University of Waterloo", Arrays.asList("MATH239", "CS240", "STAT231"), 8, 9));

        RecyclerView recyclerView = findViewById(R.id.tutors);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tutorListAdapter = new TutorViewAdapter(this, tutorList);
        tutorListAdapter.setClickListener(this);
        recyclerView.setAdapter(tutorListAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent tutorProfile = new Intent(getApplicationContext(), tutorProfile.class);
        Tutor currentTutor = tutorListAdapter.getItem(position);
        tutorProfile.putExtra("name", currentTutor.name);
        tutorProfile.putExtra("phone", currentTutor.phone);
        tutorProfile.putExtra("institution", currentTutor.institution);
        startActivity(tutorProfile);
//        Toast.makeText(this, "You clicked " + tutorListAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

}
