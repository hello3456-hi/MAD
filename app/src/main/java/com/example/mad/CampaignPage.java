package com.example.mad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

public class CampaignPage extends AppCompatActivity {

    // Declare Progress Bars
    private ProgressBar progSudan, progUkraine, progIndia, progVietnam;

    // Declare Text Views for "Raised Amount"
    private TextView txtSudanRaised, txtUkraineRaised, txtIndiaRaised, txtVietnamRaised;

    // Declare Donate Buttons
    private Button btnSudan, btnUkraine, btnIndia, btnVietnam;

    // Bottom Navigation
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSharedPreferences(DonationData.donation_data, MODE_PRIVATE).edit().clear().apply();
        setContentView(R.layout.campaign_page);

        // 1. Initialize Progress Bars
        progSudan = findViewById(R.id.progress_sudan);
        progUkraine = findViewById(R.id.progress_ukraine);
        progIndia = findViewById(R.id.progress_india);
        progVietnam = findViewById(R.id.progress_vietnam);

        // 2. Initialize Text Views
        txtSudanRaised = findViewById(R.id.text_raised_sudan);
        txtUkraineRaised = findViewById(R.id.text_raised_ukraine);
        txtIndiaRaised = findViewById(R.id.text_raised_india);
        txtVietnamRaised = findViewById(R.id.text_raised_vietnam);

        // 3. Initialize Buttons
        btnSudan = findViewById(R.id.donate_sudan);
        btnUkraine = findViewById(R.id.donate_ukraine);
        btnIndia = findViewById(R.id.donate_india);
        btnVietnam = findViewById(R.id.donate_vietnam);

        // 4. Setup Button Listeners
        setupDonateButton(btnSudan, "Sudan");
        setupDonateButton(btnUkraine, "Ukraine");
        setupDonateButton(btnIndia, "India");
        setupDonateButton(btnVietnam, "Vietnam");


        // 5. BOTTOM NAVIGATION
        bottomNavigation = findViewById(R.id.bottomNavigation);

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.navigation_campaigns);

            bottomNavigation.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(CampaignPage.this, HomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_campaigns) {
                    // We are already here, do nothing
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
        }
    }

    // This runs every time the page appears
    @Override
    protected void onResume() {
        super.onResume();
        refreshCampaignData();
    }

    // Setup buttons
    private void setupDonateButton(Button btn, String countryName) {
        if (btn != null) {
            btn.setOnClickListener(v -> {
                Intent intent = new Intent(CampaignPage.this, DonationPage.class);
                intent.putExtra("country", countryName);
                startActivity(intent);
            });
        }
    }

    // The main function to update UI from SharedPreferences
    @SuppressLint("SetTextI18n")
    private void refreshCampaignData() {
        SharedPreferences prefs = getSharedPreferences(DonationData.donation_data, MODE_PRIVATE);

        // --- SUDAN ---
        int sudanRaised = prefs.getInt(DonationData.sudan_raised, 12500);
        int sudanGoal = DonationData.sudan_goal;
        if (progSudan != null) progSudan.setProgress(calculateProgress(sudanRaised, sudanGoal));
        if (txtSudanRaised != null) txtSudanRaised.setText("$" + formatNumber(sudanRaised) + " raised");

        // --- UKRAINE ---
        int ukraineRaised = prefs.getInt(DonationData.ukraine_raised, 8700);
        int ukraineGoal = DonationData.ukraine_goal;
        if (progUkraine != null) progUkraine.setProgress(calculateProgress(ukraineRaised, ukraineGoal));
        if (txtUkraineRaised != null) txtUkraineRaised.setText("$" + formatNumber(ukraineRaised) + " raised");

        // --- INDIA ---
        int indiaRaised = prefs.getInt(DonationData.india_raised, 5200);
        int indiaGoal = DonationData.india_goal;
        if (progIndia != null) progIndia.setProgress(calculateProgress(indiaRaised, indiaGoal));
        if (txtIndiaRaised != null) txtIndiaRaised.setText("$" + formatNumber(indiaRaised) + " raised");

        // --- VIETNAM ---
        int vietnamRaised = prefs.getInt(DonationData.vietnam_raised, 18300);
        int vietnamGoal = DonationData.vietnam_goal;
        if (progVietnam != null) progVietnam.setProgress(calculateProgress(vietnamRaised, vietnamGoal));
        if (txtVietnamRaised != null) txtVietnamRaised.setText("$" + formatNumber(vietnamRaised) + " raised");
    }

    private int calculateProgress(int raised, int goal) {
        if (goal <= 0) return 0;
        int progress = (int) ((raised * 100.0f) / goal);
        return Math.min(progress, 100);
    }

    private String formatNumber(int number) {
        return String.format("%,d", number);
    }
}