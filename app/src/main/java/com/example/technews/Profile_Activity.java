package com.example.technews;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Profile_Activity extends AppCompatActivity {

    private TextView usernameTextView, emailTextView, passwordTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);

        if (user != null) {
            updateUserInfo(user);
        } else {
            usernameTextView.setText("Username: Not logged in");
            emailTextView.setText("Email: -");
            passwordTextView.setText(". . . . . . .");
        }

        findViewById(R.id.home).setOnClickListener(v -> finish());
        findViewById(R.id.dev).setOnClickListener(v -> startActivity(new Intent(this, Activity_Dev.class)));
        findViewById(R.id.editinfo).setOnClickListener(v -> showEditInfoDialog());
        findViewById(R.id.signout).setOnClickListener(v -> signOutUser());
    }

    private void updateUserInfo(FirebaseUser user) {
        emailTextView.setText("Email: " + (user.getEmail() != null ? user.getEmail() : "-"));
        passwordTextView.setText(". . . . . . .");

        FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        usernameTextView.setText("Username: " + (username != null ? username : "-"));
                    }
                });
    }

    private void showEditInfoDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.editprofile_layout, null);

        EditText firstName = view.findViewById(R.id.editFirstName);
        EditText lastName = view.findViewById(R.id.editLastName);
        EditText username = view.findViewById(R.id.editUsername);
        EditText password = view.findViewById(R.id.editPassword);
        EditText confirmPassword = view.findViewById(R.id.editConfirmPassword);

        // Prefill fields from Firestore
        if (user != null) {
            FirebaseFirestore.getInstance().collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            firstName.setText(doc.getString("firstName"));
                            lastName.setText(doc.getString("lastName"));
                            username.setText(doc.getString("username"));
                        }
                    });
        }

        new AlertDialog.Builder(this)
                .setTitle("Edit Profile")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newFirstName = firstName.getText().toString().trim();
                    String newLastName = lastName.getText().toString().trim();
                    String newUsername = username.getText().toString().trim();

                    String newPassword = password.getText().toString().trim();
                    String confirmNewPassword = confirmPassword.getText().toString().trim();

                    if (newUsername.isEmpty() ) {
                        Toast.makeText(this, "Username and Email cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!newPassword.isEmpty() && !newPassword.equals(confirmNewPassword)) {
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (user != null) {
                        // Update Email


                        // Update Password
                        if (!newPassword.isEmpty()) {
                            user.updatePassword(newPassword)
                                    .addOnSuccessListener(unused -> Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(this, "Password update failed", Toast.LENGTH_SHORT).show());
                        }

                        // Update Firestore
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("firstName", newFirstName);
                        updates.put("lastName", newLastName);
                        updates.put("username", newUsername);

                        FirebaseFirestore.getInstance().collection("users")
                                .document(user.getUid())
                                .update(updates)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                                    usernameTextView.setText("Username: " + newUsername);
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Profile_Activity.this, Login_Activity.class));
        finish();
    }
}
