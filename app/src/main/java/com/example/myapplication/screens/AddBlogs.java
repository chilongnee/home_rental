package com.example.myapplication.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

public class AddBlogs extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText titleEditText, descriptionEditText, priceEditText, locationEditText;
    private ImageView roomImageView;
    private Button chooseImageButton, saveButton;
    private DatabaseReference databaseReference;
    private Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blogs);

        databaseReference = FirebaseDatabase.getInstance().getReference("rooms");

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        locationEditText = findViewById(R.id.locationEditText);
        roomImageView = findViewById(R.id.roomImageView);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        saveButton = findViewById(R.id.saveButton);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi phương thức để chọn hình ảnh từ máy
                // (Bạn cần triển khai phương thức này)
                chooseImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi phương thức để lưu thông tin phòng (Bạn cần triển khai phương thức này)
                saveRoom();
            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            loadImage(selectedImageUri);
        }
    }

    private void loadImage(Uri imageUri) {
        Picasso.get().load(imageUri).into(roomImageView);
    }


    private void saveRoom() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (title.isEmpty() || description.isEmpty() || priceStr.isEmpty() || location.isEmpty()) {
            Toast.makeText(AddBlogs.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        if (currentUser != null) {
            // Lấy UID của người dùng hiện tại
            String userUid = currentUser.getUid();

            // Tạo đối tượng Room từ dữ liệu người dùng và thêm thông tin đăng nhập
            Room room = new Room(title, description, price, location, selectedImageUri.toString(), userUid);

            // Đẩy đối tượng Room lên Firebase Realtime Database
            String roomId = databaseReference.push().getKey();
            databaseReference.child(roomId).setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddBlogs.this, "Room saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddBlogs.this, "Failed to save room", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}