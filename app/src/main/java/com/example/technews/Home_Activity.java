package com.example.technews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Home_Activity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout newsContainer,profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        newsContainer = findViewById(R.id.newsContainer);
        profile = findViewById(R.id.profile);

        fetchNews();
        clickProfile();

    }




    private void fetchNews() {
        db.collection("news")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String title = document.getString("title");
                        String date = document.getString("date");
                        String content = document.getString("content");
                        String imageUrl = document.getString("imageUrl");

                        addNewsCard(title, date, content, imageUrl);
                    }
                });
    }

    private void addNewsCard(String title, String date, String content, String imageUrl) {
        LayoutInflater inflater = LayoutInflater.from(this);


        View cardView = inflater.inflate(R.layout.card_layout, newsContainer, false);

        TextView titleView = cardView.findViewById(R.id.newsTitle);
        TextView contentView = cardView.findViewById(R.id.content);
        ImageView imageView = cardView.findViewById(R.id.image);

        titleView.setText(title);
        contentView.setText(content);


        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(imageView);
        } else {

            imageView.setImageDrawable(null);
            imageView.setVisibility(View.GONE);
        }

        newsContainer.addView(cardView);
    }

    private void clickProfile() {
        profile.setOnClickListener(v -> startActivity(new Intent(this, Profile_Activity.class)));
    }

}

