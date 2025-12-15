package com.example.mad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DonationPage extends AppCompatActivity {

    RadioButton r5, r10, r25, r50, r100, r200;
    EditText customAmount;
    TextView mealDescription;
    RadioButton payCredit, payPayPal, payApple;
    Button donateBtn;
    TextView btnBack;

    int selectedAmount = 0;
    RadioButton[] presetButtons; // Helper array for clearing selections

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_page);

        // Retrieve country from previous page
        String country = getIntent().getStringExtra("country");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // ----------- Initialize Views -----------
        btnBack = findViewById(R.id.btn_back);
        r5 = findViewById(R.id.r_amount5);
        r10 = findViewById(R.id.r_amount10);
        r25 = findViewById(R.id.r_amount25);
        r50 = findViewById(R.id.r_amount50);
        r100 = findViewById(R.id.r_amount100);
        r200 = findViewById(R.id.r_amount200);

        customAmount = findViewById(R.id.custom_amount);
        mealDescription = findViewById(R.id.meal_description);
        payCredit = findViewById(R.id.payCredit);
        payPayPal = findViewById(R.id.payPayPal);
        payApple = findViewById(R.id.payApple);
        donateBtn = findViewById(R.id.donate_food);

        // Group buttons for easy clearing
        presetButtons = new RadioButton[]{r5, r10, r25, r50, r100, r200};

        // ----------- Navigation -----------
        btnBack.setOnClickListener(v -> {
            // Go back to CampaignPage without creating a new duplicate instance
            Intent intent = new Intent(DonationPage.this, CampaignPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // ----------- Handle Amount Selection -----------
        // We use explicit values here to avoid parsing errors from text (e.g. "$5")
        r5.setOnClickListener(v -> selectPresetAmount(5));
        r10.setOnClickListener(v -> selectPresetAmount(10));
        r25.setOnClickListener(v -> selectPresetAmount(25));
        r50.setOnClickListener(v -> selectPresetAmount(50));
        r100.setOnClickListener(v -> selectPresetAmount(100));
        r200.setOnClickListener(v -> selectPresetAmount(200));

        // ----------- Custom Amount Logic -----------
        customAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) return;

                // Uncheck all preset buttons if user types custom amount
                clearPresetSelection();

                try {
                    selectedAmount = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    selectedAmount = 0;
                }
                updateMealDescription();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // ----------- Payment Method (Only One Selectable) -----------
        payCredit.setOnClickListener(v -> clearPaymentExcept(payCredit));
        payPayPal.setOnClickListener(v -> clearPaymentExcept(payPayPal));
        payApple.setOnClickListener(v -> clearPaymentExcept(payApple));

        // ----------- Donate Button -----------
        donateBtn.setOnClickListener(v -> {
            if (selectedAmount <= 0) {
                customAmount.setError("Please enter or select a donation amount");
                return;
            }
            if (!payCredit.isChecked() && !payPayPal.isChecked() && !payApple.isChecked()) {
                payCredit.setError("Select a payment method"); // Just setting error on one radio to show msg
                return;
            }
//Get Current Date
            String currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());

// Create the Donation Object
// Assuming your Donation class constructor is: Donation(Name, Date, Amount, Meals)
            Donation newDonation = new Donation(
                    country,                // Campaign Name (e.g., "Sudan")
                    currentDate,            // Date
                    selectedAmount,         // Amount
                    selectedAmount / 5      // Meals
            );

// Add to the Global List (at the top of the list)
            DonationData.historyList.add(0, newDonation);


            // 1. Get current data
            SharedPreferences prefs = getSharedPreferences(DonationData.donation_data, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            int currentRaised = 0;

            // 2. Update the specific country's total
            if (country != null) {
                switch (country) {
                    case "Sudan":
                        currentRaised = prefs.getInt(DonationData.sudan_raised, 12500);
                        editor.putInt(DonationData.sudan_raised, currentRaised + selectedAmount);
                        break;
                    case "Ukraine":
                        currentRaised = prefs.getInt(DonationData.ukraine_raised, 8700);
                        editor.putInt(DonationData.ukraine_raised, currentRaised + selectedAmount);
                        break;
                    case "India":
                        currentRaised = prefs.getInt(DonationData.india_raised, 5200);
                        editor.putInt(DonationData.india_raised, currentRaised + selectedAmount);
                        break;
                    case "Vietnam":
                        currentRaised = prefs.getInt(DonationData.vietnam_raised, 18300);
                        editor.putInt(DonationData.vietnam_raised, currentRaised + selectedAmount);
                        break;
                }
                editor.apply(); // IMPORTANT: This saves it to storage immediately
            }

            // 3. Move to Success Page
            Intent intent = new Intent(DonationPage.this, DonationSuccess.class);
            intent.putExtra("country", country);
            intent.putExtra("donationAmount", selectedAmount);
            startActivity(intent);
        });
    }

    private void selectPresetAmount(int amount) {
        selectedAmount = amount;
        customAmount.setText(""); // Clear custom input

        // Ensure the correct button is visually checked
        // (This loop handles the visual state if selectPresetAmount is called programmatically)
        for (RadioButton btn : presetButtons) {
            btn.setChecked(false);
        }

        // Check the specific button matching the amount
        if (amount == 5) r5.setChecked(true);
        else if (amount == 10) r10.setChecked(true);
        else if (amount == 25) r25.setChecked(true);
        else if (amount == 50) r50.setChecked(true);
        else if (amount == 100) r100.setChecked(true);
        else if (amount == 200) r200.setChecked(true);

        updateMealDescription();
    }

    private void updateMealDescription() {
        int meals = selectedAmount / 5;
        if (meals <= 0) mealDescription.setText("Donates 0 Meals");
        else if (meals == 1) mealDescription.setText("Donates 1 Meal");
        else mealDescription.setText("Donates " + meals + " Meals");
    }

    private void clearPresetSelection() {
        for (RadioButton btn : presetButtons) {
            btn.setChecked(false);
        }
    }

    private void clearPaymentExcept(RadioButton selected) {
        payCredit.setChecked(selected == payCredit);
        payPayPal.setChecked(selected == payPayPal);
        payApple.setChecked(selected == payApple);
    }
}