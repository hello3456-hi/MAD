package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog; // Import for the Popup
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLogin;
    private TextView tvShowSignUp, tvForgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // 2. Link Main Login UI
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvShowSignUp = findViewById(R.id.tvShowSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBar = findViewById(R.id.progressBar);

        // 3. Login Button Click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });

        // 4. "No Account?" Click -> Open Popup
        tvShowSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        // 5. Forgot Password Click
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordDialog();
            }
        });
    }

    // --- CHECK USER PERSISTENCE (Auto Login) ---
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // User is already logged in, go to Home
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }

    // --- LOGIC 1: LOGIN ---
    private void performLogin() {
        String email = etEmailLogin.getText().toString();
        String password = etPasswordLogin.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading spinner
        showLoading(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showLoading(false);
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // --- HELPER: Show/Hide Loading ---
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        etEmailLogin.setEnabled(!show);
        etPasswordLogin.setEnabled(!show);
    }

    // --- LOGIC 2: SHOW POPUP & SIGN UP ---
    private void showSignUpDialog() {
        // 1. Prepare the layout for the popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sign_up, null);
        builder.setView(dialogView);

        // 2. Link the elements INSIDE the popup
        final EditText etEmailSignUp = dialogView.findViewById(R.id.etEmailSignUp);
        final EditText etPasswordSignUp = dialogView.findViewById(R.id.etPasswordSignUp);
        Button btnRegister = dialogView.findViewById(R.id.btnRegister);

        // 3. Create and Show the Dialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // 4. Handle Register Button Click INSIDE the popup
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailSignUp.getText().toString().trim();
                String password = etPasswordSignUp.getText().toString().trim();

                if (TextUtils.isEmpty(email) || password.length() < 6) {
                    Toast.makeText(MainActivity.this, "Invalid email or password too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create User in Firebase
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Account Created! Logging in...", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss(); // Close the popup

                                    // Go to Home Page
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    // --- LOGIC 3: FORGOT PASSWORD ---
    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_forgot_password, null);
        builder.setView(dialogView);

        final EditText etEmailReset = dialogView.findViewById(R.id.etEmailReset);
        Button btnResetPassword = dialogView.findViewById(R.id.btnResetPassword);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailReset.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Password reset email sent!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}