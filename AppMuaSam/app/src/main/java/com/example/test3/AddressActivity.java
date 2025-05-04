package com.example.test3;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.Adapter.AddressAdapter;
import com.example.test3.Interface.ApiAddress;
import com.example.test3.Model.Address;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class AddressActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton btnAddAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewAddress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(v -> {
            Intent intent = new Intent(AddressActivity.this, CreateAddress.class);
            startActivity(intent);
        });


        loadAddresses();

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadAddresses();  // Gọi API để lấy danh sách mới
    }

    private void loadAddresses() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(AddressActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")  // Thay bằng API của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiAddress apiAddress = retrofit.create(ApiAddress.class);
        Call<List<Address>> call = apiAddress.getAddresses(uid);

        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AddressAdapter adapter = new AddressAdapter(AddressActivity.this, response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(AddressActivity.this, "Không có địa chỉ nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
                Toast.makeText(AddressActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }






}

