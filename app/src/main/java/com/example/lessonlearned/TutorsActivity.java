package com.example.lessonlearned;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class TutorsActivity extends BaseActivity implements TutorViewAdapter.ItemClickListener{
    private String sort = "Distance";

    private String institutionName;
    private int institutionId;

    private String categoryName;
    private int categoryId;


    // This is temporary, we will really be getting this user object from firebase
    public class User {
        String phone;
        String name;
        String institution;
        List<String> courses;
        double price;

        public User(String phone, String name, String institution, List<String> courses, double price){
            this.phone = phone;
            this.name = name;
            this.institution = institution;
            this.courses = courses;
            this.price = price;
        }
    }

    private List<User> tutorList = Arrays.asList(new User("2222222222", "John", "University of Waterloo", Arrays.asList("CS449", "CS370"), 10),
            new User("3333333333", "Gill", "University of Waterloo", Arrays.asList("CS136", "MATH128"), 20),
            new User("4444444444", "Bob", "University of Waterloo", Arrays.asList("MATH239", "CS240", "STAT231", "STAT231", "STAT231", "STAT231", "STAT231"), 5),
            new User("4444444444", "Bob", "University of Waterloo", Arrays.asList("MATH239", "CS240", "STAT231"), 5),
            new User("4444444444", "Bob", "University of Waterloo", Arrays.asList("MATH239", "CS240", "STAT231"), 5),
            new User("4444444444", "Bob", "University of Waterloo", Arrays.asList("MATH239", "CS240", "STAT231"), 5),
            new User("4444444444", "Bob", "University of Waterloo", Arrays.asList("MATH239", "CS240", "STAT231"), 5));
    // UP TO HERE IS TEMP DATA FOR USER


    TutorViewAdapter adapter;

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

        Button sortBtn = this.findViewById(R.id.sortBtn);
        sortBtn.setText(this.sort);

        RecyclerView recyclerView = findViewById(R.id.tutors);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TutorViewAdapter(this, tutorList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

}
