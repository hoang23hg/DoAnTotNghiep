package com.example.test3.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.POST;

public interface ApiCart {
    @FormUrlEncoded
    @POST("addToCart.php")
    Call<ResponseBody> addToCart(
            @Field("uid") String uid,
            @Field("product_id") int productId,
            @Field("quantity") int quantity,
            @Field("size_id") int sizeId,
            @Field("price") double price
    );

}

