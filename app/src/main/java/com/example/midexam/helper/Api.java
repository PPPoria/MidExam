package com.example.midexam.helper;

import com.example.midexam.model.UserData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("?")
    Call<UserData> getUserData(@Query("sort") String sort, @Query("account") String account, @Query("password") String password);
}
