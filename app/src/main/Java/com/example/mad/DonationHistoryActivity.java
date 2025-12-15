package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class DonationHistoryActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private RecyclerView recyclerViewDonations;
    private DonationAdapter donationAdapter;
    private List<Donation> donationList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);

        // Find toolbar and set up back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // Initialize RecyclerView
        recyclerViewDonations = findViewById(R.id.recyclerViewDonations);
        recyclerViewDonations.setLayoutManager(new LinearLayoutManager(this));

        // Load donation data
        loadDonationData();

        // Set up adapter
        donationAdapter = new DonationAdapter(donationList);
        recyclerViewDonations.setAdapter(donationAdapter);

        // Set up SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_orange_dark));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Simulate refresh with delay
            new Handler().postDelayed(() -> {
                loadDonationData();
                donationAdapter.updateList(donationList);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "Donations refreshed", Toast.LENGTH_SHORT).show();
            }, 1500);
        });

        // Set up Tab Layout for filtering
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        if (tabLayout != null) {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    filterDonations(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }

        // Set up Bottom Navigation
        bottomNavigation = findViewById(R.id.bottomNavigation);
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.navigation_history);
            bottomNavigation.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(DonationHistoryActivity.this, HomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_campaigns) {
                    startActivity(new Intent(DonationHistoryActivity.this, CampaignPage.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_ngos) {
                    startActivity(new Intent(DonationHistoryActivity.this, NGOListActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_history) {
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(DonationHistoryActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                }

                return false;
            });
        }
    }

    private void loadDonationData() {
        donationList = new ArrayList<>();

        // Sample donation history data
        donationList.add(new Donation("Sudan Food Crisis Relief", "Dec 10, 2024", 50, 200));
        donationList.add(new Donation("Ukraine Emergency Aid", "Nov 28, 2024", 100, 400));
        donationList.add(new Donation("India Hunger Relief", "Nov 15, 2024", 25, 100));
        donationList.add(new Donation("Vietnam Food Support", "Oct 30, 2024", 75, 300));
        donationList.add(new Donation("Sudan Food Crisis Relief", "Oct 15, 2024", 100, 400));
    }

    private void filterDonations(int tabPosition) {
        // Filter based on tab position (0=All Time, 1=This Month, 2=Last Month, 3=This Year)
        // For demo purposes, we show all donations
        donationAdapter.updateList(donationList);
    }
}
