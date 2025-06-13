package com.example.technews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText emailField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Reference to input fields
        emailField = findViewById(R.id.email1);    // Username/email field
        passwordField = findViewById(R.id.password1);   // Password field

        // Login button click
        View loginButton = findViewById(R.id.create);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        //Singup button
        View signupButton = findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, Signup_Activity.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser() {
        String username = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login_Activity.this, "Please enter both Username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Login_Activity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login_Activity.this, Home_Activity.class));
                        finish();
                    } else {
                        Toast.makeText(Login_Activity.this, "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
