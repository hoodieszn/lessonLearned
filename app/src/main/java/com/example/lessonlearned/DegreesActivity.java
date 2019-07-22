package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Singletons.Context;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DegreesActivity extends BaseActivity implements DegreesViewAdapter.ItemClickListener {

    private List<Degree> degreeList;
    private DegreesViewAdapter adapter;
    private RecyclerView recyclerView;

    // Spinner
    private ProgressBar spinner;

    // Map of degrees with <degreeId, Degree>
    private Map<Integer, Degree> degreesMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_degrees);

        recyclerView = findViewById(R.id.degrees);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        spinner = findViewById(R.id.degreesProgress);
    }

    @Override
    protected void onStart() {
        super.onStart();

        startLoadingState();

        if (Context.getUser() != null){
            // Fetch Degrees from server
            try {
                RESTClientRequest.getDegrees(this);
            }
            catch (JSONException e){ }
        }
        // If no user, send back to login screen
        else {
            Intent homeIntent = new Intent(DegreesActivity.this, MainActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
        }
    }

    public void populateDegreeList(Map<Integer, Degree> degreesMap){
        stopLoadingState();

        this. degreesMap = degreesMap;
        degreeList = new ArrayList<Degree>(degreesMap.values());

        adapter = new DegreesViewAdapter(DegreesActivity.this, degreeList);
        adapter.setClickListener(DegreesActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent tutorsIntent = new Intent(DegreesActivity.this, TutorsListActivity.class);
        tutorsIntent.putExtra("degreeId", adapter.getItem(position).getId());
        tutorsIntent.putExtra("degreeName", adapter.getItem(position).getName());
        startActivity(tutorsIntent);
    }

    private void startLoadingState(){
        spinner.setVisibility(View.VISIBLE);
    }

    private void stopLoadingState(){
        spinner.setVisibility(View.INVISIBLE);
    }
}
