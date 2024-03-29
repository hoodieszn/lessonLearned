package com.lessonlearned.app;

import android.content.Intent;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.widget.EditText;

import com.lessonlearned.app.Models.UserType;
import com.lessonlearned.app.Singletons.Context;
import com.lessonlearned.app.Services.RESTClientRequest;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.util.concurrent.Callable;


public class MainActivity extends BaseActivity {

    private ProgressBar spinner;
    private RelativeLayout dimmer;
    private TextView login;
    private EditText numberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        spinner = findViewById(R.id.progressSpinner);
        dimmer = findViewById(R.id.dimMainPage);

        numberText = findViewById(R.id.phoneText);

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
                    Intent phoneIntent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                    phoneIntent.putExtra("phoneNumber", number);
                    startActivity(phoneIntent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            // Disable inputs
            startLoadingState();

            // Already signed in, get the User Object
            try {
                RESTClientRequest.getUser(goToLandingPage(), this);
            }
            catch (JSONException e){
                // Log.d("JSONException", e.toString());
                FirebaseAuth.getInstance().updateCurrentUser(null);
                stopLoadingState();
            }
        }
    }

    public Callable<Void> goToLandingPage(){
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (Context.getUser().getUserType() == UserType.student) {
                    Intent degreeIntent = new Intent(MainActivity.this, DegreesActivity.class);
                    degreeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(degreeIntent);
                }
                else if (Context.getUser().getUserType() == UserType.tutor){
                    Intent tutorProfileIntent = new Intent (MainActivity.this, TutorProfileActivity.class);
                    tutorProfileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(tutorProfileIntent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Error Authenticating", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        };
    }

    public void startLoadingState(){
        login.setVisibility(View.INVISIBLE);
        numberText.setEnabled(false);
        dimmer.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
    }

    public void stopLoadingState(){
        login.setVisibility(View.VISIBLE);
        numberText.setEnabled(true);
        dimmer.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
    }
}



