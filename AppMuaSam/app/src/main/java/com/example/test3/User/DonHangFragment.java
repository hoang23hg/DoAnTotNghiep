package com.example.test3.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.Adapter.OrderAdapter;
import com.example.test3.AddressActivity;
import com.example.test3.DanhGiaDialogFragment;
import com.example.test3.Interface.ApiOrder;
import com.example.test3.Model.Order;
import com.example.test3.Model.OrderResponse;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonHangFragment extends Fragment {
    private RecyclerView recyclerOrders;
    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_don_hang, container, false);
        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchOrders();
        return view;
    }

    private void fetchOrders() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (uid == null) {
            Toast.makeText(getContext(), "Lỗi: Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = RetrofitProduct.getClient();
        ApiOrder apiOrder = retrofit.create(ApiOrder.class);

        apiOrder.getOrders(uid).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body().getOrders();

                    if (orders.isEmpty()) {
                        Toast.makeText(getContext(), "Không có đơn hàng nào!", Toast.LENGTH_SHORT).show();
                    } else {
                        orderAdapter = new OrderAdapter(getContext(), orders);
                        recyclerOrders.setAdapter(orderAdapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi lấy đơn hàng! Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

}
