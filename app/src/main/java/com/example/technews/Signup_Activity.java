package com.example.technews;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup_Activity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private TextInputEditText emailEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    private TextInputEditText firstNameEditText, lastNameEditText;
    private TextView signupButton;
    private TextView signInRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        firstNameEditText = findViewById(R.id.firstname);
        lastNameEditText = findViewById(R.id.lastname);
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.conpass);
        signupButton = findViewById(R.id.create);
        signInRedirect = findViewById(R.id.signin);

        signupButton.setOnClickListener(v -> registerUser());

        signInRedirect.setOnClickListener(v -> {
            startActivity(new Intent(Signup_Activity.this, Login_Activity.class));
            finish();
        });
    }

    private void registerUser() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Input validation
        if (firstName.isEmpty()) {
            firstNameEditText.setError("Enter first name");
            firstNameEditText.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            lastNameEditText.setError("Enter last name");
            lastNameEditText.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            usernameEditText.setError("Enter username");
            usernameEditText.requestFocus();
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            emailEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password should be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // Create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String fullName = firstName + " " + lastName;

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build();

                        if (user != null) {
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            // Save user to Firestore
                                            Map<String, Object> userData = new HashMap<>();
                                            userData.put("uid", user.getUid());
                                            userData.put("firstName", firstName);
                                            userData.put("lastName", lastName);
                                            userData.put("username", username);
                                            userData.put("email", email);

                                            db.collection("users").document(user.getUid())
                                                    .set(userData)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(Signup_Activity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(Signup_Activity.this, Login_Activity.class));
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(Signup_Activity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                                    });
                                        } else {
                                            Toast.makeText(Signup_Activity.this, "Failed to set display name", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(Signup_Activity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

