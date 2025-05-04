package com.example.test3.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test3.Interface.ApiFavorite;
import com.example.test3.Interface.FavoriteListener;
import com.example.test3.Model.Favorite;
import com.example.test3.Model.FavoriteResponse;
import com.example.test3.Model.Product;
import com.example.test3.ProductDetailsActivity;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;
import com.example.test3.User.YeuThichFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private List<Favorite> favoriteList;
    private FavoriteListener listener; // Thêm biến Listener

    public FavoriteAdapter(Context context, List<Favorite> favoriteList, FavoriteListener listener) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Favorite favorite = favoriteList.get(position);
        holder.tvProductName.setText(favorite.getName());
        holder.tvProductPrice.setText("$" + favorite.getPrice());

        // Load ảnh bằng Glide
        Glide.with(context)
                .load(favorite.getImageUrl())
                .placeholder(R.drawable.anh1)
                .into(holder.imgProduct);

        // Xử lý sự kiện xóa yêu thích
        holder.btnRemoveFavorite.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            int productId = favoriteList.get(position).getProductId();

            ApiFavorite apiFavorite = RetrofitProduct.getClient().create(ApiFavorite.class);
            Call<ResponseBody> call = apiFavorite.removeFavorite(uid, productId);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        favoriteList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        });
        holder.favAddCategory.setOnClickListener(v -> {
            if (favorite.getProductId() == 0) {
                Toast.makeText(context, "Lỗi: productId không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailsActivity.class);
            intent.putExtra("product_id", favorite.getProductId());
            intent.putExtra("product_name", favorite.getName());
            intent.putExtra("product_price", favorite.getPrice());
            intent.putExtra("product_image", favorite.getImageUrl());
            intent.putExtra("product_description", favorite.getDescription());
            context.startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnRemoveFavorite,favAddCategory;
        TextView tvProductName, tvProductPrice;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnRemoveFavorite = itemView.findViewById(R.id.btnRemoveFavorite);
            favAddCategory = itemView.findViewById(R.id.fav_add_category);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}

