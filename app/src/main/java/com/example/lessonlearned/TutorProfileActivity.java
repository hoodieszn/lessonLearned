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
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lessonlearned.Dialogs.CreatePostingDialog;
import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.example.lessonlearned.Singletons.Context;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TutorProfileActivity extends AppCompatActivity implements CreatePostingDialog.CreatePostingDialogListener{

    private Tutor tutor;
    private List<TutorPosting> activePostings = new ArrayList<>();

    private static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    private CreatePostingDialog dialog;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutor_profile);

        tutor = (Tutor)Context.getUser();
        activePostings = tutor.getPostings();
        dialog = new CreatePostingDialog();

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

    // Get Tutor Information
    public void populateTutorProfile() {
        TextView currentTutorName = findViewById(R.id.currentTutorName);
        currentTutorName.setText(tutor.getName());

        TextView school = findViewById(R.id.school);
        school.setText(tutor.getSchoolName());

        TextView phone = findViewById(R.id.phone);
        phone.setText(tutor.getPhone());

        logout = findViewById(R.id.tutorLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Context.setUser(null);

                Intent login = new Intent(TutorProfileActivity.this, MainActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(login);
            }
        });

        populatePostings();

        final ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePostingDialog();
            }
        });
    }

    public void populatePostings(){
        RecyclerView recyclerView = findViewById(R.id.tutorPostings);
        ActivePostsViewAdapter adapter = new ActivePostsViewAdapter(activePostings, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void showCreatePostingDialog() {
        dialog = new CreatePostingDialog();
        dialog.show(getSupportFragmentManager(), "CreatePostingDialog");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        tutor = (Tutor)Context.getUser();
        activePostings = tutor.getPostings();

        // New posting added
        populatePostings();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
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