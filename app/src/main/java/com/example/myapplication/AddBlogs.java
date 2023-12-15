package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

public class AddBlogs extends AppCompatActivity {
    private static final String TAG = "AddBlogs";

    private TextInputEditText edt_address;
    private AutoCompleteTextView autoCompletetv_ttnoithat;
    ArrayAdapter<String> adapterItems;
    String[] items_ttnoithat = {"Nội thất cao cấp", "Nội thất đầy đủ", "Nhà trống"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blogs);
        edt_address = findViewById(R.id.edt_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        autoCompletetv_ttnoithat = findViewById(R.id.autoCompletetv_ttnoithat);


        setToolBar(toolbar);

        String apiKey = "AIzaSyDN8RpJlymZg4NpATx9rwuSb6x5ktM0JB4";
        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), apiKey);
        }
        edt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              edtAddressClick(v);
            }
        });

        tttnoithat(autoCompletetv_ttnoithat);

    }

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                        // Do something with the selected place
                        edt_address.setText(place.getName());
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
            });
    public void edtAddressClick(View v){
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(AddBlogs.this);
        startAutocomplete.launch(intent);
    }

    public void setToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setTitle("");
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbar != null && toolbarTitle != null) {
            // Customize the toolbar title for this activity
            toolbarTitle.setText("Đăng tin");
            setSupportActionBar(toolbar);
        }
        // Set a custom navigation icon (replace with your own icon)
        toolbar.setNavigationIcon(R.drawable.baseline_clear_24);

        // Set a click listener for the navigation icon
        toolbar.setNavigationOnClickListener(v -> {
            // Handle the navigation icon click (e.g., go back)
            Intent intent = new Intent(AddBlogs.this, Dashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.godown, R.anim.goup);
            finish();
        });
    }

    public void tttnoithat(AutoCompleteTextView autoCompletetv_ttnoithat) {
        adapterItems = new ArrayAdapter<String>(AddBlogs.this, R.layout.list_item_ttnoithat, items_ttnoithat);
        autoCompletetv_ttnoithat.setAdapter(adapterItems);
        autoCompletetv_ttnoithat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(AddBlogs.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
