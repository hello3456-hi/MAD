package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvWelcome, tvUserEmail;
    private BottomNavigationView bottomNavigation;
    private MaterialCardView cardDonate, cardNGOs, cardHistory, cardProfile;
    private MaterialButton btnViewCampaigns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Link UI elements
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        cardDonate = findViewById(R.id.cardDonate);
        cardNGOs = findViewById(R.id.cardNGOs);
        cardHistory = findViewById(R.id.cardHistory);
        cardProfile = findViewById(R.id.cardProfile);
        btnViewCampaigns = findViewById(R.id.btnViewCampaigns);

        // Display welcome message
        if (mAuth.getCurrentUser() != null) {
            String email = mAuth.getCurrentUser().getEmail();
            tvWelcome.setText("Welcome back!");
            tvUserEmail.setText(email);
        } else {
            tvWelcome.setText("Welcome to FoodBridge!");
            tvUserEmail.setText("Guest User");
        }

        // Set up Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.navigation_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                // Already on home
                return true;
            } else if (itemId == R.id.navigation_campaigns) {
                startActivity(new Intent(HomeActivity.this, CampaignPage.class));
                return true;
            } else if (itemId == R.id.navigation_ngos) {
                startActivity(new Intent(HomeActivity.this, NGOListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_history) {
                startActivity(new Intent(HomeActivity.this, DonationHistoryActivity.class));
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            }

            return false;
        });

        // Card click listeners
        cardDonate.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CampaignPage.class));
        });

        cardNGOs.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, NGOListActivity.class));
        });

        cardHistory.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, DonationHistoryActivity.class));
        });

        cardProfile.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });

        btnViewCampaigns.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CampaignPage.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset bottom navigation selection when returning to home
        bottomNavigation.setSelectedItemId(R.id.navigation_home);
    }
}
