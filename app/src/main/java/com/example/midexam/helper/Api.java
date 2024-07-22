package com.example.midexam.helper;

import com.example.midexam.model.UserData;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    @FormUrlEncoded
    @POST("user")
    Call<UserData> getUserData(@Field("sort") String sort,
                               @Field("account") String account,
                               @Field("password") String password);

    @FormUrlEncoded
    @Multipart
    @POST("user")
    Call<UserData> updateUserData(@Field("sort") String sort,
                                  @Field("account") String account,
                                  @Field("password") String password,
                                  @Body UserData userData);

    @FormUrlEncoded
    @Multipart
    @POST("user")
    Call<UserData> updateUserImage(@Field("sort") String sort,
                                   @Field("account") String account,
                                   @Field("password") String password,
                                   @Part List<MultipartBody.Part> images);

    @FormUrlEncoded
    @POST("user")
    Call<UserData> trackUserData(@Field("sort") String sort,
                                 @Field("account") String account,
                                 @Field("password") String password,
                                 @Body UserData userData);
}
