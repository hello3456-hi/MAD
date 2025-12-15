package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvUserName, tvEmail, tvInitials;
    private LinearLayout btnEditProfile, btnNotifications, btnPrivacySecurity;
    private LinearLayout btnPaymentMethods, btnDonationHistory, btnHelpSupport;
    private LinearLayout btnAbout, btnLogOut;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        // Find toolbar and set up back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Set up Bottom Navigation
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_profile);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_campaigns) {
                startActivity(new Intent(ProfileActivity.this, CampaignPage.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_ngos) {
                startActivity(new Intent(ProfileActivity.this, NGOListActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_history) {
                startActivity(new Intent(ProfileActivity.this, DonationHistoryActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                // Already on Profile
                return true;
            }

            return false;
        });

        // Find UI elements
        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        tvInitials = findViewById(R.id.tvInitials);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnPrivacySecurity = findViewById(R.id.btnPrivacySecurity);
        btnPaymentMethods = findViewById(R.id.btnPaymentMethods);
        btnDonationHistory = findViewById(R.id.btnDonationHistory);
        btnHelpSupport = findViewById(R.id.btnHelpSupport);
        btnAbout = findViewById(R.id.btnAbout);
        btnLogOut = findViewById(R.id.btnLogOut);

        // Display user info
        if (mAuth.getCurrentUser() != null) {
            String email = mAuth.getCurrentUser().getEmail();
            tvEmail.setText(email);

            // Extract name from email or use default
            String name = email != null ? email.split("@")[0] : "User";
            tvUserName.setText(name);

            // Set initials
            if (name.length() >= 2) {
                tvInitials.setText(name.substring(0, 2).toUpperCase());
            } else {
                tvInitials.setText(name.toUpperCase());
            }
        }

        // Set up click listeners
        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });

        btnNotifications.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, NotificationsActivity.class));
        });

        btnPrivacySecurity.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, PrivacySecurityActivity.class));
        });

        btnPaymentMethods.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, PaymentMethodsActivity.class));
        });

        btnDonationHistory.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, DonationHistoryActivity.class));
        });

        btnHelpSupport.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HelpSupportActivity.class));
        });

        btnAbout.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, AboutActivity.class));
        });

        btnLogOut.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(ProfileActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
