package com.example.lessonlearned;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lessonlearned.Dialogs.ReportAbuseDialog;
import com.example.lessonlearned.Dialogs.ReviewDialog;
import com.example.lessonlearned.Models.ContactedTutor;
import com.example.lessonlearned.Models.Student;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.example.lessonlearned.Singletons.Context;
import com.google.firebase.auth.FirebaseAuth;

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

    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        TextView studentName = findViewById(R.id.studentName);
        TextView studentSchool = findViewById(R.id.studentSchool);
        TextView studentPhone = findViewById(R.id.studentPhone);

        logout = findViewById(R.id.studentLogout);
        recyclerView = findViewById(R.id.contactedTutorsList);

        Student user = (Student)Context.getUser();

        if (user != null){

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Context.setUser(null);

                    Intent login = new Intent(StudentProfileActivity.this, MainActivity.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(login);
                }
            });


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


    public void updateReported(){
        contactedTutors = ((Student)Context.getUser()).getContactedTutors();
        contactedTutorsAdapter.notifyDataSetChanged();
    }

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
            alertDialog.setMessage("Sorry, you can only write a review for a tutor once!");
        }

        alertDialog.show();
    }

}
