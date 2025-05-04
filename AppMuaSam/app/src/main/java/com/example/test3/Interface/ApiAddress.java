package com.example.test3.Interface;

import com.example.test3.Model.Address;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiAddress {
    @GET("get_addresses.php")
    Call<List<Address>> getAddresses(@Query("uid") String uid);
    @GET("check_address.php")
    Call<ResponseBody> checkAddress(@Query("uid") String uid);

    @FormUrlEncoded
    @POST("add_address.php")
    Call<ResponseBody> addAddress(
            @Field("uid") String uid,
            @Field("receiver_name") String receiverName,
            @Field("phone_number") String phoneNumber,
            @Field("houseNumber") String houseNumber,
            @Field("street") String street,
            @Field("ward") String ward,
            @Field("district") String district,
            @Field("city") String city,
            @Field("is_default") boolean isDefault
    );
    @FormUrlEncoded
    @POST("delete_address.php") // Đường dẫn API PHP
    Call<ResponseBody> deleteAddress(
            @Field("address_id") int address_id
    );
    @FormUrlEncoded
    @POST("setDefaultAddress.php")
    Call<ResponseBody> setDefaultAddress(@Field("address_id") int address_id);

    @GET("get_default_address.php")
    Call<Address> getDefaultAddress(@Query("uid") String uid);
    @FormUrlEncoded
    @POST("update_address.php")
    Call<ResponseBody> updateAddress(
            @Field("address_id") int addressId,
            @Field("receiver_name") String receiverName,
            @Field("phone_number") String phoneNumber,
            @Field("houseNumber") String houseNumber,
            @Field("street") String street,
            @Field("ward") String ward,
            @Field("district") String district,
            @Field("city") String city
    );


}
