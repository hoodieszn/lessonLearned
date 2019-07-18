package com.example.lessonlearned;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lessonlearned.Dialogs.ReportAbuseDialog;
import com.example.lessonlearned.Dialogs.ReviewDialog;
import com.example.lessonlearned.Models.ContactedTutor;
import com.example.lessonlearned.Models.Student;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.example.lessonlearned.Singletons.Context;

import org.json.JSONException;

import java.util.List;

public class StudentProfileActivity extends AppCompatActivity implements ReportAbuseDialog.ReportAbuseDialogListener, ReviewDialog.ReviewDialogListener {

    // Tutors list and current clicked
    private List<ContactedTutor> contactedTutors;
    private int currentSelectedTutorId;

    // View Elements and Layouts
    ContactedTutorsViewAdapter contactedTutorsAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        TextView studentName = this.findViewById(R.id.studentName);
        TextView studentSchool = this.findViewById(R.id.studentSchool);
        TextView studentPhone = this.findViewById(R.id.studentPhone);

        recyclerView = findViewById(R.id.contactedTutorsList);

        Student user = (Student)Context.getUser();

        if (user != null){
            studentName.setText(user.getName());
            studentSchool.setText(user.getSchoolName());
            studentPhone.setText(user.getPhone());

            // Get the contacted tutors from student object
            contactedTutors = user.getContactedTutors();

            layoutManager = new LinearLayoutManager(StudentProfileActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            contactedTutorsAdapter = new ContactedTutorsViewAdapter(StudentProfileActivity.this, contactedTutors);

            recyclerView.setAdapter(contactedTutorsAdapter);
        }
    }

    public void showReportAbuseDialog(int tutorId) {
        currentSelectedTutorId = tutorId;
        DialogFragment dialog = new ReportAbuseDialog();
        dialog.show(getSupportFragmentManager(), "ReportAbuseDialog");
    }

    public void showReviewDialog(int tutorId) {
        currentSelectedTutorId = tutorId;
        DialogFragment dialog = new ReviewDialog();
        dialog.show(getSupportFragmentManager(), "ReviewDialog");
    }

    // Abuse OK Clicked
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String reason) {
        // Post report to server
        try {
            RESTClientRequest.postTutorAbuse(currentSelectedTutorId, reason, this);
        }
        catch (JSONException e){
            Log.d("JSONException", e.toString());
        }
    }

    // Review OK Clicked
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String reason, double rating) {
// Post report to server
        try {
            RESTClientRequest.postTutorReview(currentSelectedTutorId, reason, rating, this);
        }
        catch (JSONException e){
            Log.d("JSONException", e.toString());
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { }

    public void donePost(int statusCode){

        AlertDialog alertDialog = new AlertDialog.Builder(StudentProfileActivity.this).create();

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if (statusCode == 200){
            alertDialog.setMessage("Your review has been sent!");
        }
        else {
            alertDialog.setMessage("There was an error sending your review");
        }

        alertDialog.show();
    }

}
