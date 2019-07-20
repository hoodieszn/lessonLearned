package com.example.lessonlearned;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lessonlearned.Models.User;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.Singletons.Context;


public abstract class BaseActivity extends AppCompatActivity {
    protected final void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        Toolbar toolBar = findViewById(R.id.app_bar);
        setSupportActionBar(toolBar);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem profileIcon = menu.findItem(R.id.profileIcon);

        if (Context.getUser() != null){
            profileIcon.setVisible(true);
        }
        else {
            profileIcon.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.profileIcon) {
            // Handle My Profile click

            User user = Context.getUser();

            if (user != null){
                if (user.getUserType() == UserType.student){
                    // Navigate to student Profile page
                    Intent studentProfileIntent = new Intent(BaseActivity.this, StudentProfileActivity.class);
                    startActivity(studentProfileIntent);
                }
                else if (user.getUserType() == UserType.tutor){
                    // Navigate to Tutor Profile page
                    Intent tutorProfileIntent = new Intent(BaseActivity.this, TutorProfileActivity.class);
                    startActivity(tutorProfileIntent);
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


