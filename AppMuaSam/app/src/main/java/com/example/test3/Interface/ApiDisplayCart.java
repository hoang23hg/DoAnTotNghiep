package com.example.test3.Interface;

import com.example.test3.Model.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiDisplayCart {
    @GET("get_cart.php")
    Call<List<CartItem>> getCartItems(@Query("uid") String uid);

    @PUT("cart/update")
    Call<Void> updateCartItem(@Body CartItem cartItem);

    @DELETE("cart/delete/{cart_id}")
    Call<Void> deleteCartItem(@Path("cart_id") int cart_id);
}
