package com.example.moneymate.network;

import com.example.moneymate.models.User;
import com.example.moneymate.models.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("signup.php")
    Call<SignupResponse> registerUser(@Body User user);
}
