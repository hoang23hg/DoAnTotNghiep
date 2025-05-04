package com.example.test3.Interface;

import com.example.test3.Model.Favorite;
import com.example.test3.Model.FavoriteResponse;
import com.example.test3.Model.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiFavorite {
    @FormUrlEncoded
    @POST("add_to_favorites.php")
    Call<ResponseBody> addToFavorites(
            @Field("uid") String uid,
            @Field("product_id") int productId
    );

    @FormUrlEncoded
    @POST("remove_from_favorites.php")
    Call<ResponseBody> removeFavorite(
            @Field("uid") String uid,
            @Field("product_id") int productId
    );

    @GET("get_favorites.php")
    Call<List<Favorite>> getFavoriteList(@Query("uid") String uid);
}
