package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;

public class HelpSupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        // Find toolbar and set up back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // Set up FAQ section click listeners
        LinearLayout faqGettingStarted = findViewById(R.id.faqGettingStarted);
        LinearLayout faqDonations = findViewById(R.id.faqDonations);
        LinearLayout faqNGOs = findViewById(R.id.faqNGOs);
        LinearLayout faqAccount = findViewById(R.id.faqAccount);

        faqGettingStarted.setOnClickListener(v -> {
            Toast.makeText(this, "Getting Started: Create an account and start donating!", Toast.LENGTH_LONG).show();
        });

        faqDonations.setOnClickListener(v -> {
            Toast.makeText(this, "Donations: Your donations go directly to verified NGOs.", Toast.LENGTH_LONG).show();
        });

        faqNGOs.setOnClickListener(v -> {
            Toast.makeText(this, "NGOs: All our partner NGOs are verified and trusted.", Toast.LENGTH_LONG).show();
        });

        faqAccount.setOnClickListener(v -> {
            Toast.makeText(this, "Account: Manage your profile in the Profile section.", Toast.LENGTH_LONG).show();
        });

        // Set up contact options
        LinearLayout btnLiveChat = findViewById(R.id.btnLiveChat);
        LinearLayout btnEmailUs = findViewById(R.id.btnEmailUs);
        LinearLayout btnCallUs = findViewById(R.id.btnCallUs);

        btnLiveChat.setOnClickListener(v -> {
            Toast.makeText(this, "Live Chat coming soon!", Toast.LENGTH_SHORT).show();
        });

        btnEmailUs.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:support@foodbridge.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "FoodBridge Support Request");
            try {
                startActivity(Intent.createChooser(intent, "Send Email"));
            } catch (Exception e) {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        });

        btnCallUs.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+1234567890"));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Could not open dialer", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
