package com.example.test3.Interface;

import com.example.test3.Model.CategoryResponse;
import com.example.test3.Model.Product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

public interface ApiProduct {
    @GET("get_products.php")
    Call<Map<String, List<Product>>> getProducts();
    @GET("get_products_by_category.php")
    Call<CategoryResponse> getProductsByCategory(@Query("category_id") int categoryId);
    @GET("search_products.php")
    Call<List<Product>> searchProducts(@Query("query") String query);
}

