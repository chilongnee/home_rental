package com.example.myapplication.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.screens.EditProfile;
import com.example.myapplication.screens.MyProfile;
import com.example.myapplication.screens.login_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    CardView btn_logout;
    TextView tv_fullname;
    private String fullName;
    String avatarUrl;
    String email;
    String address;
    String phonenumber;
    String date;
    String gender;
    String password;
    String userId;
    CircleImageView profile_image;
    CardView btn_editProfile;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setAvatarUrl(String avatarUrl){
        this.avatarUrl = avatarUrl;
    }
    public void setEmail(String email){this.email = email;}
    public void setAddress(String address) {this.address = address;}

    public void setPhoneNumber(String phonenumber) {this.phonenumber = phonenumber;}

    public void setGender(String gender) {this.gender = gender;}

    public void setDate(String date) {this.date = date;}

    public void setPassword(String password) {this.password = password;};
    public void setuserId(String userId) {this.userId = userId;}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        tv_fullname = view.findViewById(R.id.tv_fullname);
        profile_image = view.findViewById(R.id.profile_image);
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_editProfile = view.findViewById(R.id.btn_editProfile);
        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProfile.class);
                intent.putExtra("fullname", fullName);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("phonenumber", phonenumber);
                intent.putExtra("gender", gender);
                intent.putExtra("date", date);
                intent.putExtra("address", address);
                intent.putExtra("avatarUrl", avatarUrl);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        if (this.fullName != null) {
            tv_fullname.setText(this.fullName);
        }
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(this)
                    .load(avatarUrl)
                    .into(profile_image);
        } else {
            Picasso.get().load(avatarUrl).into(profile_image);
        }
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }

            private void logoutUser() {
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login screen or another appropriate activity
                Intent intent = new Intent(getActivity(), login_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        TextView textView9 = view.findViewById(R.id.textView9);
        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoriteNews.class);
                startActivity(intent);
            }
        });

        return view;
    }


}
