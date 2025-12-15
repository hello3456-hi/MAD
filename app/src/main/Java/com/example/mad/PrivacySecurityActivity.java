package com.example.mad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrivacySecurityActivity extends AppCompatActivity {

    private SwitchMaterial switchAnonymousDonation;
    private SwitchMaterial switchProfileVisibility;
    private LinearLayout btnTwoFactor;
    private LinearLayout btnChangePassword;
    private LinearLayout btnDeleteAccount;
    private SharedPreferences prefs;
    private FirebaseAuth mAuth;
    private static final String PREFS_NAME = "privacy_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_security);

        mAuth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Find toolbar and set up back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // Initialize views
        switchAnonymousDonation = findViewById(R.id.switchAnonymousDonation);
        switchProfileVisibility = findViewById(R.id.switchProfileVisibility);
        btnTwoFactor = findViewById(R.id.btnTwoFactor);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        // Load saved preferences
        loadPreferences();

        // Set up switch listeners
        switchAnonymousDonation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference("anonymous_donation", isChecked);
            Toast.makeText(this, isChecked ? "Anonymous donations enabled" : "Anonymous donations disabled", Toast.LENGTH_SHORT).show();
        });

        switchProfileVisibility.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference("profile_visibility", isChecked);
            Toast.makeText(this, isChecked ? "Profile is now visible" : "Profile is now hidden", Toast.LENGTH_SHORT).show();
        });

        // Set up button click listeners
        btnTwoFactor.setOnClickListener(v -> showTwoFactorDialog());
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        btnDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
    }

    private void loadPreferences() {
        switchAnonymousDonation.setChecked(prefs.getBoolean("anonymous_donation", true));
        switchProfileVisibility.setChecked(prefs.getBoolean("profile_visibility", false));
    }

    private void savePreference(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void showTwoFactorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Two-Factor Authentication")
                .setMessage("Two-factor authentication adds an extra layer of security to your account. This feature will be available soon.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showChangePasswordDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText currentPassword = new EditText(this);
        currentPassword.setHint("Current Password");
        currentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(currentPassword);

        EditText newPassword = new EditText(this);
        newPassword.setHint("New Password");
        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newPassword);

        EditText confirmPassword = new EditText(this);
        confirmPassword.setHint("Confirm New Password");
        confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(confirmPassword);

        new AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setView(layout)
                .setPositiveButton("Change", (dialog, which) -> {
                    String current = currentPassword.getText().toString();
                    String newPass = newPassword.getText().toString();
                    String confirm = confirmPassword.getText().toString();

                    if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!newPass.equals(confirm)) {
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (newPass.length() < 6) {
                        Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    changePassword(current, newPass);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void changePassword(String currentPassword, String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || user.getEmail() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Re-authenticate user first
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    // Now update password
                    user.updatePassword(newPassword)
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to change password: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone and all your data will be permanently lost.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    showConfirmDeleteDialog();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showConfirmDeleteDialog() {
        EditText passwordInput = new EditText(this);
        passwordInput.setHint("Enter your password to confirm");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        layout.addView(passwordInput);

        new AlertDialog.Builder(this)
                .setTitle("Confirm Account Deletion")
                .setView(layout)
                .setPositiveButton("Delete My Account", (dialog, which) -> {
                    String password = passwordInput.getText().toString();
                    if (password.isEmpty()) {
                        Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    deleteAccount(password);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount(String password) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || user.getEmail() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Re-authenticate before deletion
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    user.delete()
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                // Redirect to login
                                finishAffinity();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to delete account: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                });
    }
}
