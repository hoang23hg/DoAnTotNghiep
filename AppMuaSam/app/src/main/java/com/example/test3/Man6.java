package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Man6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man6);
        ImageView back_image = findViewById(R.id.back_image);

        back_image.setOnClickListener(view -> {
            Intent intent = new Intent(Man6.this, Man5.class);

            startActivity(intent);
        });
        TextView back_email = findViewById(R.id.back_email);
        String email = getIntent().getStringExtra("EMAIL_KEY");

        if (email != null) {
            back_email.setText(email);
        }

    }
}