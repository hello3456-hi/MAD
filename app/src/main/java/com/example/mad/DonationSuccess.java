package com.example.mad;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DonationSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_success);

        ProgressBar progressBar = findViewById(R.id.progress_goal);
        TextView donationAmountText = findViewById(R.id.donation_amount);
        TextView goalText = findViewById(R.id.goal);

        String country = getIntent().getStringExtra("country");
        int donatedAmount = getIntent().getIntExtra("donatedAmount", 0);

        SharedPreferences prefs = getSharedPreferences(DonationData.donation_data, MODE_PRIVATE);

        int raised = 0;
        int goal = 0;

        if ("Sudan".equals(country)) {
            raised = prefs.getInt(DonationData.sudan_raised, 12500);
            goal = DonationData.sudan_goal;
        }

        int progress = (int) ((raised * 100.0f) / goal);

        progressBar.setProgress(progress);
        donationAmountText.setText("$" + donatedAmount + ", equivalent to " + (donatedAmount / 5) + " meal(s)");
        goalText.setText("Goal: $" + raised + " / $" + goal);
    }
}
