package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login_activity extends AppCompatActivity {
    Button btn_signup, btn_signin;
    ImageView img_homeicon;
    TextView tv_logoname, tv_slogan;
    TextInputLayout input_username, input_password;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btn_signup = findViewById(R.id.btn_signup);
        btn_signin = findViewById(R.id.btn_signin);
        img_homeicon = findViewById(R.id.img_homeicon);
        tv_logoname = findViewById(R.id.tv_logoname);
        tv_slogan = findViewById(R.id.tv_slogan);
        input_username = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);
        progressBar = findViewById(R.id.progressBar);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_activity.this, signup_activity.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(img_homeicon, "logo_image");
                pairs[1] = new Pair<View, String>(tv_logoname, "logo_text");
                pairs[2] = new Pair<View, String>(tv_logoname, "logo_desc");
                pairs[3] = new Pair<View, String>(input_username, "username_tran");
                pairs[4] = new Pair<View, String>(input_password, "password_tran");
                pairs[5] = new Pair<View, String>(btn_signin, "button_tran");
                pairs[6] = new Pair<View, String>(btn_signup, "login_signup_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login_activity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(v);
            }
        });

    }

    private Boolean validateEmail() {
        String val = input_username.getEditText().getText().toString();
        if (val.isEmpty()){
            input_username.setError("Field is not empty");
            return false;
        } else {
            input_username.setError(null);
            input_username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = input_password.getEditText().getText().toString();
        if (val.isEmpty()){
            input_password.setError("Field is not empty");
            return false;
        } else {
            input_password.setError(null);
            input_password.setErrorEnabled(false);
            return true;
        }
    }

    public void signIn(View view) {
        progressBar.setVisibility(view.VISIBLE);
        if(!validateEmail() | !validatePassword()) {
            progressBar.setVisibility(view.GONE);
            return;
        } else {
            isUser(view);
        }

    }

    private void isUser(View view) {
        String userEnteredUsername = input_username.getEditText().getText().toString();
        String userEnteredPassword = input_password.getEditText().getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(view.GONE);
                if (snapshot.exists()) {
                    input_username.setError(null);
                    input_username.setErrorEnabled(false);
                    String passwordDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordDB.equals(userEnteredPassword)) {
                        input_password.setError(null);
                        input_password.setErrorEnabled(false);
                        String fullnameDB = snapshot.child(userEnteredUsername).child("fullname").getValue(String.class);
                        String emailDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String phoneNumberDB = snapshot.child(userEnteredUsername).child("phonenumber").getValue(String.class);
                        String usernameDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        Intent intent = new Intent(login_activity.this, Dashboard.class);
                        startActivity(intent);
                    } else {
                        input_password.setError("Wrong password");
                        input_password.requestFocus();
                    }
                } else {
                    Toast.makeText(login_activity.this, "No such user exists", Toast.LENGTH_SHORT).show();
                    input_username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
