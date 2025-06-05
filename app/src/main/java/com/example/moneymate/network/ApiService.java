package com.example.moneymate.network;

import com.example.moneymate.models.BorrowerLoanResponse;
import com.example.moneymate.models.GenericResponse;
import com.example.moneymate.models.LatestLoanResponse;
import com.example.moneymate.models.Lender;
import com.example.moneymate.models.LenderHistoryResponse;
import com.example.moneymate.models.LenderIdResponse;
import com.example.moneymate.models.LenderLoginRequest;
import com.example.moneymate.models.LenderTermsRequest;
import com.example.moneymate.models.Loan;
import com.example.moneymate.models.LoanApplicationResponse;
import com.example.moneymate.models.LoanResponse;
import com.example.moneymate.models.LoanStatusResponse;
import com.example.moneymate.models.LoginRequest;
import com.example.moneymate.models.LoginResponse;
import com.example.moneymate.models.Notification;
import com.example.moneymate.models.P2PResponse;
import com.example.moneymate.models.PhoneResponse;
import com.example.moneymate.models.Repayment;
import com.example.moneymate.models.RepaymentResponse;
import com.example.moneymate.models.ResetPasswordRequest;
import com.example.moneymate.models.SendOtpRequest;
import com.example.moneymate.models.SendOtpResponse;
import com.example.moneymate.models.User;
import com.example.moneymate.models.SignupResponse;
import com.example.moneymate.models.VerifyOtpRequest;
import com.example.moneymate.models.VerifyOtpResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @POST("signup.php")
    Call<SignupResponse> registerUser(@Body User user);

    @POST("send_otp.php")
    Call<SendOtpResponse> sendOtp(@Body SendOtpRequest request);

    @POST("verify_otp.php")
    Call<VerifyOtpResponse> verifyOtp(@Body VerifyOtpRequest request);

    @POST("resetpassword.php")
    Call<GenericResponse> resetPassword(@Body ResetPasswordRequest request);

    @POST("signup.php")
    Call<SignupResponse> registerLender(@Body Lender lender);

    @POST("signin.php")
    @Headers("Content-Type: application/json")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("submitTerms.php")
    Call<ResponseBody> submitTerms(@Body LenderTermsRequest terms);

    @Multipart
    @POST("apply_loan.php")
    Call<GenericResponse> applyForLoan(
            @Part("loan_details") RequestBody loanDetails,
            @Part MultipartBody.Part idFront,
            @Part MultipartBody.Part idBack
    );

    @GET("get_loan_applications.php")
    Call<LoanResponse> getLoanApplications();

    @GET("repayment.php") // Adjust filename if needed
    Call<RepaymentResponse> getPayments(@Query("loan_id") int loanId);

    @POST("repay.php")
    @Headers("Content-Type: application/json")
    Call<GenericResponse> submitRepayment(@Body Repayment request);

    @GET("get_loans.php")
    Call<List<Loan>> getLoans(@Query("borrower_id") int borrowerId);

    @GET("get_repayment_details.php")
    Call<RepaymentResponse> getRepayments(@Query("loan_id") int loanId); // Returns RepaymentResponse, not a list

    @Headers("Content-Type: application/json")
    @GET("getP2PData.php")
    Call<P2PResponse> getP2PData(@Query("lender_id") int lenderId);

    @GET("getP2PData.php")
    Call<P2PResponse> getLendersWithTerms(); // Reusing P2PResponse

    @GET("get_lender_id.php")
    Call<LenderIdResponse> getLenderIdByEmail(@Query("email") String email);

    @GET("getLoanStatus.php")
    Call<LoanStatusResponse> getLoanStatus(@Query("lender_id") int lenderId);

    @GET("getLenderHistory.php")
    Call<LenderHistoryResponse> getLenderHistory(@Query("lender_id") int lenderId);

        @GET("get_loan_applications.php")
        Call<LoanResponse> getLoanApplications(@Query("lender_id") int lenderId);

        @GET("get_borrower_loans.php")
        Call<BorrowerLoanResponse> getBorrowerLoans(@Query("borrower_id") int borrowerId);


    @FormUrlEncoded
    @POST("disburse_loan.php")
    Call<ResponseBody> disburseLoan(
            @Field("loan_id") int loanId,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("deny_loan.php")
    Call<ResponseBody> denyLoan(
            @Field("loan_id") int loanId,
            @Field("reason") String reason
    );

    @GET("get_notifications.php")
    Call<List<Notification>> getNotifications(@Query("borrower_id") int borrowerId);


    @GET("getLatestDisbursedLoan.php")
    Call<LatestLoanResponse> getLatestDisbursedLoan(@Query("phone_number") String phoneNumber);

    @GET("get_borrower_phone.php")
    Call<PhoneResponse> getBorrowerPhone(@Query("borrower_id") int borrowerId);

    @FormUrlEncoded
    @POST("repayment.php")
    Call<Void> makeRepayment(
            @Field("loan_id") int loanId,
            @Field("borrower_id") int borrowerId,
            @Field("amount_paid") double amountPaid
    );



}


