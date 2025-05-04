package com.example.test3.Interface;

import com.example.test3.Model.Category;
import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;
import java.util.Map;

public interface ApiCategory {
    @GET("get_categories.php")
    Call<Map<String, List<Category>>> getCategories();
}

