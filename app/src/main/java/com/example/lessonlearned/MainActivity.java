package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    protected final void onCreate(Bundle savedInstanceState) {
        // After SignUp/Login bring them here
        // (We need to actually show them a list of schools but lets just do this for the first prototype)
        super.onCreate(savedInstanceState, R.layout.activity_main);
        Intent degreeIntent = new Intent(this, DegreesActivity.class);
        startActivity(degreeIntent);
    }
}



