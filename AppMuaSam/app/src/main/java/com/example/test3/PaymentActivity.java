package com.example.test3;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.Adapter.PaymentAdapter;
import com.example.test3.Interface.ApiAddress;
import com.example.test3.Interface.ApiOrder;
import com.example.test3.Model.Address;
import com.example.test3.Model.CartItem;
import com.example.test3.Model.OrderRequest;
import com.example.test3.Model.OrderResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {

    private List<CartItem> gioHangList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    private TextView textTotalPrice, tvUserNamePayment, tvPhoneNumberPayment, tvAddressPayment;
    private String selectedPaymentMethod = "COD";
    private int selectedAddressId = -1; // ID của địa chỉ mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ImageView imageaddress = findViewById(R.id.imgaddress);
        imageaddress.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, AddressActivity.class);
            startActivityForResult(intent, 100); // Start để chọn địa chỉ mới
        });

        Button btnThanhToan = findViewById(R.id.btnThanhToan);
        recyclerView = findViewById(R.id.recyclerViewPayment);
        textTotalPrice = findViewById(R.id.textTotalPrice);
        tvUserNamePayment = findViewById(R.id.tvUserNamePayment);
        tvPhoneNumberPayment = findViewById(R.id.tvPhoneNumberPayment);
        tvAddressPayment = findViewById(R.id.tvAddressPayment);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        textTotalPrice.setText("Tổng tiền: $" + totalPrice);

        double shippingFee = 1.0;
        double totalPayment = totalPrice + shippingFee;

        TextView textShippingFee = findViewById(R.id.textShippingFee);
        TextView textTotalPayment = findViewById(R.id.textTotalPayment);

        textTotalPrice.setText("Tổng tiền: $" + totalPrice);
        textShippingFee.setText("Phí vận chuyển: $" + shippingFee);
        textTotalPayment.setText("Tổng thanh toán: $" + totalPayment);

        RadioGroup radioGroupPayment = findViewById(R.id.radioGroupPayment);

        radioGroupPayment.setOnCheckedChangeListener((group, checkedId1) -> {
            if (checkedId1 == R.id.rbCOD) {
                selectedPaymentMethod = "COD";
            } else if (checkedId1 == R.id.rbCreditCard) {
                selectedPaymentMethod = "Credit Card";
            } else if (checkedId1 == R.id.rbMomo) {
                selectedPaymentMethod = "Momo";
            }
        });

        ImageView backPayment = findViewById(R.id.backPayment);
        gioHangList = (List<CartItem>) getIntent().getSerializableExtra("gioHang");
        if (gioHangList == null) {
            gioHangList = new ArrayList<>();
        }

        adapter = new PaymentAdapter(this, gioHangList);
        recyclerView.setAdapter(adapter);

        backPayment.setOnClickListener(view -> {
            finish();
        });


        // Gọi API lấy địa chỉ mặc định
        getDefaultAddress();

        btnThanhToan.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(PaymentActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            List<OrderRequest.ProductItem> productList = new ArrayList<>();
            for (CartItem item : gioHangList) {
                productList.add(new OrderRequest.ProductItem(item.getProductId(), item.getSizeId(), item.getQuantity(), item.getPrice()));
            }

            OrderRequest orderRequest = new OrderRequest(uid, totalPrice, selectedPaymentMethod,selectedAddressId, productList);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2/android_api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiOrder apiOrder = retrofit.create(ApiOrder.class);

            Call<OrderResponse> call = apiOrder.createOrder(orderRequest);
            call.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(PaymentActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                        for (CartItem item : gioHangList) {
                            updateSoldCount(item.getProductId(), item.getQuantity());
                        }
                        clearCart(uid);
                        adapter.notifyDataSetChanged();
                        gioHangList.clear();
                        finish();
                    } else {
                        Toast.makeText(PaymentActivity.this, "Lỗi khi đặt hàng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    Toast.makeText(PaymentActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getDefaultAddress();

    }


    private void getDefaultAddress() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiAddress api = retrofit.create(ApiAddress.class);
        Call<Address> call = api.getDefaultAddress(uid);

        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Address address = response.body();
                    selectedAddressId = address.getAddressId();
                    String fullAddress = address.getHouseNumber() + " " +
                            address.getStreet() + ", " +
                            address.getWard() + ", " +
                            address.getDistrict() + ", " +
                            address.getCity();
                    tvUserNamePayment.setText(address.getReceiverName());
                    tvPhoneNumberPayment.setText(address.getPhoneNumber());
                    tvAddressPayment.setText(fullAddress);
                } else {
                    Toast.makeText(PaymentActivity.this, "Không có địa chỉ mặc định!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearCart(String uid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiOrder apiOrder = retrofit.create(ApiOrder.class);
        Call<OrderResponse> call = apiOrder.clearCart(new OrderRequest(uid));

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("CLEAR_CART", "Giỏ hàng đã được xóa khỏi MySQL.");
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.e("CLEAR_CART", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    private void updateSoldCount(int productId, int quantity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiOrder apiOrder = retrofit.create(ApiOrder.class);

        HashMap<String, Object> data = new HashMap<>();
        data.put("product_id", productId);
        data.put("quantity", quantity);

        Call<ResponseBody> call = apiOrder.updateSoldCount(data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("UPDATE_SOLD_COUNT", "Cập nhật sold_count thành công!");
                } else {
                    Log.e("UPDATE_SOLD_COUNT", "Lỗi cập nhật sold_count!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UPDATE_SOLD_COUNT", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }



}
