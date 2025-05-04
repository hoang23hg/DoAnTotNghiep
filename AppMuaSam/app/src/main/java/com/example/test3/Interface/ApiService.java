package com.example.test3.Interface;

import com.example.test3.Model.DescriptionResponse;
import com.example.test3.Model.OrderDetail;
import com.example.test3.Model.ReviewResponse;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface  ApiService {
    @FormUrlEncoded
    @POST("add_to_order.php")
    Call<ResponseBody> addToOrder(
            @Field("user_id") String userId,  // Chuyển từ int -> String
            @Field("product_id") int productId,
            @Field("quantity") int quantity,
            @Field("size") String size,
            @Field("price") double price
    );
    @POST("update_sold_count.php")
    Call<ResponseBody> updateSoldCount(@Body HashMap<String, Object> data);
    @Multipart
    @POST("upload_avatar.php")
    Call<ResponseBody> uploadAvatar(
            @Part MultipartBody.Part image,
            @Part("uid") RequestBody uid
    );
    @FormUrlEncoded
    @POST("add_review.php")
    Call<ResponseBody> submitReview(
            @Field("product_id") int productId,
            @Field("uid") String uid,
            @Field("rating") float rating,
            @Field("comment") String comment
    );

    @GET("getOrderDetails.php")
    Call<List<OrderDetail>> getOrderDetails(@Query("orderId") int orderId);

    @GET("get_description.php")
    Call<DescriptionResponse> getDescription(@Query("product_id") int productId);

    @GET("get_reviews.php")
    Call<ReviewResponse> getReviews(@Query("product_id") int productId);
    @GET("get_average_rating.php")
    Call<JsonObject> getAverageRating(@Query("product_id") int productId);

}
