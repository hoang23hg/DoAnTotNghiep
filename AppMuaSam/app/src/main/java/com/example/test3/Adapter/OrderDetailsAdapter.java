package com.example.test3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test3.DanhGiaDialogFragment;
import com.example.test3.Interface.ApiOrder;
import com.example.test3.Model.OrderDetail;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailViewHolder> {
    private List<OrderDetail> orderDetails;
    private Context context;

    public OrderDetailsAdapter(List<OrderDetail> orderDetails, Context context) {
        this.orderDetails = orderDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_details, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        holder.textProductName.setText(detail.getProductName());
        holder.txtQuantity.setText(String.valueOf(detail.getQuantity()));
        holder.txtPrice.setText(String.format("%.2f $", detail.getPrice()));
        holder.txtTotalPrice.setText(String.format("Total: %.2f $", detail.getPrice() * detail.getQuantity()));
        holder.txtSizePayment.setText("Size: " + detail.getSizeName());

        Glide.with(context)
                .load(detail.getImageUrl()) // URL ảnh từ MySQL
                .placeholder(R.drawable.anh1) // Ảnh mặc định
                .into(holder.imageProduct);
        holder.btnDanhGia.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();
            DanhGiaDialogFragment dialog = new DanhGiaDialogFragment(detail.getProduct_id(), uid);
            dialog.show(activity.getSupportFragmentManager(), "DanhGiaDialog");
        });

    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textProductName, txtQuantity, txtPrice, txtTotalPrice,txtSizePayment;
        Button btnDanhGia;
        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtSizePayment = itemView.findViewById(R.id.txtSizePayment);
            btnDanhGia = itemView.findViewById(R.id.btnDanhGia);

        }
    }
}
