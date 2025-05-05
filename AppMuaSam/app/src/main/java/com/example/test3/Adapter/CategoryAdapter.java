package com.example.test3.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test3.Model.Category;
import com.example.test3.ProductActivity;
import com.example.test3.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<Category> categoryList;
    private boolean isExpanded = false;
    private final int MAX_VISIBLE_ITEMS = 4;
    private RecyclerView recyclerView;

    public CategoryAdapter(Context context, List<Category> categoryList, RecyclerView recyclerView) {
        this.context = context;
        this.categoryList = categoryList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!isExpanded && position == MAX_VISIBLE_ITEMS) {
            holder.categoryText.setText("Xem thêm");
            holder.categoryImage.setImageResource(R.drawable.ic_expand_more);
            holder.itemView.setOnClickListener(v -> {
                isExpanded = true;
                recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
                notifyDataSetChanged();
            });
        } else if (isExpanded && position == categoryList.size()) {
            holder.categoryText.setText("Thu gọn");
            holder.categoryImage.setImageResource(R.drawable.ic_expand_less);
            holder.itemView.setOnClickListener(v -> {
                isExpanded = false;
                recyclerView.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false));
                notifyDataSetChanged();
            });
        } else if (position < categoryList.size()) {
            Category category = categoryList.get(position);
            holder.categoryText.setText(category.getCategory_name());
            Glide.with(context)
                    .load(category.getImageUrl())
                    .placeholder(R.drawable.loadding)
                    .error(R.drawable.loadding)
                    .into(holder.categoryImage);

            // Bắt sự kiện khi bấm vào danh mục -> Mở ProductActivity
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("category_id", category.getId());
                intent.putExtra("category_name", category.getCategory_name());// Truyền ID danh mục
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (!isExpanded) {
            return Math.min(categoryList.size(), MAX_VISIBLE_ITEMS + 1); // Thêm 1 ô "Xem thêm"
        }
        return categoryList.size() + 1; // Thêm 1 ô "Thu gọn"
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryText;
        ImageView categoryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryText = itemView.findViewById(R.id.categoryText);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }
}
