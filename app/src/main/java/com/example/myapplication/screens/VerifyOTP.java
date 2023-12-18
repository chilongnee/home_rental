package com.example.myapplication.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.myapplication.R;

import com.example.myapplication.model.User;
import com.example.myapplication.ultis.AndroidUtil;
import com.google.android.gms.tasks.*;


import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    ProgressBar progressBar;
    Button btn_verify;
    PinView pinFromUser;
    String codeBySystem;
    String fullname;
    String email;
    String username;
    String password;
    String address;
    String gender;
    String date;
    String phonenumber;

    Long timeoutSeconds = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        btn_verify = findViewById(R.id.btn_verify);
        pinFromUser = findViewById(R.id.pinFromUser);
        fullname = getIntent().getStringExtra("fullname");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        address = getIntent().getStringExtra("address");
        gender = getIntent().getStringExtra("gender");
        date = getIntent().getStringExtra("date");
        phonenumber = getIntent().getStringExtra("phonenumber");

        Toast.makeText(VerifyOTP.this, " " + phonenumber, Toast.LENGTH_SHORT).show();
        Log.d("phone", phonenumber);
        progressBar = findViewById(R.id.progressBar);
        sendVerificationCodeToUser(phonenumber, false);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                clickbtnVerify(v);
            }
        });
    }


    private void sendVerificationCodeToUser(String phonenumber, boolean isResend) {
        /*
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,
                60L,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
         */

        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phonenumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOTP.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(), "OTP set successfully");
                            }
                        });
        PhoneAuthOptions options = builder.build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                        Log.d("OTP", "Verification completed with code: " + code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyOTP.this, "OTP verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("VerificationFailed", e.getMessage());
                }

            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
        signInWithPhoneAuthCredential(credential);

    }
    private void clickbtnVerify(View v) {
        String code = pinFromUser.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullname, username, email, password, address, gender, date, phonenumber, null);
                            registerNewUser(user);
                            Toast.makeText(VerifyOTP.this, "Verification completed!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyOTP.this, "Verification Not Completed ! Try Again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



    private void registerNewUser(User user) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //  progressBar.setVisibility(View.GONE); // Always hide the ProgressBar

                        if (task.isSuccessful()) {
                            // User has been successfully created
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                // Add user to Realtime Database with UID as the key
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                                databaseReference.child(userId).setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Data has been successfully written to the database
                                                    Toast.makeText(VerifyOTP.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                    FirebaseAuth.getInstance().signOut();

                                                    // Redirect to the login screen or another appropriate activity
                                                    Intent intent = new Intent(VerifyOTP.this, login_activity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // Handle the error when adding to Realtime Database
                                                    Toast.makeText(VerifyOTP.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Handle the case where FirebaseUser is null
                                Toast.makeText(VerifyOTP.this, "FirebaseUser is null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle the error when creating a user
                            Toast.makeText(VerifyOTP.this, "Registration failed2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}