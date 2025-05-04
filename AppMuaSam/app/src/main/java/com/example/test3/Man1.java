package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Man1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man1);

        // Chuyển sang Man2 sau 5 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(Man1.this, Man2.class);
            startActivity(intent);
            finish(); // Đóng Man1 để không quay lại khi nhấn back
        }, 5000); // 5000ms = 5 giây
    }
}
