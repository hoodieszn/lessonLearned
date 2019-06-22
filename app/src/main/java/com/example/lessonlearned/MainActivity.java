package com.example.lessonlearned;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView login = findViewById(R.id.login);
        final TextView numberText = findViewById(R.id.phoneText);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String number = numberText.getText().toString().trim();

                if(number.isEmpty() || number.length() != 10){
                    numberText.requestFocus();
                    Toast.makeText(MainActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();

                }else {
                    Intent degreeIntent = new Intent(MainActivity.this, VerifyPhone.class);
                    degreeIntent.putExtra("phoneNumber", number);
                    startActivity(degreeIntent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent degreeIntent = new Intent(MainActivity.this,
                    DegreesActivity.class);
            degreeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(degreeIntent);
        }
    }
}



