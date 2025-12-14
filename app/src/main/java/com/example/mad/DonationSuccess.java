package com.example.mad;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DonationSuccess extends AppCompatActivity {

    @SuppressLint("SetTextI18n") // Remove Warning
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_success);

        // ---------- Find Dynamically Changed Items ----------
        ProgressBar progressBar = findViewById(R.id.progress_goal);
        TextView donationAmountText = findViewById(R.id.donation_amount);
        TextView goalText = findViewById(R.id.goal);
        TextView countryText = findViewById(R.id.country_name);

        // ---------- Declare Country and Donation Amount ----------
        String country = getIntent().getStringExtra("country");
        int donationAmount = getIntent().getIntExtra("donationAmount", 0);

        SharedPreferences prefs = getSharedPreferences(DonationData.donation_data, MODE_PRIVATE);

        int raised = 0;
        int goal = 0;

        switch (country) {
            case "Sudan":
                raised = prefs.getInt(DonationData.sudan_raised, 12500);
                goal = DonationData.sudan_goal;
                break;
            case "Ukraine":
                raised = prefs.getInt(DonationData.ukraine_raised, 8700);
                goal = DonationData.ukraine_goal;
                break;
            case "India":
                raised = prefs.getInt(DonationData.india_raised, 5200);
                goal = DonationData.india_goal;
                break;
            case "Vietnam":
                raised = prefs.getInt(DonationData.vietnam_raised, 18300);
                goal = DonationData.vietnam_goal;
                break;
        }

        countryText.setText(country);

        int progress = (int) ((raised * 100.0f) / goal);
        progressBar.setProgress(progress);

        donationAmountText.setText("$" + donationAmount + ", equivalent to " + (donationAmount / 5) + " meal(s)");
        goalText.setText("Goal: $" + raised + " / $" + goal);

        // ---------- Function to Return to Campaign Page
        Button returnBtn = findViewById(R.id.return_to_campaign);
        returnBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DonationSuccess.this, CampaignPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

    }
}
