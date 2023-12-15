package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class signup_activity extends AppCompatActivity {
    ImageView img_homeicon;
    TextView tv_logoname;
    TextView tv_slogane;
    Button btn_signup, btn_login;
    TextInputLayout input_fullname, input_username, input_password, input_phonenumber, input_email;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
// ...
// Initialize Firebase Auth


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        img_homeicon = findViewById(R.id.img_homeicon);
        tv_logoname = findViewById(R.id.tv_logoname);
        tv_slogane = findViewById(R.id.tv_slogan);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);
        input_fullname = findViewById(R.id.input_fullname);
        input_username = findViewById(R.id.input_username);
        input_email = findViewById(R.id.input_email);
        input_phonenumber = findViewById(R.id.input_phonenumber);
        input_password = findViewById(R.id.input_password);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_activity.this, login_activity.class);

                Pair[] pairs = new Pair[3];
                pairs[0] =  new Pair<View, String>(img_homeicon, "logo_image");
                pairs[1] =  new Pair<View, String>(tv_logoname, "logo_text");
                pairs[2] =  new Pair<View, String>(tv_slogane, "logo_desc");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(signup_activity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }
    private Boolean validateName() {
        String val = input_fullname.getEditText().getText().toString();
        if (val.isEmpty()){
            input_fullname.setError("Field is not empty");
            return false;
        } else {
            input_fullname.setError(null);
            input_fullname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateUsername() {
        String noSpaceWhite = "\\A\\w{4,20}\\z";
        String val = input_username.getEditText().getText().toString();
        if (val.isEmpty()){
            input_username.setError("Field is not empty!");
            return false;
        } else if (val.length() >= 15) {
            input_username.setError("Username is too long");
            return false;
        } else if (!val.matches(noSpaceWhite)) {
            input_username.setError("White Spaces are not allowed");
            return false;
        } else {
            input_username.setError(null);
            input_username.setErrorEnabled(false);
            return true;
        }
    }
    private  Boolean validateEmail() {
        String val = input_email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()){
            input_email.setError("Field is not empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            input_email.setError("Invalid email address");
            return false;
        } else {
            input_fullname.setError(null);
            input_username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePhone() {
        String val = input_phonenumber.getEditText().getText().toString();
        if (val.isEmpty()){
            input_phonenumber.setError("Field is not empty");
            return false;
        } else {
            input_phonenumber.setError(null);
            input_phonenumber.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = input_password.getEditText().getText().toString();String passwordVal = "^(?=.*[0-9])" +           // Require at least one digit
                "(?=.*[a-z])" +              // Require at least one lowercase letter
                "(?=.*[A-Z])" +              // Require at least one uppercase letter
                "(?=.*[!@$%^&(){}\\[\\]:;<>,.?/~_+-=|])" + // Require at least one special character
                "(?=\\S+$)" +                // No white space
                ".{8,32}$";                  // Password length between 8 and 32 characters


        if (val.isEmpty()){
            input_password.setError("Field is not empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            input_password.setError("Password is too weaK");
            return false;
        } else {
            input_password.setError(null);
            input_password.setErrorEnabled(false);
            return true;
        }
    }

    public void registerUser(View view) {
        progressBar.setVisibility(View.VISIBLE);

        if (!validateName() | !validateUsername() | !validateEmail() | !validatePhone() | !validatePassword()) {
            progressBar.setVisibility(View.GONE);
            return;
        }

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        String fullname = input_fullname.getEditText().getText().toString();
        String username = input_username.getEditText().getText().toString();
        String phonenumber = input_phonenumber.getEditText().getText().toString();
        String email = input_email.getEditText().getText().toString();
        String password = input_password.getEditText().getText().toString();
        UserHelperClass helperClass = new UserHelperClass(fullname, username, email, phonenumber, password);

        // Check if email, username, and phone number already exist
        checkExistingUser(email, username, phonenumber, helperClass);


    }

    private void checkExistingUser(String email, String username, String phonenumber, UserHelperClass user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(username);

        Query emailRef = reference.orderByChild("email").equalTo(email);
        Query usernameRef = reference.orderByChild("username").equalTo(username);
        Query phoneNumberRef = reference.orderByChild("phonenumber").equalTo(phonenumber);

        emailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot emailSnapshot) {
                if (emailSnapshot.exists()) {
                    // Email already exists
                    Toast.makeText(signup_activity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    // Check if username already exists
                    usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot usernameSnapshot) {
                            if (usernameSnapshot.exists()) {
                                // Username already exists
                                Toast.makeText(signup_activity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                // Check if phone number already exists
                                phoneNumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot phoneSnapshot) {
                                        if (phoneSnapshot.exists()) {
                                            // Phone number already exists
                                            Toast.makeText(signup_activity.this, "Phone number already exists", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            // No existing user, proceed with registration
                                            registerNewUser(user);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(signup_activity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }


                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(signup_activity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(signup_activity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void registerNewUser(UserHelperClass user) {
        // Register the new user
        reference.child(user.getUsername()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data has been successfully written to the database
                            Toast.makeText(signup_activity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), login_activity.class);
                            startActivity(intent);
                        } else {
                            // Handle the error
                            Toast.makeText(signup_activity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE); // Hide the ProgressBar
                    }
                });
    }

}