package com.example.lessonlearned;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.lessonlearned.Models.ContactedTutor;
import com.example.lessonlearned.Models.Student;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Singletons.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentProfileActivity extends Activity implements ContactedTutorsViewAdapter.ItemClickListener{

    List<ContactedTutor> contactedTutors;

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
            contactedTutorsAdapter.setClickListener(StudentProfileActivity.this);

            recyclerView.setAdapter(contactedTutorsAdapter);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ContactedTutor currentContactedTutor = contactedTutorsAdapter.getItem(position);
    }

}
