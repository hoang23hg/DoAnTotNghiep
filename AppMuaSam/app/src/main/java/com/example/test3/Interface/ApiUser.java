package com.example.test3.Interface;
import com.example.test3.Model.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiUser {
    @FormUrlEncoded
    @POST("sync_user.php")  // API PHP trên server
    Call<ResponseBody> syncUser(
            @Field("uid") String uid,
            @Field("email") String email
    );
    @GET("get_user_info.php")
    Call<UserResponse> getUserInfo(@Query("uid") String uid);
    @FormUrlEncoded
    @POST("update_profile.php")
    Call<ResponseBody> updateProfile(
            @Field("uid") String uid,
            @Field("display_name") String displayName,
            @Field("phone") String phone
    );
}
