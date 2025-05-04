package com.example.test3.Interface;


import com.example.test3.Model.Size;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiSize {
    @GET("get_sizes_by_product.php")
    Call<List<Size>> getSizesByProduct(@Query("product_id") int productId);
}
