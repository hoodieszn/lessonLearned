package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.example.lessonlearned.Singletons.Context;
import java.util.ArrayList;
import java.util.List;

public class TutorProfileActivity extends BaseActivity implements TutorsViewAdapter.ItemClickListener {

    private User user;
    private List<TutorPosting> userPostings = new ArrayList<>();

    TutorsViewAdapter tutorPostingAdapter;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;

    public void setTutorPostings(List<TutorPosting> userPostings) {
        this.userPostings = userPostings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        user = Context.getUser();
        // Fetch Postings from server
        RESTClientRequest.getTutorPostings(user.getId(), this);
    }

    public void populateTutorProfile() {
        TextView userName = findViewById(R.id.tutorName);
        userName.setText(user.getName());

        TextView school = findViewById(R.id.school);
        school.setText(user.getSchoolName());

        TextView phone = findViewById(R.id.phone);
        phone.setText(user.getPhone());

        initActivePostings();

    }

    private void initActivePostings() {
        layoutManager = new LinearLayoutManager(TutorProfileActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView = findViewById(R.id.tutorPostings);

        tutorPostingAdapter = new TutorsViewAdapter(TutorProfileActivity.this, userPostings);
        tutorPostingAdapter.setClickListener(TutorProfileActivity.this);

        recyclerView.setAdapter(tutorPostingAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent tutorProfile = new Intent(getApplicationContext(), TutorPostingActivity.class);

        TutorPosting currentPosting = tutorPostingAdapter.getItem(position);
        tutorProfile.putExtra("tutorId", currentPosting.getTutorId());
        tutorProfile.putExtra("postingId", currentPosting.getId());
        startActivity(tutorProfile);
    }
}
