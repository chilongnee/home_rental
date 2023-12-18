package com.example.myapplication.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

import org.w3c.dom.Text;

public class MyProfile extends AppCompatActivity {
    Toolbar toolbar;
    TextView tv_fullname;
    TextView tv_email;
    TextView tv_phonenumber;
    TextView tv_address;
    String fullname;
    String email;
    String avatarUrl;
    String address;
    String phonenumber;
    String gender;
    String date;
    String password;
    TextView btn_editProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        toolbar = findViewById(R.id.toolbar);
        tv_fullname = findViewById(R.id.tv_fullname);
        tv_email = findViewById(R.id.tv_email);
        tv_phonenumber = findViewById(R.id.tv_phonenumber);
        tv_address = findViewById(R.id.tv_address);
        btn_editProfile = findViewById(R.id.btn_editProfile);

        fullname = getIntent().getStringExtra("fullname");
        email = getIntent().getStringExtra("email");
        phonenumber = getIntent().getStringExtra("phonenumber");
        avatarUrl = getIntent().getStringExtra("avatarUrl");
        address = getIntent().getStringExtra("address");
        gender = getIntent().getStringExtra("gender");
        date = getIntent().getStringExtra("date");
        password = getIntent().getStringExtra("password");

        setToolBar(toolbar, fullname);

        tv_fullname.setText(fullname);
        tv_email.setText(email);
        tv_phonenumber.setText(phonenumber);
        tv_address.setText(address);

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfie(fullname, email, phonenumber, avatarUrl, address, gender, date, password);

            }
        });
    }

    private void editProfie(String fullname, String email, String phonenumber, String avatarUrl, String address, String gender, String date, String password) {
        Intent intent = new Intent(MyProfile.this, EditProfile.class);
        intent.putExtra("fullname", fullname);
        intent.putExtra("avatarUrl", avatarUrl);
        intent.putExtra("address", address);
        intent.putExtra("phonenumber", phonenumber);
        intent.putExtra("gender", gender);
        intent.putExtra("date", date);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    private void setToolBar(Toolbar toolbar, String fullname) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setTitle("");
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        if (toolbar != null && toolbarTitle != null) {
            // Customize the toolbar title for this activity
            toolbarTitle.setText(fullname);
            setSupportActionBar(toolbar);
        }
        // Set a custom navigation icon (replace with your own icon)
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);

        // Set a click listener for the navigation icon
        toolbar.setNavigationOnClickListener(v -> {
            // Handle the navigation icon click (e.g., go back)
            Intent intent = new Intent(MyProfile.this, Dashboard.class);
            intent.putExtra("fullname", fullname);
            intent.putExtra("avatarUrl", avatarUrl);
            intent.putExtra("address", address);
            intent.putExtra("phonenumber", phonenumber);
            intent.putExtra("gender", gender);
            intent.putExtra("date", date);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("goToHomeFragment", true);

            startActivity(intent);
        });
    }
}