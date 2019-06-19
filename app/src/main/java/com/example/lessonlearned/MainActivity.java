package com.example.lessonlearned;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolBar = findViewById(R.id.app_bar);
        toolBar.setTitle("Lesson Learned");
        setSupportActionBar(toolBar);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem accounts = menu.findItem(R.id.accounts);
        boolean studentAccount = true; // ToDo: Actually figure out what type of account
        accounts.setVisible(!studentAccount);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.accounts) {
            // Handle Account click
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
