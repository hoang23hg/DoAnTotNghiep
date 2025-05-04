package com.example.test3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.test3.Interface.ApiService;
import com.example.test3.Interface.ApiUser;
import com.example.test3.Model.UserResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private TextView tvDisplayName, tvEmail, tvPhone;
    ImageView ivAvatar;
    private Uri imageUri;
    private ImageView imageView;
    private Button btnChooseImage, btnUploadImage,btnEditProfile;
    private File selectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        ivAvatar = findViewById(R.id.iv_avatar);
        fetchUserInfo();

        btnEditProfile = findViewById(R.id.btnEditProfile);

        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imageView = findViewById(R.id.iv_avatar);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        btnChooseImage.setOnClickListener(v -> checkStoragePermission());
        btnUploadImage.setOnClickListener(v -> {
            if (selectedFile != null) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                uploadImage(selectedFile, userId);

            } else {
                Toast.makeText(this, "Vui lòng chọn ảnh trước!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Kiểm tra quyền trước khi mở thư viện ảnh
    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, STORAGE_PERMISSION_CODE);
            } else {
                openGallery();
            }
        } else { // Android 12 trở xuống
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                openGallery();
            }
        }
    }

    // Mở thư viện ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Xử lý khi người dùng chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri); // Hiển thị ảnh được chọn

            String imagePath = getRealPathFromURI(imageUri);
            if (imagePath != null) {
                selectedFile = new File(imagePath);
            }
        }
    }

    // Upload ảnh lên server
    private void uploadImage(File file, String uid) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        RequestBody uidBody = RequestBody.create(MediaType.parse("text/plain"), uid);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = apiService.uploadAvatar(body, uidBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Lưu đường dẫn avatar vào SharedPreferences
                    String avatarUrl = "http://10.0.2.2/android_api/uploads/" + file.getName();
                    saveAvatarToSharedPreferences(avatarUrl);

                    Toast.makeText(ProfileActivity.this, "Ảnh đã cập nhật!", Toast.LENGTH_SHORT).show();
                    fetchUserInfo(); // Load lại avatar trong ProfileActivity
                } else {
                    Toast.makeText(ProfileActivity.this, "Upload thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // Hàm lưu avatar vào SharedPreferences
    private void saveAvatarToSharedPreferences(String avatarUrl) {
        SharedPreferences prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("avatar_url", avatarUrl);
        editor.apply();
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chỉnh sửa hồ sơ");

        // Tạo layout cho Dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        // Ô nhập tên hiển thị
        EditText etNewDisplayName = new EditText(this);
        etNewDisplayName.setHint("Nhập tên hiển thị");
        layout.addView(etNewDisplayName);

        // Ô nhập số điện thoại
        EditText etNewPhone = new EditText(this);
        etNewPhone.setHint("Nhập số điện thoại");
        etNewPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        layout.addView(etNewPhone);

        builder.setView(layout);

        // Nút "Lưu"
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newDisplayName = etNewDisplayName.getText().toString().trim();
            String newPhone = etNewPhone.getText().toString().trim();

            if (!newDisplayName.isEmpty() && !newPhone.isEmpty()) {
                updateProfile(newDisplayName, newPhone);
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút "Hủy"
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
    private void updateProfile(String displayName, String phone) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUser apiUser = retrofit.create(ApiUser.class);
        Call<ResponseBody> call = apiUser.updateProfile(userId, displayName, phone);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("UPDATE_PROFILE");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    fetchUserInfo();
                } else {
                    Toast.makeText(ProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUser apiUser = retrofit.create(ApiUser.class);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        apiUser.getUserInfo(userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    if ("success".equals(userResponse.getStatus())) {
                        UserResponse.User user = userResponse.getData();
                        tvDisplayName.setText(user.getDisplayName());
                        tvEmail.setText(user.getEmail());
                        tvPhone.setText(user.getPhone());

                        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                            Glide.with(ProfileActivity.this)
                                    .load(user.getAvatar())
                                    .into(ivAvatar);
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Không tìm thấy thông tin!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Lấy đường dẫn thật của file ảnh từ Uri
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    // Xử lý kết quả khi người dùng cấp quyền hoặc từ chối
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private final BroadcastReceiver profileUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fetchUserInfo(); // Cập nhật lại thông tin người dùng khi có thay đổi
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("UPDATE_PROFILE");
        LocalBroadcastManager.getInstance(this).registerReceiver(profileUpdateReceiver, filter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(profileUpdateReceiver);
    }



}
