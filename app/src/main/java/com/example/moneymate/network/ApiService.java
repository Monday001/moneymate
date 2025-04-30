package com.example.moneymate.network;

import com.example.moneymate.models.GenericResponse;
import com.example.moneymate.models.Lender;
import com.example.moneymate.models.LenderLoginRequest;
import com.example.moneymate.models.LoginResponse;
import com.example.moneymate.models.ResetPasswordRequest;
import com.example.moneymate.models.SendOtpRequest;
import com.example.moneymate.models.SendOtpResponse;
import com.example.moneymate.models.User;
import com.example.moneymate.models.SignupResponse;
import com.example.moneymate.models.VerifyOtpRequest;
import com.example.moneymate.models.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("signup.php")
    Call<SignupResponse> registerUser(@Body User user);

    @POST("send_otp.php")
    Call<SendOtpResponse> sendOtp(@Body SendOtpRequest request);

    @POST("verify_otp.php")
    Call<VerifyOtpResponse> verifyOtp(@Body VerifyOtpRequest request);

    @POST("reset_password.php")
    Call<GenericResponse> resetPassword(@Body ResetPasswordRequest request);

    @POST("signup.php")
    Call<SignupResponse> registerLender(@Body Lender lender);

    @POST("signin.php")
    Call<LoginResponse> authenticateLender(@Body LenderLoginRequest lender);






}
