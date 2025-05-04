package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Man5 extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man5);

        ImageView back_image = findViewById(R.id.back_image);
        back_image.setOnClickListener(view -> {
            Intent intent = new Intent(Man5.this, Man3.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();

        Button sendemail = findViewById(R.id.sendemail);
        EditText et_username = findViewById(R.id.et_username);

        sendemail.setOnClickListener(v -> {
            String email = et_username.getText().toString().trim();

            // Kiểm tra email có rỗng không
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Man5.this, "Vui lòng nhập email để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra định dạng email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(Man5.this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gửi email đặt lại mật khẩu
            resetPassword(email);
        });
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Man5.this, "Email đặt lại mật khẩu đã được gửi!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang màn Man6 khi gửi email thành công
                        Intent intent = new Intent(Man5.this, Man6.class);
                        intent.putExtra("EMAIL_KEY", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Man5.this, "Lỗi gửi email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
