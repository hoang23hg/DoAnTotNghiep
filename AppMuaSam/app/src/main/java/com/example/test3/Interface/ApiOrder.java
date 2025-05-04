package com.example.test3.Interface;

import com.example.test3.Model.Order;
import com.example.test3.Model.OrderRequest;
import com.example.test3.Model.OrderResponse;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiOrder {
    @POST("order.php")
    Call<OrderResponse> createOrder(@Body OrderRequest orderRequest);
    @POST("clear_cart.php")
    Call<OrderResponse> clearCart(@Body OrderRequest orderRequest);
    @GET("get_orders.php")
    Call<OrderResponse> getOrders(@Query("uid") String uid);
    @POST("update_sold_count.php")
    Call<ResponseBody> updateSoldCount(@Body HashMap<String, Object> data);

    @FormUrlEncoded
    @POST("return_order.php")
    Call<ResponseBody> returnOrder(
            @Field("order_id") int orderId
    );

}
