package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay and then check auth state
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                // User is logged in, go to Home
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            } else {
                // User not logged in, go to Login
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}
