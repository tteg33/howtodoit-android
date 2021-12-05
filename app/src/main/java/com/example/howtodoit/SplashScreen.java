package com.example.howtodoit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


/**
 * Android Splash Screen activity
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        final Intent i = new Intent (SplashScreen.this, MainActivity.class);

        new Handler().postDelayed(() -> {
            startActivity(i);
            finish();
        }, 500);
    }
}