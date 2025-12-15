package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail;
    private MaterialButton btnSaveChanges, btnCancel, btnChangePhoto;
    private TextView tvUserName;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "user_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Find toolbar and set up back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // Initialize views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        tvUserName = findViewById(R.id.tvUserName);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnCancel = findViewById(R.id.btnCancel);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);

        // Load current user data
        loadUserData();

        // Save button click
        btnSaveChanges.setOnClickListener(v -> saveProfile());

        // Cancel button click
        btnCancel.setOnClickListener(v -> finish());

        // Change photo button click
        btnChangePhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Photo upload coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String email = user.getEmail();
            etEmail.setText(email);

            // Load saved name or extract from email
            String savedName = prefs.getString("full_name", "");
            if (savedName.isEmpty() && email != null) {
                savedName = email.split("@")[0];
            }

            etFullName.setText(savedName);
            tvUserName.setText(savedName.toUpperCase());
        }
    }

    private void saveProfile() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Name is required");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        // Save name to SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("full_name", fullName);
        editor.apply();

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
