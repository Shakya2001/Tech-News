package com.example.technews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Activity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2500; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        // Wait for SPLASH_DURATION then start MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash_Activity.this, Main_Activity.class);
            startActivity(intent);
            finish(); // close SplashActivity so user can't go back to it
        }, SPLASH_DURATION);
    }
}