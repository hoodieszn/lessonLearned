package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Singletons.Context;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DegreesActivity extends BaseActivity implements DegreesViewAdapter.ItemClickListener {

    private List<Degree> degreeList;
    private DegreesViewAdapter adapter;
    private RecyclerView recyclerView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_degrees);

        recyclerView = findViewById(R.id.degrees);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent login = new Intent(DegreesActivity.this, MainActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(login);
            }
        });

        Toast.makeText(DegreesActivity.this, Context.getUser() != null ? Context.getUser().toString() : "user's null boi", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Context.getUser() != null){

            // Fetch Degrees from server
            try {
                RESTClientRequest.getDegrees(populateDegreeList());
            }
            catch (JSONException e){
                Log.d("JSONException", e.toString());
            }
        }
    }

    public Callable<Void> populateDegreeList(){
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                degreeList = new ArrayList<Degree>(Context.getDegrees().values());

                adapter = new DegreesViewAdapter(DegreesActivity.this, degreeList);
                adapter.setClickListener(DegreesActivity.this);
                recyclerView.setAdapter(adapter);

                return null;
            }
        };
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent tutorsIntent = new Intent(DegreesActivity.this, TutorsActivity.class);
        tutorsIntent.putExtra("degreeId", adapter.getItem(position).getId());

        startActivity(tutorsIntent);
    }
}
