package com.example.lessonlearned;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class TutorProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7), (int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Float tutorRating = 3.0f; // 3.0 is from your database

        // To show rating on RatingBar
        ratingBar.setRating(tutorRating);

        Button contact_button = findViewById(R.id.contactButton);

        Intent tutorInfo = getIntent();

        //populate tutor profile
        String name = tutorInfo.getStringExtra("name");
        final String phone = tutorInfo.getStringExtra("phone");
        String institution = tutorInfo.getStringExtra("institution");
        double price = tutorInfo.getDoubleExtra("price", 0);

        TextView tutorName = findViewById(R.id.Name);
        tutorName.setText(name);

        TextView tutorRole = findViewById(R.id.Role);
        tutorRole.setText(institution);

        TextView tutorPrice = findViewById(R.id.tutorPrice);
        tutorPrice.setText(String.format("$%s / hour", price));

        contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phone));
                intent.putExtra("sms_body", "Hello, I would like to arrange a time for tutoring");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(Activity.RESULT_OK);
        finish();
    }
}
