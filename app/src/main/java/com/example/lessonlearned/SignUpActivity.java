package com.example.lessonlearned;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lessonlearned.Models.ContactedTutor;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.Services.RESTClientRequest;

import com.example.lessonlearned.Models.School;
import com.example.lessonlearned.Singletons.Context;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SignUpActivity extends AppCompatActivity {
    public List<School> schools;
    public double lat;
    public double longg;

    public void setSchoolList(List<School>restResponse){
        this.schools = restResponse;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        try {
            RESTClientRequest.getSchools(this);
        }
        catch (JSONException e) {
            // Log.d("JSONException", e.toString());
        }
    }

    public void populateSignUp(){

        EditText phone = findViewById(R.id.phone);
        phone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        final Spinner schoolDropdown = findViewById(R.id.schoolDropdown);
        final Spinner usertypeDropdown = findViewById(R.id.usertypeDropdown);

        List<String> listofSchools = new ArrayList<String>();
        List<String> typesofAccounts = new ArrayList<String>();


        typesofAccounts.add("Select an Account Type");
        typesofAccounts.add(UserType.student.toString().toUpperCase());
        typesofAccounts.add(UserType.tutor.toString().toUpperCase());


        listofSchools.add("Select a School");
        for (int i = 0; i < schools.size(); i++){
            listofSchools.add(schools.get(i).getName());
        }

        final ArrayAdapter<String> schoolDropdownAdapter = new ArrayAdapter<String>(this, R.layout.sort_spinner, listofSchools){
            @Override
            public boolean isEnabled (int position){
                if (position == 0){
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        schoolDropdownAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        schoolDropdown.setAdapter(schoolDropdownAdapter);

        final ArrayAdapter<String> accountDropdownAdapter = new ArrayAdapter<String>(this, R.layout.sort_spinner, typesofAccounts){
            @Override
            public boolean isEnabled (int position){
                if (position == 0){
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        accountDropdownAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        usertypeDropdown.setAdapter(accountDropdownAdapter);

        Button register = findViewById(R.id.button3);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String schoolName;
                String userType;
                EditText username = findViewById(R.id.editText);
                String name = username.getText().toString();
                String phonenumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                phonenumber = phonenumber.substring(2);

                if(schoolDropdown != null && schoolDropdown.getSelectedItem() != null && usertypeDropdown != null && usertypeDropdown.getSelectedItem() != null ) {
                    schoolName = (String)schoolDropdown.getSelectedItem();
                    userType = (String)usertypeDropdown.getSelectedItem();
                    List <ContactedTutor> contactedTutors = new ArrayList<ContactedTutor>();
                    String firebaseId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    int schoolID = 1;
                    for (int i = 0; i < schools.size(); i++){
                        if (schoolName == schools.get(i).getName()){
                            schoolID = schools.get(i).getId();
                        }
                    }


                    if (userType.compareToIgnoreCase(UserType.student.toString()) == 0){

                            try{
                                RESTClientRequest.postAccount(1, firebaseId, schoolID, schoolName, name, phonenumber, lat, longg, UserType.student, contactedTutors, SignUpActivity.this);
                            }
                            catch (JSONException e){
                                // Log.d("JSONException", e.toString());
                            }
                    }
                    else if (userType.compareToIgnoreCase(UserType.tutor.toString()) == 0) {
                        try{
                            RESTClientRequest.postAccount(1, firebaseId, schoolID, schoolName, name, phonenumber, lat, longg, UserType.tutor, contactedTutors, SignUpActivity.this);
                        } catch (JSONException e){
                            // Log.d("JSONException", e.toString());
                        }

                    }
                }
            }
        });

    }
    public Callable<Void> handleRegister(){
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (Context.getUser().getUserType() == UserType.student) {
                    Intent degreeIntent = new Intent(SignUpActivity.this, DegreesActivity.class);
                    degreeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(degreeIntent);
                }
                else {
                    Intent degreeIntent = new Intent(SignUpActivity.this, TutorProfileActivity.class);
                    degreeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(degreeIntent);
                }
                return null;
            }
        };
    }
}
