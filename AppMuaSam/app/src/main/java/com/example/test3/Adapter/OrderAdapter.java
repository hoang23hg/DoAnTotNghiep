package com.example.test3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.DanhGiaDialogFragment;
import com.example.test3.Interface.ApiOrder;
import com.example.test3.Model.Order;
import com.example.test3.OrderDetailsActivity;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orderList;
    private Context context;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtOrderId.setText("Mã đơn hàng: " + order.getOrder_id());
        holder.txtReceiver.setText("Tên: " + order.getReceiver_name());
        holder.txtPhone.setText("SĐT: " + order.getPhone_number());
        holder.txtAddress.setText("Địa chỉ: " + order.getFull_address());
        holder.txtDate.setText("Ngày: " + order.getOrder_date());
        holder.txtTotal.setText("Tổng: $" + order.getTotal_price());
        holder.txtOrderStatus.setText("Trạng thái: " + order.getStatus());

        holder.btnChiTiet.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailsActivity.class);
            intent.putExtra("order_id", order.getOrder_id());  // truyền id đơn hàng
            context.startActivity(intent);
        });
        holder.btnHoanHang.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc muốn hoàn hàng sản phẩm này không?")
                    .setPositiveButton("Hoàn hàng", (dialog, which) -> {
                        requestReturnOrder(order.getOrder_id(), holder.getAdapterPosition());
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtReceiver, txtPhone, txtAddress, txtDate, txtTotal,txtOrderStatus;
        Button  btnChiTiet, btnHoanHang;
        public ViewHolder(View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtReceiver = itemView.findViewById(R.id.txtReceiver);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            btnChiTiet = itemView.findViewById(R.id.btnChiTiet);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            btnHoanHang = itemView.findViewById(R.id.btnHoanHang);
        }
    }
    private void requestReturnOrder(int orderId, int position) {
        ApiOrder apiOrder = RetrofitProduct.getClient().create(ApiOrder.class);

        apiOrder.returnOrder(orderId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Hoàn hàng thành công!", Toast.LENGTH_SHORT).show();

                    orderList.get(position).setStatus("Returned"); // nếu có field "status"
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(context, "Lỗi khi hoàn hàng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

