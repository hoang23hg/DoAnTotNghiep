package com.example.test3;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.Adapter.OrderDetailsAdapter;
import com.example.test3.Interface.ApiService;
import com.example.test3.Model.OrderDetail;
import com.example.test3.Retrofit.RetrofitProduct;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    ImageView back;
    RecyclerView recyclerView;
    OrderDetailsAdapter adapter;
    List<OrderDetail> orderDetailList = new ArrayList<>();
    int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        back = findViewById(R.id.backOrderDetails);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
        recyclerView = findViewById(R.id.recyclerViewOrderDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderDetailsAdapter(orderDetailList, this);
        recyclerView.setAdapter(adapter);

        // Nhận order_id từ Intent
        orderId = getIntent().getIntExtra("order_id", -1);

        if (orderId != -1) {
            getOrderDetails(orderId);
        } else {
            Toast.makeText(this, "Order ID không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrderDetails(int orderId) {
        ApiService apiService = RetrofitProduct.getClient().create(ApiService.class);

        Call<List<OrderDetail>> call = apiService.getOrderDetails(orderId);
        call.enqueue(new Callback<List<OrderDetail>>() {
            @Override
            public void onResponse(Call<List<OrderDetail>> call, Response<List<OrderDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderDetailList.clear();
                    orderDetailList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(OrderDetailsActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderDetail>> call, Throwable t) {
                Toast.makeText(OrderDetailsActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
