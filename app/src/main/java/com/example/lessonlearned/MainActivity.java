package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
//        setContentView(R.layout.activity_main);
        Intent degreeIntent = new Intent(this, DegreesActivity.class);
        startActivity(degreeIntent);
    }


}



