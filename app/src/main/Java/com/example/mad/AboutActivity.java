package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Find toolbar and set up back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // Set up click listeners for information links
        LinearLayout btnTermsOfService = findViewById(R.id.btnTermsOfService);
        LinearLayout btnPrivacyPolicy = findViewById(R.id.btnPrivacyPolicy);
        LinearLayout btnLicenses = findViewById(R.id.btnLicenses);

        btnTermsOfService.setOnClickListener(v -> {
            Toast.makeText(this, "Terms of Service", Toast.LENGTH_SHORT).show();
        });

        btnPrivacyPolicy.setOnClickListener(v -> {
            Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show();
        });

        btnLicenses.setOnClickListener(v -> {
            Toast.makeText(this, "Open Source Licenses", Toast.LENGTH_SHORT).show();
        });

        // Set up social media links
        findViewById(R.id.btnFacebook).setOnClickListener(v -> openUrl("https://facebook.com"));
        findViewById(R.id.btnTwitter).setOnClickListener(v -> openUrl("https://twitter.com"));
        findViewById(R.id.btnInstagram).setOnClickListener(v -> openUrl("https://instagram.com"));
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open link", Toast.LENGTH_SHORT).show();
        }
    }
}
