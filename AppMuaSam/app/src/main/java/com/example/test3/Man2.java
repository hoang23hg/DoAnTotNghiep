package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Man2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man2);
        Button btnGetStarted = findViewById(R.id.btnGetStarted);
        TextView tvSignIn = findViewById(R.id.tvSignIn);

        // Sự kiện khi bấm vào "Let’s get started"
        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(Man2.this, Man3.class);
            startActivity(intent);
        });

        // Sự kiện khi bấm vào "Sign in"
        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(Man2.this, Man4.class);
            startActivity(intent);
        });
    }
}