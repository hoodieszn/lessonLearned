package com.example.lessonlearned;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.lessonlearned.Models.Student;
import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Singletons.Context;

public class StudentProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        TextView studentName = this.findViewById(R.id.studentName);
        TextView studentSchool = this.findViewById(R.id.studentSchool);
        TextView studentPhone = this.findViewById(R.id.studentPhone);

        Student user = (Student)Context.getUser();

        if (user != null){
            studentName.setText(user.getName());
            studentSchool.setText(Integer.toString(user.getSchoolId()));
            studentPhone.setText(user.getPhone());
        }
    }

}
