package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class NotificationsActivity extends AppCompatActivity {

    private SwitchMaterial switchCampaignUpdates;
    private SwitchMaterial switchUrgentCampaigns;
    private SwitchMaterial switchDonationReceipts;
    private SwitchMaterial switchMonthlySummary;
    private MaterialButton btnSavePreferences;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "notification_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Find toolbar and set up back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // Initialize switches
        switchCampaignUpdates = findViewById(R.id.switchCampaignUpdates);
        switchUrgentCampaigns = findViewById(R.id.switchUrgentCampaigns);
        switchDonationReceipts = findViewById(R.id.switchDonationReceipts);
        switchMonthlySummary = findViewById(R.id.switchMonthlySummary);
        btnSavePreferences = findViewById(R.id.btnSavePreferences);

        // Load saved preferences
        loadPreferences();

        // Set up save button
        btnSavePreferences.setOnClickListener(v -> savePreferences());
    }

    private void loadPreferences() {
        switchCampaignUpdates.setChecked(prefs.getBoolean("campaign_updates", true));
        switchUrgentCampaigns.setChecked(prefs.getBoolean("urgent_campaigns", true));
        switchDonationReceipts.setChecked(prefs.getBoolean("donation_receipts", true));
        switchMonthlySummary.setChecked(prefs.getBoolean("monthly_summary", false));
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("campaign_updates", switchCampaignUpdates.isChecked());
        editor.putBoolean("urgent_campaigns", switchUrgentCampaigns.isChecked());
        editor.putBoolean("donation_receipts", switchDonationReceipts.isChecked());
        editor.putBoolean("monthly_summary", switchMonthlySummary.isChecked());
        editor.apply();

        Toast.makeText(this, "Notification preferences saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
