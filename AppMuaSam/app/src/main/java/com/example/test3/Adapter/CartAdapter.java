package com.example.test3.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.test3.Interface.ApiCart;
import com.example.test3.Interface.ApiDisplayCart;
import com.example.test3.Model.CartItem;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;
import com.example.test3.User.GioHangFragment;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartList;
    private ApiDisplayCart apiDisplayCart;
    private OnCartItemDeleteListener deleteListener;
    private OnCartUpdateListener onCartUpdateListener;

    public CartAdapter(Context context, List<CartItem> cartList, OnCartUpdateListener updateListener, OnCartItemDeleteListener deleteListener) {
        this.context = context;
        this.cartList = cartList;
        this.onCartUpdateListener = updateListener;
        this.deleteListener = deleteListener;  // Gán giá trị cho biến deleteListener
        this.apiDisplayCart = RetrofitProduct.getClient().create(ApiDisplayCart.class);
    }
    public List<CartItem> getCartItems() {
        return cartList;
    }



    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);
        holder.cartItemName.setText(item.getProductName());
        holder.cartItemSize.setText("Size: " + item.getSizeName());
        holder.cartItemPrice.setText("$" + item.getPrice());
        holder.cartItemTotalPrice.setText("$" + (item.getPrice() * item.getQuantity()));
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        Glide.with(context).load(item.getProductImage()).into(holder.cartItemImage);

        // Xử lý tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> updateQuantity(item, item.getQuantity() + 1));

        // Xử lý giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                updateQuantity(item, item.getQuantity() - 1);
            }
        });

        // Xử lý xóa sản phẩm khỏi giỏ hàng
        holder.btnDelete.setOnClickListener(v -> {
            Log.d("DELETE_CART", "Nhấn nút xóa, cart_id: " + item.getCartId());
            if (deleteListener != null) {
                deleteListener.onCartItemDelete(item.getCartId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // Cập nhật số lượng
    private void updateQuantity(CartItem item, int newQuantity) {
        item.setQuantity(newQuantity);
        apiDisplayCart.updateCartItem(item).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                notifyDataSetChanged();
                if (onCartUpdateListener != null) {
                    onCartUpdateListener.onCartUpdated(); // Cập nhật tổng tiền
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi cập nhật số lượng", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Xóa sản phẩm khỏi giỏ hàng
    private void deleteCartItem(int cartId, int position) {
        apiDisplayCart.deleteCartItem(cartId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cartList.remove(position);
                notifyItemRemoved(position);
                if (onCartUpdateListener != null) {
                    onCartUpdateListener.onCartUpdated(); // Cập nhật tổng tiền
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface OnCartItemDeleteListener {
        void onCartItemDelete(int cartId, int position);
    }

    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView cartItemImage, btnIncrease, btnDecrease, btnDelete;
        TextView cartItemName, cartItemSize, cartItemPrice, cartItemTotalPrice, txtQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemImage = itemView.findViewById(R.id.cartItemImage);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemSize = itemView.findViewById(R.id.cartItemSize);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            cartItemTotalPrice = itemView.findViewById(R.id.cartItemTotalPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
