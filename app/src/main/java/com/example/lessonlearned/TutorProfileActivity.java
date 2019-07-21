package com.example.lessonlearned;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.example.lessonlearned.Singletons.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TutorProfileActivity extends AppCompatActivity {

    private Tutor tutor;
    private List<TutorPosting> activePostings = new ArrayList<>();
    private static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);
        tutor = (Tutor)Context.getUser();
        activePostings = tutor.getPostings();
        populateTutorProfile();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request it if we dont have it
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_MULTIPLE_REQUEST);
        }
        else {
            handleLocation();
        }
    }

    public void populateTutorProfile() {
        TextView currentTutorName = findViewById(R.id.currentTutorName);
        currentTutorName.setText(tutor.getName());

        TextView school = findViewById(R.id.school);
        school.setText(tutor.getSchoolName());

        TextView phone = findViewById(R.id.phone);
        phone.setText(tutor.getPhone());

        initActivePostings();
        initAddPostingButton();

    }

    private void initAddPostingButton() {
        ImageButton addButton = findViewById(R.id.addButton);
        final Intent addPosting = new Intent(getApplicationContext(), CreateTutorPosting.class);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addPosting);
            }
        });
    }
    private void initActivePostings() {
        RecyclerView recyclerView = findViewById(R.id.tutorPostings);
        ActivePostsViewAdapter adapter = new ActivePostsViewAdapter(activePostings, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

                        Context.getUser().setLatitude(location.getLatitude());
                        Context.getUser().setLongitude(location.getLongitude());
                        // Toast.makeText(TutorsListActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TutorProfileActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }

        }
    }
}

//=======
//        Button but = findViewById(R.id.button2);
//        but.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent degreeIntent = new Intent(TutorProfileActivity.this, CreateTutorPosting.class);
//                degreeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(degreeIntent);
//            }
//        });
//    }
//
//>>>>>>> 0b2e6edd19769b4d2fa9a90b0011f527e77958b5