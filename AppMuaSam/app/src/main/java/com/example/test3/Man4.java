package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test3.Interface.ApiUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Man4 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etEmail,etUsername, etPassword,etRepeatPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man4);

        mAuth = FirebaseAuth.getInstance();


        Button btnSignUp = findViewById(R.id.btnSignUp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);

        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String repeatPassword = etRepeatPassword.getText().toString().trim(); // Lấy đúng giá trị mật khẩu nhập lại

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(Man4.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            } else if (!isValidEmail(email)) {
                Toast.makeText(Man4.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(repeatPassword)) { // So sánh đúng cách
                Toast.makeText(Man4.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
            } else {

                registerUser(email, password);
            }
        });



        ImageView back_image = findViewById(R.id.back_image);

        back_image.setOnClickListener(view -> {
            Intent intent = new Intent(Man4.this, Man3.class);
            startActivity(intent);
        });

        TextView tvSignInLink= findViewById(R.id.tvSignInLink);

        tvSignInLink.setOnClickListener(v -> {
            Intent intent = new Intent(Man4.this, Man3.class);
            startActivity(intent);
        });

        ImageView ivTogglePassword = findViewById(R.id.ivTogglePassword);
        EditText etPassword = findViewById(R.id.etPassword);

        ivTogglePassword.setOnClickListener(v -> {
            if (etPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.closeview); // Icon con mắt mở
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.view); // Icon con mắt đóng
            }
            etPassword.setSelection(etPassword.getText().length()); // Giữ con trỏ ở cuối
        });
        EditText etRepeatPassword = findViewById(R.id.etRepeatPassword);

        ImageView ivToggleRepeatPassword = findViewById(R.id.ivToggleRepeatPassword);

        ivToggleRepeatPassword.setOnClickListener(v -> {
            if (etRepeatPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                etRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivToggleRepeatPassword.setImageResource(R.drawable.closeview); // Icon con mắt mở
            } else {
                etRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivToggleRepeatPassword.setImageResource(R.drawable.view); // Icon con mắt đóng
            }
            etRepeatPassword.setSelection(etRepeatPassword.getText().length()); // Giữ con trỏ ở cuối
        });
    }
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification().addOnCompleteListener(emailTask -> {
                                if (emailTask.isSuccessful()) {
                                    Toast.makeText(Man4.this, "Đăng ký thành công! Kiểm tra email để xác minh.", Toast.LENGTH_LONG).show();
                                    syncUserToServer(user.getUid(), email);  // Gửi dữ liệu lên MySQL
                                    Intent intent = new Intent(Man4.this, Man3.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Man4.this, "Lỗi gửi email: " + emailTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(Man4.this, "Lỗi đăng ký: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void syncUserToServer(String uid, String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")  // Đổi thành URL của server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUser apiService = retrofit.create(ApiUser.class);
        Call<ResponseBody> call = apiService.syncUser(uid, email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("Sync", "User synced successfully");
                } else {
                    Log.e("Sync", "Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Sync", "Error syncing user", t);
            }
        });
    }

}