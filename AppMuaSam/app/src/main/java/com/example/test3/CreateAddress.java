package com.example.test3;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test3.Interface.ApiAddress;
import com.google.firebase.auth.FirebaseAuth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAddress extends AppCompatActivity {
    EditText edtReceiverName, edtPhoneNumber, edtHouseNumber, edtStreet, edtWard, edtDistrict, edtCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_address);
        edtReceiverName = findViewById(R.id.edtReceiverName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtHouseNumber = findViewById(R.id.edtHouseNumber);
        edtStreet = findViewById(R.id.edtStreet);
        edtWard = findViewById(R.id.edtWard);
        edtDistrict = findViewById(R.id.edtDistrict);
        edtCity = findViewById(R.id.edtCity);

        ImageView btnBackaddress = findViewById(R.id.btnBackaddress);
        btnBackaddress.setOnClickListener(v -> {
            finish();
        });
        Button btnComplete = findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String receiverName = edtReceiverName.getText().toString().trim();
            String phoneNumber = edtPhoneNumber.getText().toString().trim();
            String houseNumber = edtHouseNumber.getText().toString().trim();
            String street = edtStreet.getText().toString().trim();
            String ward = edtWard.getText().toString().trim();
            String district = edtDistrict.getText().toString().trim();
            String city = edtCity.getText().toString().trim();

            if (receiverName.isEmpty() || phoneNumber.isEmpty() || houseNumber.isEmpty() || street.isEmpty() ||
                    ward.isEmpty() || district.isEmpty() || city.isEmpty()) {
                Toast.makeText(CreateAddress.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            addNewAddress(uid, receiverName, phoneNumber, houseNumber, street, ward, district, city, true);
        });

    }
    private void addNewAddress(String uid, String receiverName, String phoneNumber,
                               String houseNumber, String street, String ward,
                               String district, String city, boolean isDefault) {
        Log.d("ADD_ADDRESS", "Sending Data: " + uid + ", " + receiverName + ", " + phoneNumber
                + ", " + houseNumber + ", " + street + ", " + ward + ", " + district + ", " + city + ", " + isDefault);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiAddress apiAddress = retrofit.create(ApiAddress.class);
        Call<ResponseBody> call = apiAddress.addAddress(uid, receiverName, phoneNumber,
                houseNumber, street, ward, district, city, isDefault);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateAddress.this, "Đã lưu địa chỉ!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn trước
                } else {
                    Toast.makeText(CreateAddress.this, "Lỗi khi lưu địa chỉ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateAddress.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}