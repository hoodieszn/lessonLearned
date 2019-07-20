package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.Singletons.Context;

import java.util.concurrent.Callable;

public class CreateTutorPosting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tutor_posting);
        
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
    }
    public Callable<Void> goTutorPage(){
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (Context.getUser().getUserType() == UserType.tutor) {
                    Intent degreeIntent = new Intent(CreateTutorPosting.this, TutorProfileActivity.class);
                    degreeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(degreeIntent);
                }
                return null;
            }
        };
    }
}
