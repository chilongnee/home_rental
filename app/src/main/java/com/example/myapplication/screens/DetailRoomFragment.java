package com.example.myapplication.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

public class DetailRoomFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room_fragment);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("roomId")) {
            String roomId = intent.getStringExtra("roomId");
            String title = intent.getStringExtra("title");
            double price = intent.getDoubleExtra("price", 0.0);
            String description = intent.getStringExtra("description");
            String location = intent.getStringExtra("location");
            String imageUrl = intent.getStringExtra("imageUrl");

            ImageView roomImageView = findViewById(R.id.roomImageView);
            TextView titleTextView = findViewById(R.id.titleTextView);
            TextView priceTextView = findViewById(R.id.priceTextView);
            TextView descriptionTextView = findViewById(R.id.descriptionTextView);
            TextView locationTextView = findViewById(R.id.locationTextView);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(Uri.parse(imageUrl)).into(roomImageView);
            }

            titleTextView.setText(title);
            priceTextView.setText(String.valueOf(price));
            descriptionTextView.setText(description);
            locationTextView.setText(location);
        }
    }
}