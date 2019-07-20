package com.example.lessonlearned;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Singletons.Context;
import java.util.ArrayList;
import java.util.List;

public class TutorProfileActivity extends AppCompatActivity {

    private Tutor tutor;
    private List<TutorPosting> activePostings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        tutor = (Tutor)Context.getUser();
        activePostings = tutor.getPostings();
        populateTutorProfile();
    }

    public void populateTutorProfile() {
        TextView currentTutorName = findViewById(R.id.currentTutorName);
        currentTutorName.setText(tutor.getName());

        TextView school = findViewById(R.id.school);
        school.setText(tutor.getSchoolName());

        TextView phone = findViewById(R.id.phone);
        phone.setText(tutor.getPhone());

        initActivePostings();
        initAddPostingButton();

    }

    private void initAddPostingButton() {
        ImageButton addButton = findViewById(R.id.addButton);
        final Intent addPosting = new Intent(getApplicationContext(), CreateTutorPosting.class);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addPosting);
            }
        });
    }
    private void initActivePostings() {
        RecyclerView recyclerView = findViewById(R.id.tutorPostings);
        ActivePostsViewAdapter adapter = new ActivePostsViewAdapter(activePostings, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
