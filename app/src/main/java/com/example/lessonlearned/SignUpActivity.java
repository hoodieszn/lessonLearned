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
import com.example.lessonlearned.Services.RESTClient;
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
            Log.d("JSONException", e.toString());
        }






    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleLocation();
    }
    private void handleLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            try {
                LocationManager lm = (LocationManager)getSystemService(android.content.Context.LOCATION_SERVICE);

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        lat = location.getLatitude();
                        longg = location.getLongitude();


                        //Toast.makeText(TutorsListActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
            catch (SecurityException e){
                Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void populateSignUp(){
        final Spinner spinner2 = findViewById(R.id.spinner2);
        final Spinner spinner = findViewById(R.id.spinner);
        List<String> listofSchools = new ArrayList<String>();
        List<String> typesofAccounts = new ArrayList<String>();
        typesofAccounts.add("Type of User");
        typesofAccounts.add("Student");
        typesofAccounts.add("Tutor");

        String hint = "Choose your university";
        listofSchools.add(hint);
        for (int i = 0; i < schools.size(); i++){
            listofSchools.add(schools.get(i).getName());
        }
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.sort_spinner, listofSchools){
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
        spinnerArrayAdapter.setDropDownViewResource(R.layout.sort_spinner);
        spinner2.setAdapter(spinnerArrayAdapter);
        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.sort_spinner, typesofAccounts){
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
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.sort_spinner);
        spinner.setAdapter(spinnerArrayAdapter2);
        Button register = findViewById(R.id.button3);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String schoolName;
                String userType;
                EditText username = findViewById(R.id.editText3);
                String name = username.getText().toString();
                String phonenumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                phonenumber = phonenumber.substring(2);
                if(spinner2 != null && spinner2.getSelectedItem() !=null && spinner != null && spinner.getSelectedItem() != null ) {
                    schoolName = (String)spinner2.getSelectedItem();
                    userType = (String)spinner.getSelectedItem();
                    List <ContactedTutor> contactedTutors = new ArrayList<ContactedTutor>();
                    String firebaseId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    int schoolID = 1;
                    for (int i = 0; i < schools.size(); i++){
                        if (schoolName == schools.get(i).getName()){
                            schoolID = schools.get(i).getId();
                        }
                    }
                    if (userType == "Student"){
                            try{
                                RESTClientRequest.postAccount(1, firebaseId, schoolID, schoolName, name, phonenumber, lat, longg, UserType.student, contactedTutors, SignUpActivity.this);
                            } catch (JSONException e){
                            Log.d("JSONException", e.toString());
                        }
                    } else {
                        try{
                            RESTClientRequest.postAccount(1, firebaseId, schoolID, schoolName, name, phonenumber, lat, longg, UserType.tutor, contactedTutors, SignUpActivity.this);
                            //Intent degreeIntent = new Intent(SignUpActivity.this, TutorsListActivity.class);
                            //SignUpActivity.this.startActivity(degreeIntent);
                        } catch (JSONException e){
                            Log.d("JSONException", e.toString());
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
                    Log.d("BREAKPOINT", "GETS TO HERE");
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
