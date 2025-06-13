package com.example.technews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout;

public class Activity_Dev extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_layout);

        findViewById(R.id.home).setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Dev.this, Home_Activity.class); // Replace with your actual Home activity
            startActivity(intent);
            finish(); // Optional: close current activity
        });

        findViewById(R.id.profile).setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Dev.this, Profile_Activity.class);
            startActivity(intent);
            finish(); // Optional: close current activity
        });

    }
}
