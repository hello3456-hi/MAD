package com.example.mad;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


public class DonationPage extends AppCompatActivity {

    // Amount RadioButtons
    RadioButton r5, r10, r25, r50, r100, r200;

    // Custom amount
    EditText customAmount;

    // Donation description
    TextView mealDescription;

    // Payment methods
    RadioButton payCredit, payPayPal, payApple;

    // Donate button
    Button donateBtn;

    int selectedAmount = 0;  // stores the final donation amount

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_page);

        String country = getIntent().getStringExtra("country");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView btnBack = findViewById(R.id.btn_back);

        // ----------- Find Amount Buttons -----------
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

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DonationPage.this, CampaignPage.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(intent);
        });

        // ----------- Handle Amount Selection -----------
        r5.setOnClickListener(v -> selectPresetAmount(5));
        r10.setOnClickListener(v -> selectPresetAmount(10));
        r25.setOnClickListener(v -> selectPresetAmount(25));
        r50.setOnClickListener(v -> selectPresetAmount(50));
        r100.setOnClickListener(v -> selectPresetAmount(100));
        r200.setOnClickListener(v -> selectPresetAmount(200));

        // Create an array of all preset buttons
        RadioButton[] presetButtons = {r5, r10, r25, r50, r100, r200};

        // When a preset button is clicked
        for (RadioButton btn : presetButtons) {
            btn.setOnClickListener(v -> {
                selectedAmount = Integer.parseInt(btn.getText().toString().replace("$","")); // assuming text like "$5"
                customAmount.setText(""); // clear custom
                // uncheck all others
                for (RadioButton otherBtn : presetButtons) {
                    if (otherBtn != btn) otherBtn.setChecked(false);
                }
                updateMealDescription();
            });
        }


        // ----------- Custom Amount Logic -----------
        customAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {

                    // Uncheck all preset buttons
                    clearPresetSelection();

                    // Convert input to integer
                    try {
                        selectedAmount = Integer.parseInt(s.toString());
                    } catch (Exception e) {
                        selectedAmount = 0;
                    }

                    updateMealDescription();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        // ----------- Payment Method (Only one selectable) -----------
        payCredit.setOnClickListener(v -> clearPaymentExcept(payCredit));
        payPayPal.setOnClickListener(v -> clearPaymentExcept(payPayPal));
        payApple.setOnClickListener(v -> clearPaymentExcept(payApple));


        // ----------- Donate Button -----------
        donateBtn.setOnClickListener(v -> {

            // Ensure user selects at least amount & payment
            if (selectedAmount <= 0) {
                customAmount.setError("Please enter or select a donation amount");
                return;
            }

            if (!payCredit.isChecked() && !payPayPal.isChecked() && !payApple.isChecked()) {
                payCredit.setError("Select a payment method");
                return;
            }

            SharedPreferences prefs = getSharedPreferences(DonationData.donation_data, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            // Example: user donated to Sudan
            int donationAmount = selectedAmount; // from radio/custom input

            int currentRaised = 0;

            switch (country) {
                case "Sudan":
                    currentRaised = prefs.getInt(DonationData.sudan_raised, 12500);
                    editor.putInt(DonationData.sudan_raised, currentRaised + donationAmount);
                    break;

                case "Ukraine":
                    currentRaised = prefs.getInt(DonationData.ukraine_raised, 8700);
                    editor.putInt(DonationData.ukraine_raised, currentRaised + donationAmount);
                    break;

                case "India":
                    currentRaised = prefs.getInt(DonationData.india_raised, 5200);
                    editor.putInt(DonationData.india_raised, currentRaised + donationAmount);
                    break;

                case "Vietnam":
                    currentRaised = prefs.getInt(DonationData.vietnam_raised, 18300);
                    editor.putInt(DonationData.vietnam_raised, currentRaised + donationAmount);
                    break;
            }

            editor.apply();


            // Navigate to success page
            Intent intent = new Intent(DonationPage.this, DonationSuccess.class);
            intent.putExtra("country", country);
            intent.putExtra("donationAmount", selectedAmount);
            startActivity(intent);
        });
    }


    // ------------ Helper: When user taps a preset amount ------------
    private void selectPresetAmount(int amount) {
        selectedAmount = amount;

        // Clear custom input
        customAmount.setText("");

        updateMealDescription();
    }

    // ------------ Helper: Update donation description dynamically ------------
    private void updateMealDescription() {
        int meals = selectedAmount / 5;  // floor division

        if (meals <= 0) {
            mealDescription.setText("Donates 0 Meals");
            return;
        }

        // Correct grammar
        if (meals == 1) {
            mealDescription.setText("Donates 1 Meal");
        } else {
            mealDescription.setText("Donates " + meals + " Meals");
        }
    }

    // ------------ Helper: Uncheck all preset amount buttons ------------
    private void clearPresetSelection() {
        r5.setChecked(false);
        r10.setChecked(false);
        r25.setChecked(false);
        r50.setChecked(false);
        r100.setChecked(false);
        r200.setChecked(false);
    }

    // ------------ Helper: Payment selection (only one at a time) ------------
    private void clearPaymentExcept(RadioButton selected) {
        payCredit.setChecked(selected == payCredit);
        payPayPal.setChecked(selected == payPayPal);
        payApple.setChecked(selected == payApple);
    }
}
