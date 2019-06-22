package com.example.lessonlearned;

import android.content.Intent;
//<<<<<<< HEAD
//=======
//import android.support.v7.app.AppCompatActivity;
//>>>>>>> 0788b08cf290e8a98002f465b6344c8a0eb87764
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends BaseActivity {

//<<<<<<< HEAD
//    protected final void onCreate(Bundle savedInstanceState) {
//        // After SignUp/Login bring them here
//        // (We need to actually show them a list of schools but lets just do this for the first prototype)
//        super.onCreate(savedInstanceState, R.layout.activity_main);
//        Intent degreeIntent = new Intent(this, DegreesActivity.class);
//        startActivity(degreeIntent);
//=======
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView signUp = findViewById(R.id.textView6);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Loginpage.class);
                startActivity(myIntent);
            }
        });
    }
}



