package com.example.test3.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.test3.AddressActivity;
import com.example.test3.ChangePasswordActivity;
import com.example.test3.Man3;
import com.example.test3.ProfileActivity;
import com.example.test3.R;
import com.example.test3.Interface.ApiUser;
import com.example.test3.Model.UserResponse;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CaNhanFragment extends Fragment {

    private FirebaseAuth auth;
    private Button btnLogout, btnAddress, btnProfile;
    private ImageView ivAvatar;
    private SharedPreferences prefs;
    Button btnChangePassword;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ca_nhan, container, false);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        auth = FirebaseAuth.getInstance();
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAddress = view.findViewById(R.id.btnAddress);
        btnProfile = view.findViewById(R.id.btnProfile);

        prefs = requireActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);

        btnLogout.setOnClickListener(v -> logout());
        btnAddress.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddressActivity.class)));
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
        });

        fetchUserInfo();  // Gọi API ngay khi mở Fragment

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserInfo();  // Luôn gọi API để đảm bảo avatar mới được cập nhật
    }

    private void loadUserAvatar(String avatarUrl) {
        Log.d("Avatar", "Avatar mới: " + avatarUrl);
        Glide.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.user_login)
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Không lưu cache
                .skipMemoryCache(true) // Bỏ qua cache trong RAM
                .into(ivAvatar);
    }

    private void fetchUserInfo() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUser apiUser = retrofit.create(ApiUser.class);
        Call<UserResponse> call = apiUser.getUserInfo(userId);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse.User user = response.body().getData();
                    if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                        // Lưu avatar mới vào SharedPreferences
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("avatar_url", user.getAvatar());
                        editor.apply();

                        loadUserAvatar(user.getAvatar());
                    }
                } else {
                    Toast.makeText(getActivity(), "Không thể lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        auth.signOut();
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), Man3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
