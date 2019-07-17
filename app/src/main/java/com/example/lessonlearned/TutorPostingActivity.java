package com.example.lessonlearned;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.UserReview;

import java.util.ArrayList;
import java.util.List;

public class TutorPostingActivity extends Activity {

    private List<UserReview> userReviews = new ArrayList<>(); //GET
    private List<Course> postingCourses = new ArrayList<>();
    private double price;
    private Tutor tutor;
    private List<String> comments = new ArrayList<>();
    private List<String> commentOwners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_posting);

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

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Float tutorRating = 4.0f; // 3.0 is from your database

        // To show rating on RatingBar
        ratingBar.setRating(tutorRating);

        Button contact_button = findViewById(R.id.contactButton);

        Intent tutorInfo = getIntent();

        //populate tutor profile
        String name = tutorInfo.getStringExtra("name");
        final String phone = tutorInfo.getStringExtra("phone");
        String institution = tutorInfo.getStringExtra("institution");
        double price = tutorInfo.getDoubleExtra("price", 0);

        TextView tutorName = findViewById(R.id.Name);
        tutorName.setText(name);

        TextView tutorRole = findViewById(R.id.Role);
        tutorRole.setText(institution);

        TextView tutorPrice = findViewById(R.id.tutorPrice);
        tutorPrice.setText(String.format("$%s / hour", price));

        contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phone));
                intent.putExtra("sms_body", "Hello, I would like to arrange a time for tutoring");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        //GET
        UserReview test1 = new UserReview(1, "Josh Freeman", 1, "This is a the test comment 2", 45);
        UserReview test2 = new UserReview(2, "Haley Freeman", 1, "This is a the test comment 2", 45);
        userReviews.add(test1);
        userReviews.add(test2);

        initComments();
    }

    private void initComments(){
        for(int i=0; i<userReviews.size(); i++){
            comments.add(userReviews.get(i).getComment());
            commentOwners.add("-" + userReviews.get(i).getStudentName());
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView commentSection = findViewById(R.id.commentSection);
        CommentsViewAdapter adapter = new CommentsViewAdapter(comments, commentOwners);
        commentSection.setAdapter(adapter);
        commentSection.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(Activity.RESULT_OK);
        finish();
    }
}
