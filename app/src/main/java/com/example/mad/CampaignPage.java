package com.example.mad;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CampaignPage extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_page);

        // Set up Bottom Navigation
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_campaigns);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(CampaignPage.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_campaigns) {
                // Already on Campaigns
                return true;
            } else if (itemId == R.id.navigation_ngos) {
                startActivity(new Intent(CampaignPage.this, NGOListActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_history) {
                startActivity(new Intent(CampaignPage.this, DonationHistoryActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(CampaignPage.this, ProfileActivity.class));
                finish();
                return true;
            }

            return false;
        });

        // ---------- Find all Progress Bars ----------
        ProgressBar sudanBar = findViewById(R.id.progress_sudan);
        ProgressBar ukraineBar = findViewById(R.id.progress_ukraine);
        ProgressBar indiaBar = findViewById(R.id.progress_india);
        ProgressBar vietnamBar = findViewById(R.id.progress_vietnam);

        // ---------- Update Progress Bars ----------
        updateProgress(sudanBar, 12500, 20000);
        updateProgress(ukraineBar, 8700, 15000);
        updateProgress(indiaBar, 5200, 10000);
        updateProgress(vietnamBar, 18300, 25000);

        // ---------- Donate Buttons ----------
        Button donateSudan = findViewById(R.id.donate_sudan);
        Button donateUkraine = findViewById(R.id.donate_ukraine);
        Button donateIndia = findViewById(R.id.donate_india);
        Button donateVietnam = findViewById(R.id.donate_vietnam);

        // ---------- Open Corresponding Donation Page ----------
        donateSudan.setOnClickListener(v -> openDonationPage("Sudan"));
        donateUkraine.setOnClickListener(v -> openDonationPage("Ukraine"));
        donateIndia.setOnClickListener(v -> openDonationPage("India"));
        donateVietnam.setOnClickListener(v -> openDonationPage("Vietnam"));

    }

    // ---------- Function to Update Campaign Page Dynamically After Donations ----------
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(DonationData.donation_data, MODE_PRIVATE);

        int sudanRaised = prefs.getInt(DonationData.sudan_raised, 12500);
        int sudanProgress = (int) ((sudanRaised * 100.0f) / DonationData.sudan_goal);

        ProgressBar sudanBar = findViewById(R.id.progress_sudan);
        sudanBar.setProgress(sudanProgress);
    }

    // ---------- Function to Calculate and Update Progress ----------
    private void updateProgress(ProgressBar bar, int raised, int goal) {
        int percentage = (int) (((double) raised / goal) * 100);
        bar.setProgress(percentage);
    }

    // ---------- Function to Open Donation Page ----------
    private void openDonationPage(String country) {
        Intent intent = new Intent(CampaignPage.this, DonationPage.class);
        intent.putExtra("country", country);
        startActivity(intent);
    }
}
