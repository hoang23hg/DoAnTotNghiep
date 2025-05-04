package com.example.test3.Interface;

import com.example.test3.Model.ApiResponseCart;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiDelCart {
    @FormUrlEncoded
    @POST("delete_cart_item.php")  // Đường dẫn API
    Call<ApiResponseCart> deleteCartItem(
            @Field("cart_id") int cartId
    );
}
