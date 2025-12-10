package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast; // We need this for the logout message
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity { // <--- EXTEND THIS CLASS

    private FirebaseAuth mAuth;
    private TextView tvWelcome;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 1. Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 2. Link UI elements
        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);

        // 3. Display a welcome message using the current user's email
        if (mAuth.getCurrentUser() != null) {
            String email = mAuth.getCurrentUser().getEmail();
            tvWelcome.setText("Welcome to FoodBridge, " + email + "!");
        } else {
            tvWelcome.setText("Welcome to FoodBridge!");
        }

        // 4. Implement Logout logic
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut(); // Sign out the user from Firebase
                Toast.makeText(HomeActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();

                // Navigate back to Login screen
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish(); // Close HomeActivity so 'Back' button doesn't return here
            }
        });
    }
}