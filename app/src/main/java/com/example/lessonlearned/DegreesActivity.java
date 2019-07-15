package com.example.lessonlearned;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.lessonlearned.Models.Degree;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class DegreesActivity extends BaseActivity implements DegreesViewAdapter.ItemClickListener {

    private List<Degree> degreeList;  //= Arrays.asList("Math","Science","Engineering", "Arts", "Business", "Environment", "Other");
    private DegreesViewAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_degrees);

        // Temp: is gonna be rest client call in future
        degreeList = Arrays.asList(new Degree(2, "Mathematics", 1),
                new Degree(3, "Science", 1),
                new Degree(4, "Engineering", 1));

        RecyclerView recyclerView = findViewById(R.id.degrees);
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

        adapter = new DegreesViewAdapter(this, degreeList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent tutorsIntent = new Intent(DegreesActivity.this, TutorsActivity.class);

        //ToDo: fix these, right now they're just extra intent vals

        tutorsIntent.putExtra("InstitutionName", "University of Waterloo");
        tutorsIntent.putExtra("InstitutionId", "0");

        tutorsIntent.putExtra("CategoryName", adapter.getItem(position).getName());
        tutorsIntent.putExtra("CategoryId", adapter.getItem(position).getId());
        startActivity(tutorsIntent);
    }
}
