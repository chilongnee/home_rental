package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Dashboard extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton btn_addblogs;
    private Fragment selectedFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bottomNavigationView = findViewById(R.id.nav_view);
        btn_addblogs = findViewById(R.id.btn_addblogs);
        btn_addblogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, AddBlogs.class);
                startActivity(intent);
                overridePendingTransition(R.anim.goup, R.anim.godown);
            }
        });
        // Set the default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new HomeFragment())
                .commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {


                // Handle item clicks using if-else
                if (item.getItemId() == R.id.btn_home) {
                    selectedFragment = new HomeFragment();

                } else if (item.getItemId() == R.id.btn_chat) {
                    selectedFragment = new ChatFragment();
                } else if (item.getItemId() == R.id.btn_manablogs) {
                    selectedFragment = new BlogsFragment();
                } else if (item.getItemId() == R.id.btn_account) {
                    selectedFragment = new AccountFragment();
                } else {
                    // Handle other items if needed
                    return false;
                }

                // Replace the current fragment with the selected one
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, selectedFragment)
                        .commit();

                return true;
            }
        });

    }

}