package com.example.myapplication;

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
import com.example.myapplication.screens.login_activity;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    CardView btn_logout;
    TextView tv_fullname;
    private String fullName;
    String avatarUrl;
    CircleImageView profile_image;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setAvatarUrl(String avatarUrl){
        this.avatarUrl = avatarUrl;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        tv_fullname = view.findViewById(R.id.tv_fullname);
        profile_image = view.findViewById(R.id.profile_image);
        btn_logout = view.findViewById(R.id.btn_logout);

        if (this.fullName != null) {
            tv_fullname.setText(this.fullName);
        }
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(this)
                    .load(avatarUrl)
                    .into(profile_image);
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

        return view;
    }

}
