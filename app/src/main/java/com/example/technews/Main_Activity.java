package com.example.technews;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        initializeFirebase();
        checkUserAuthentication();
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void checkUserAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Class<?> destinationActivity = currentUser == null
                ? Login_Activity.class
                : Home_Activity.class;

        navigateToActivity(destinationActivity);
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish(); // Prevent returning to MainActivity with back button
    }
}