package com.example.test3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test3.Model.CartItem;
import com.example.test3.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private Context context;
    private List<CartItem> cartItemList;

    public PaymentAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        holder.textProductName.setText(item.getProductName());
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
        holder.txtPrice.setText(String.format("%.2f $", item.getPrice()));
        holder.txtTotalPrice.setText(String.format("Total: %.2f $", item.getPrice() * item.getQuantity()));
        holder.txtSizePayment.setText("Size: " + item.getSizeName());

        // Load ảnh sản phẩm bằng Glide
        Glide.with(context)
                .load(item.getProductImage()) // URL ảnh từ MySQL
                .placeholder(R.drawable.anh1) // Ảnh mặc định
                .into(holder.imageProduct);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textProductName, txtQuantity, txtPrice, txtTotalPrice,txtSizePayment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtSizePayment = itemView.findViewById(R.id.txtSizePayment);
        }
    }
}

