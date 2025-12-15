package com.example.mad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class PaymentMethodsActivity extends AppCompatActivity {

    private ImageView btnVisaOptions;
    private ImageView btnMastercardOptions;
    private ImageView btnEmailOptions;
    private MaterialButton btnAddPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        // Find toolbar and set up back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // Initialize views
        btnVisaOptions = findViewById(R.id.btnVisaOptions);
        btnMastercardOptions = findViewById(R.id.btnMastercardOptions);
        btnEmailOptions = findViewById(R.id.btnEmailOptions);
        btnAddPaymentMethod = findViewById(R.id.btnAddPaymentMethod);

        // Set up click listeners for card options
        if (btnVisaOptions != null) {
            btnVisaOptions.setOnClickListener(v -> showCardOptionsMenu(v, "Visa •••• 1234"));
        }

        if (btnMastercardOptions != null) {
            btnMastercardOptions.setOnClickListener(v -> showCardOptionsMenu(v, "Mastercard •••• 5678"));
        }

        if (btnEmailOptions != null) {
            btnEmailOptions.setOnClickListener(v -> showCardOptionsMenu(v, "PayPal"));
        }

        // Set up add payment method button
        if (btnAddPaymentMethod != null) {
            btnAddPaymentMethod.setOnClickListener(v -> showAddPaymentMethodDialog());
        }
    }

    private void showCardOptionsMenu(View view, String cardName) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add("Set as Default");
        popup.getMenu().add("Edit");
        popup.getMenu().add("Remove");

        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            switch (title) {
                case "Set as Default":
                    Toast.makeText(this, cardName + " set as default", Toast.LENGTH_SHORT).show();
                    return true;
                case "Edit":
                    Toast.makeText(this, "Edit " + cardName, Toast.LENGTH_SHORT).show();
                    return true;
                case "Remove":
                    showRemoveConfirmation(cardName);
                    return true;
                default:
                    return false;
            }
        });

        popup.show();
    }

    private void showRemoveConfirmation(String cardName) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Payment Method")
                .setMessage("Are you sure you want to remove " + cardName + "?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    Toast.makeText(this, cardName + " removed", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showAddPaymentMethodDialog() {
        String[] options = {"Credit/Debit Card", "PayPal", "Google Pay", "Bank Account"};

        new AlertDialog.Builder(this)
                .setTitle("Add Payment Method")
                .setItems(options, (dialog, which) -> {
                    String selected = options[which];
                    Toast.makeText(this, "Adding " + selected + " - Coming soon!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
