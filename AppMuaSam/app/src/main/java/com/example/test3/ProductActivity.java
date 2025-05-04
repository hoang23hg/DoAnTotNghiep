package com.example.test3;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.Adapter.ProductAdapter;
import com.example.test3.Interface.ApiProduct;
import com.example.test3.Model.CategoryResponse;
import com.example.test3.Model.Product;
import com.example.test3.Retrofit.RetrofitProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ApiProduct apiProduct;
    private int categoryId;
    private String categoryName; // Biến lưu tên danh mục
    private TextView txtCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);

        recyclerView = findViewById(R.id.recyclerCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ImageView imgBackCategory = findViewById(R.id.imgBackCategory);
        txtCategoryName = findViewById(R.id.txtCategoryName); // Ánh xạ TextView

        imgBackCategory.setOnClickListener(v -> onBackPressed());

        apiProduct = RetrofitProduct.getClient().create(ApiProduct.class);


        categoryId = getIntent().getIntExtra("category_id", -1);
        categoryName = getIntent().getStringExtra("category_name");

        if (categoryName != null) {
            txtCategoryName.setText(categoryName); // Hiển thị tên danh mục
        }

        if (categoryId != -1) {
            loadProductsByCategory(categoryId);
        } else {
            Toast.makeText(this, "Không tìm thấy danh mục!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProductsByCategory(int categoryId) {
        apiProduct.getProductsByCategory(categoryId).enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Lấy tên danh mục
                    String categoryName = response.body().getCategoryName();
                    txtCategoryName.setText(categoryName); // Hiển thị tên danh mục

                    // Hiển thị danh sách sản phẩm
                    List<Product> productList = response.body().getProducts();
                    productAdapter = new ProductAdapter(ProductActivity.this, productList);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(ProductActivity.this, "Không có sản phẩm!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.e("ProductActivity", "Lỗi: " + t.getMessage());
                Toast.makeText(ProductActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
