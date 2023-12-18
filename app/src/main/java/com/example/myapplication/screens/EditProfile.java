package com.example.myapplication.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;

public class EditProfile extends AppCompatActivity {
    Toolbar toolbar;
    String fullname;
    String avatarUrl;
    String address;
    String phonenumber;
    String gender;
    String date;
    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = findViewById(R.id.toolbar);
        setToolBar(toolbar);
        fullname = getIntent().getStringExtra("fullname");
        avatarUrl = getIntent().getStringExtra("avatarUrl");
        address = getIntent().getStringExtra("address");
        phonenumber = getIntent().getStringExtra("phonenumber");
        gender = getIntent().getStringExtra("gender");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
    }
    private void setToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        if (toolbar != null && toolbarTitle != null) {
            // Customize the toolbar title for this activity
            toolbarTitle.setText("Chỉnh sửa trang cá nhân");
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);

        // Set a click listener for the navigation icon
        toolbar.setNavigationOnClickListener(v -> {
            // Handle the navigation icon click (e.g., go back)
            Intent intent = new Intent(EditProfile.this, MyProfile.class);
            intent.putExtra("fullname", fullname);
            intent.putExtra("avatarUrl", avatarUrl);
            intent.putExtra("address", address);
            intent.putExtra("phonenumber", phonenumber);
            intent.putExtra("gender", gender);
            intent.putExtra("date", date);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
        });
    }
}