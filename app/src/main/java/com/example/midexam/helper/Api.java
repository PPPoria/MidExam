package com.example.midexam.helper;

import com.example.midexam.model.UserData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    @FormUrlEncoded
    @POST("user/log")
    Call<UserData> log(@Field("account") String account,
                       @Field("password") String password);

    @FormUrlEncoded
    @POST("user/register")
    Call<UserData> register(@Field("account") String account,
                            @Field("password") String password);


    @POST("user/updateData")
    Call<UserData> updateData(@Header("token") String token,
                              @Body UserData body);

    @Multipart
    @POST("user/image")
    Call<UserData> updateImage(@Header("token") String token,@Part("account") RequestBody body, @Part MultipartBody.Part head, @Part MultipartBody.Part background);
}
