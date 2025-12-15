package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class NGOListActivity extends AppCompatActivity implements NGOAdapter.OnNGOClickListener {

    private BottomNavigationView bottomNavigation;
    private RecyclerView recyclerViewNGOs;
    private NGOAdapter ngoAdapter;
    private List<NGO> ngoList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_list);

        // Find toolbar and set up back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize RecyclerView
        recyclerViewNGOs = findViewById(R.id.recyclerViewNGOs);
        recyclerViewNGOs.setLayoutManager(new LinearLayoutManager(this));

        // Load NGO data
        loadNGOData();

        // Set up adapter
        ngoAdapter = new NGOAdapter(ngoList, this);
        recyclerViewNGOs.setAdapter(ngoAdapter);

        // Set up SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_orange_dark));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Simulate refresh with delay
            new Handler().postDelayed(() -> {
                loadNGOData();
                ngoAdapter = new NGOAdapter(ngoList, this);
                recyclerViewNGOs.setAdapter(ngoAdapter);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "NGO list refreshed", Toast.LENGTH_SHORT).show();
            }, 1500);
        });

        // Set up Bottom Navigation
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_ngos);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(NGOListActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_campaigns) {
                startActivity(new Intent(NGOListActivity.this, CampaignPage.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_ngos) {
                return true;
            } else if (itemId == R.id.navigation_history) {
                startActivity(new Intent(NGOListActivity.this, DonationHistoryActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(NGOListActivity.this, ProfileActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }

    private void loadNGOData() {
        ngoList = new ArrayList<>();

        ngoList.add(new NGO(
                "Global Food Initiative",
                "Los Angeles, CA",
                "Dedicated to eliminating hunger and improving food security through sustainable agriculture and community programs.",
                4.8, 12, 45000, true
        ));

        ngoList.add(new NGO(
                "Meals for Hope",
                "New York, NY",
                "Providing nutritious meals to families in crisis situations and disaster-affected regions worldwide.",
                4.9, 8, 32000, true
        ));

        ngoList.add(new NGO(
                "Feed the Children",
                "Chicago, IL",
                "Working to end childhood hunger by providing food, essentials and hope to families in need.",
                4.7, 15, 78000, true
        ));

        ngoList.add(new NGO(
                "World Hunger Relief",
                "Houston, TX",
                "Combating global hunger through emergency food aid, sustainable agriculture, and community development.",
                4.6, 20, 120000, true
        ));

        ngoList.add(new NGO(
                "Food Bank Network",
                "Seattle, WA",
                "Connecting surplus food with people in need through a network of local food banks and pantries.",
                4.5, 6, 25000, false
        ));
    }

    @Override
    public void onDonateClick(NGO ngo) {
        Toast.makeText(this, "Donating to " + ngo.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DonationPage.class);
        intent.putExtra("ngo_name", ngo.getName());
        startActivity(intent);
    }
}
