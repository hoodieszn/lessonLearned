package com.lessonlearned.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lessonlearned.app.Models.UserType;
import com.lessonlearned.app.Singletons.Context;
import com.lessonlearned.app.Services.RESTClientRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String verificationId = "";
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private EditText codeText;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);


        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNumber = "+1" + phoneNumber;
        sendVerificationCode(phoneNumber);

        progressBar = findViewById(R.id.progressbar);
        codeText = findViewById(R.id.codeText);
        login = findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = codeText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {
                    codeText.setError("Enter valid code...");
                    codeText.requestFocus();
                    return;
                }

                verifyCode(code);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }


    private void sendVerificationCode(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );

    }

    private void verifyCode(String code){
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e){
            Toast toast = Toast.makeText(this, "Verification Code is wrong: " + e, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete() && task.isSuccessful()){

                            // Sign in is complete, get the User Object
                            try {
                                RESTClientRequest.getUser(goToLandingPage(), VerifyPhoneActivity.this);
                            }
                            catch (JSONException e){
                                // Log.d("JSONException", e.toString());
                            }
                        }
                        else{
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            verificationId = s;
            super.onCodeSent(s, forceResendingToken);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public Callable<Void> goToLandingPage(){
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (Context.getUser().getUserType() == UserType.student) {
                    Intent degreeIntent = new Intent(VerifyPhoneActivity.this, DegreesActivity.class);
                    degreeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(degreeIntent);
                }
                else if (Context.getUser().getUserType() == UserType.tutor){
                    Intent tutorProfileIntent = new Intent (VerifyPhoneActivity.this, TutorProfileActivity.class);
                    tutorProfileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(tutorProfileIntent);
                }
                else {
                    Toast.makeText(VerifyPhoneActivity.this, "Error Authenticating", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        };
    }
}
