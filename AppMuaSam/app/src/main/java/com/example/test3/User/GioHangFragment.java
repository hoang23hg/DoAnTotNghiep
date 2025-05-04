package com.example.test3.User;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test3.Adapter.CartAdapter;
import com.example.test3.Interface.ApiAddress;
import com.example.test3.Interface.ApiDelCart;
import com.example.test3.Interface.ApiDisplayCart;
import com.example.test3.Model.ApiResponseCart;
import com.example.test3.Model.CartItem;
import com.example.test3.PaymentActivity;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ApiDisplayCart apiDisplayCart;
    private ApiAddress apiAddress;
    private TextView textTotalPrice;
    private double totalPrice = 0;
    private Button btnThanhToan;
    private String uid;
    private List<CartItem> gioHangList = new ArrayList<>();

    public GioHangFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gio_hang, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        textTotalPrice = view.findViewById(R.id.textTotalPrice);
        apiDisplayCart = RetrofitProduct.getClient().create(ApiDisplayCart.class);
        apiAddress = RetrofitProduct.getClient().create(ApiAddress.class);

        // Lấy uid từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

        loadCart();

        btnThanhToan = view.findViewById(R.id.btnThanhToan);
        btnThanhToan.setOnClickListener(v -> {
            if (gioHangList.isEmpty()) {
                Toast.makeText(getContext(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getContext(), PaymentActivity.class);
            intent.putExtra("gioHang", (Serializable) gioHangList);
            intent.putExtra("totalPrice", totalPrice);
            startActivity(intent);
        });


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        loadCart();

    }

    private void loadCart() {
        if (uid.isEmpty()) {
            Toast.makeText(getContext(), "Không tìm thấy UID", Toast.LENGTH_SHORT).show();
            return;
        }

        apiDisplayCart.getCartItems(uid).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CartItem> cartItems = response.body();
                    totalPrice = calculateTotal(cartItems);
                    textTotalPrice.setText("Tổng: $" + totalPrice);

                    if (!cartItems.isEmpty()) {
                        gioHangList.clear();  // Xóa dữ liệu cũ
                        gioHangList.addAll(cartItems);  // Cập nhật danh sách giỏ hàng

                        cartAdapter = new CartAdapter(getContext(), gioHangList,
                                () -> {
                                    totalPrice = calculateTotal(cartAdapter.getCartItems());
                                    textTotalPrice.setText("Tổng: $" + totalPrice);
                                },
                                (cartId, position) -> {
                                    deleteCartItem(cartId, position); // Gọi hàm xóa sản phẩm
                                }
                        );
                        recyclerView.setAdapter(cartAdapter);



                    } else {
                        Toast.makeText(getContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_RESPONSE", "Lỗi response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Log.e("API_RESPONSE", "Lỗi kết nối API: " + t.getMessage());
            }
        });
    }

    private double calculateTotal(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }


    private void deleteCartItem(int cartId, int position) {
        ApiDelCart apiDelCart = RetrofitProduct.getClient().create(ApiDelCart.class);

        Call<ApiResponseCart> call = apiDelCart.deleteCartItem(cartId);
        call.enqueue(new Callback<ApiResponseCart>() {
            @Override
            public void onResponse(Call<ApiResponseCart> call, Response<ApiResponseCart> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponseCart apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Log.d("DELETE_CART", "Xóa thành công cart_id: " + cartId);
                        gioHangList.remove(position);
                        cartAdapter.notifyItemRemoved(position);
                        updateTotalPrice();
                        Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("DELETE_CART", "Lỗi: " + apiResponse.getMessage());
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("DELETE_CART", "Lỗi server: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseCart> call, Throwable t) {
                Log.e("DELETE_CART", "Lỗi kết nối API: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice() {
        totalPrice = calculateTotal(gioHangList);
        textTotalPrice.setText("Tổng: $" + totalPrice);
    }




}
