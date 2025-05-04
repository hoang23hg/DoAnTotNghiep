package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText phoneEditText, otpEditText;
    private Button btnSendOtp, btnVerifyOtp;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        mAuth = FirebaseAuth.getInstance();
        phoneEditText = findViewById(R.id.phoneEditText);
        otpEditText = findViewById(R.id.otpEditText);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        ImageView back_image = findViewById(R.id.back_image);

        back_image.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPhoneActivity.this, Man3.class);

            startActivity(intent);
        });

        btnSendOtp.setOnClickListener(v -> {
            String phoneNumber = phoneEditText.getText().toString().trim();
            if (TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(LoginPhoneActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }
            sendOtp(phoneNumber);
        });

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = otpEditText.getText().toString().trim();
            if (TextUtils.isEmpty(otp)) {
                Toast.makeText(LoginPhoneActivity.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyOtp(otp);
        });
    }

    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber.substring(1)) // Định dạng số điện thoại VN (+84)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(LoginPhoneActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                verificationId = id;
                                otpEditText.setVisibility(View.VISIBLE); // Hiện ô nhập OTP
                                btnVerifyOtp.setVisibility(View.VISIBLE); // Hiện nút xác minh OTP
                                Toast.makeText(LoginPhoneActivity.this, "Mã OTP đã được gửi!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOtp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginPhoneActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginPhoneActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginPhoneActivity.this, "Lỗi đăng nhập: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
