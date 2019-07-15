package com.example.lessonlearned;

import android.content.Intent;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView login = findViewById(R.id.login);
        final TextView signUp = findViewById(R.id.textView4);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        final EditText numberText = findViewById(R.id.phoneText);

        numberText.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorLocation;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursorLocation = s.length()-numberText.getSelectionStart();
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here =D
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
                String phone = string.replaceAll("[^\\d]", "");
                if (!editedFlag) {
                    if (phone.length() >= 6 && !backspacingFlag) {
                        editedFlag = true;
                        //Formatting the string to make it look more readable by adding brackets and dashes
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-" + phone.substring(6);
                        numberText.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        numberText.setSelection(numberText.getText().length()-cursorLocation);
                    // This is the case where only 3 to 5 digits have been added so we only add brackets and no dashes
                    } else if (phone.length() >= 3 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" +phone.substring(0, 3) + ") " + phone.substring(3);
                        numberText.setText(ans);
                        numberText.setSelection(numberText.getText().length()-cursorLocation);
                    }
                } else {
                    editedFlag = false;
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String number = numberText.getText().toString().trim();
                number = number.replace("-" , "" );
                number = number.replace("(", "");
                number = number.replace(")", "");
                number = number.replace(" ", "");

                if(number.isEmpty() || number.length() != 10){
                    numberText.requestFocus();
                    Toast.makeText(MainActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent degreeIntent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
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
            Intent degreeIntent = new Intent(MainActivity.this, DegreesActivity.class);
            degreeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(degreeIntent);
        }
    }
}



