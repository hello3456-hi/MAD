package com.example.mad;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CampaignPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_page);

        // ---------- 1. Find all Progress Bars ----------
        ProgressBar sudanBar = findViewById(R.id.progress_sudan);
        ProgressBar ukraineBar = findViewById(R.id.progress_ukraine);
        ProgressBar indiaBar = findViewById(R.id.progress_india);
        ProgressBar vietnamBar = findViewById(R.id.progress_vietnam);

        // ---------- 2. Update Progress Bars ----------
        updateProgress(sudanBar, 12500, 20000);
        updateProgress(ukraineBar, 8700, 15000);
        updateProgress(indiaBar, 5200, 10000);
        updateProgress(vietnamBar, 18300, 25000);

        // ---------- 3. Donate Buttons ----------
        Button donateSudan = findViewById(R.id.donate_sudan);
        Button donateUkraine = findViewById(R.id.donate_ukraine);
        Button donateIndia = findViewById(R.id.donate_india);
        Button donateVietnam = findViewById(R.id.donate_vietnam);

        donateSudan.setOnClickListener(v -> openDonationPage());
        donateUkraine.setOnClickListener(v -> openDonationPage());
        donateIndia.setOnClickListener(v -> openDonationPage());
        donateVietnam.setOnClickListener(v -> openDonationPage());
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(DonationData.donation_data, MODE_PRIVATE);

        int sudanRaised = prefs.getInt(DonationData.sudan_raised, 12500);
        int sudanProgress = (int) ((sudanRaised * 100.0f) / DonationData.sudan_goal);

        ProgressBar sudanBar = findViewById(R.id.progress_sudan);
        sudanBar.setProgress(sudanProgress);
    }

    // ---------- Function to calculate and update progress ----------
    private void updateProgress(ProgressBar bar, int raised, int goal) {
        int percentage = (int) (((double) raised / goal) * 100);
        bar.setProgress(percentage);
    }

    // ---------- Function to open donation page ----------
    private void openDonationPage() {
        Intent intent = new Intent(CampaignPage.this, DonationPage.class);
        startActivity(intent);
    }
}
