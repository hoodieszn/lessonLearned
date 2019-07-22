package com.example.lessonlearned;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TutorPostingActivity extends Activity {

    private int postingId;
    private Tutor tutor;
    private TutorPosting posting;
    private List<Course> courses = new ArrayList<>();
    private List<String> comments = new ArrayList<>();
    private List<String> commentOwners = new ArrayList<>();

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_posting);

        //set the popup width and height based on the device's dimensions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        // Get tutor id from intent, then fetch tutor information
        Intent tutorInfo = getIntent();
        postingId = tutorInfo.getIntExtra("postingId", 1);
        int tutorId = tutorInfo.getIntExtra("tutorId", 1);

        // Fetch Postings from server
        try {
            RESTClientRequest.getTutorById(tutorId, this);
        }
        catch (JSONException e){
            Log.d("JSONException", e.toString());
        }

    }

    public void populateTutorInfo(){
        TextView tutorName = findViewById(R.id.Name);
        tutorName.setText(tutor.getName());

        TextView tutorSchool = findViewById(R.id.tutorSchool);
        tutorSchool.setText(tutor.getSchoolName());


        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating((float)tutor.getAverageRating());

        TextView tutorPrice = findViewById(R.id.tutorPrice);

        for (int i=0; i< tutor.getPostings().size(); i++){
            TutorPosting currentPosting = tutor.getPostings().get(i);
            if (currentPosting.getId() == postingId){
                posting = currentPosting;
            }
        }

        initContactButton();

        //populate courses
        courses = posting.getPostingCourses();
        initCourses();

        TextView postText = findViewById(R.id.postText);
        postText.setText(posting.getPostText());

        if (posting != null){
            tutorPrice.setText(String.format("$%s / hour", Double.toString(posting.getPrice())));
        }
        initComments();
    }

    private void initContactButton() {
        Button contact_button = findViewById(R.id.contactButton);

        contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + tutor.getPhone()));
                intent.putExtra("sms_body", "Hello, I would like to arrange a time for tutoring.");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
    private void initCourses() {
        FlexboxLayout courseContainer = findViewById(R.id.courseContainer);
        ViewGroup.LayoutParams params = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((RelativeLayout.LayoutParams) params).setMargins(5, 5, 5, 5);

        for (Course c : courses) {
            TextView courseName = new TextView(this);
            courseName.setLayoutParams(params);
            courseName.setText(c.getName());
            courseName.setBackgroundResource(R.drawable.course_code_bg);
            courseName.setTextSize(14);
            courseName.setTextColor(Color.WHITE);
            courseContainer.addView(courseName);
        }
    }
    private void initComments(){
        for(int i=0; i< tutor.getReviews().size(); i++){
            comments.add(tutor.getReviews().get(i).getComment());
            commentOwners.add("- " + tutor.getReviews().get(i).getStudentName());
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
