package com.mike4christ.sti_mobile.retrofit_interface;



import com.mike4christ.sti_mobile.Model.AllRisk.AllRiskPost.AllRiskPostHead;
import com.mike4christ.sti_mobile.Model.AllRisk.FormSuccessDetail.BuyQuoteFormGetHead_AllRisk;
import com.mike4christ.sti_mobile.Model.AllRisk.QouteHeadAllrisk;
import com.mike4christ.sti_mobile.Model.Auth.UserDataHead;
import com.mike4christ.sti_mobile.Model.Auth.ChangePassPost;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.UserGetObj;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.UserPostObj;
import com.mike4christ.sti_mobile.Model.Auth.RegisterObj;
import com.mike4christ.sti_mobile.Model.Claim.ClaimPost;
import com.mike4christ.sti_mobile.Model.Claim.MyClaims.ClaimHead;
import com.mike4christ.sti_mobile.Model.Claim.TrackClaim.GetClaimStatus;
import com.mike4christ.sti_mobile.Model.Etic.EticPost.EticPostHead;
import com.mike4christ.sti_mobile.Model.Etic.FormSuccessDetail.BuyQuoteFormGetHead_Etic;
import com.mike4christ.sti_mobile.Model.Etic.QouteHeadEtic;
import com.mike4christ.sti_mobile.Model.ForgetPass.UserHName;
import com.mike4christ.sti_mobile.Model.ForgetPass.UserHNew;
import com.mike4christ.sti_mobile.Model.ForgetPass.UserHead;
import com.mike4christ.sti_mobile.Model.Marine.FormSuccessDetail.BuyQuoteFormGetHead_Marine;
import com.mike4christ.sti_mobile.Model.Marine.MarinePost.MarinePostHead;
import com.mike4christ.sti_mobile.Model.Marine.QouteHeadMarine;
import com.mike4christ.sti_mobile.Model.MyPolicies.PolicyHead;
import com.mike4christ.sti_mobile.Model.Pin.changePin;
import com.mike4christ.sti_mobile.Model.Pin.setPin;
import com.mike4christ.sti_mobile.Model.ProfileUpdate.UserEditHead;
import com.mike4christ.sti_mobile.Model.ProfileUpdate.UserGetUpdateHead;
import com.mike4christ.sti_mobile.Model.RenewPolicyGet;
import com.mike4christ.sti_mobile.Model.Swiss.FormSuccessDetail.BuyQuoteFormGetHead_Swiss;
import com.mike4christ.sti_mobile.Model.Swiss.QouteHeadSwiss;
import com.mike4christ.sti_mobile.Model.Swiss.SwissPost.SwissPostHead;
import com.mike4christ.sti_mobile.Model.TransactionHistroy.TransactionHead;
import com.mike4christ.sti_mobile.Model.Vehicle.BrandType.VehicleBrandType;
import com.mike4christ.sti_mobile.Model.Vehicle.FormSuccessDetail.BuyQuoteFormGetHead;
import com.mike4christ.sti_mobile.Model.Vehicle.Quote.PostVehicleData;
import com.mike4christ.sti_mobile.Model.Vehicle.Quote.QouteHead;
import com.mike4christ.sti_mobile.Model.Vehicle.VehicleBrand.Vehicles_Brand;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePost.VehiclePostHead;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ApiInterface {
    @POST("users")
    Call<UserDataHead> register(@Body RegisterObj regPostData, @HeaderMap HashMap<String, String> headerMap);
    @POST("users/login")
    Call<UserGetObj> login(@Body UserPostObj userPostObj);

    @GET("vehicle-brands")
    Call<Vehicles_Brand> vehicle_brand();

    @GET("vehicle-brand-types/{brand_id}")
    Call<VehicleBrandType> brand_type(@Path("brand_id") int brand_id);

    @POST("change-password")
    Call<ResponseBody> change_password(@Header("Authorization") String token, @Body ChangePassPost changePassPost);

    //Buy Vehicle Policy
    @POST("buy-vehicle-policy")
    Call<BuyQuoteFormGetHead> vehicle_policy(@Header("Authorization") String token, @Body VehiclePostHead vehiclePostHead);

    //Vehicle Quote
    @POST("get-vehicle-quote")
    Call<QouteHead> vehicle_quote(@Header("Authorization") String token, @Body PostVehicleData postVehicleData);
    //AllRisk Quote
    @FormUrlEncoded
    @POST("get-all-risk-quote")
    Call<QouteHeadAllrisk> allrisk_quote(@Header("Authorization") String token, @Field("sum_insured") String sum_insrured);

    //Travel Quote
    @FormUrlEncoded
    @POST("get-travel-quote")
    Call<QouteHeadEtic> etic_quote(@Header("Authorization") String token, @Field("sum_insured") long sum_insured);

    //Swiss Quote
    @FormUrlEncoded
    @POST("get-swiss-quote")
    Call<QouteHeadSwiss> swiss_quote(@Header("Authorization") String token, @Field("date_of_birth") String date_of_birth);

    //Marine Quote
    @FormUrlEncoded
    @POST("get-marine-quote")
    Call<QouteHeadMarine> marine_quote(@Header("Authorization") String token, @Field("value") String value,
                                       @Field("conversion_rate") String conversion_rate);

    //Buy AllRisk Policy
    @POST("buy-all-risk-policy")
    Call<BuyQuoteFormGetHead_AllRisk> allrisk_policy(@Header("Authorization") String token, @Body AllRiskPostHead allRiskPostHead);
    //Buy AllRisk Policy
    @POST("buy-travel-policy")
    Call<BuyQuoteFormGetHead_Etic> etic_policy(@Header("Authorization") String token, @Body EticPostHead eticPostHead);

    //Buy AllRisk Policy
    @POST("buy-marine-policy")
    Call<BuyQuoteFormGetHead_Marine> marine_policy(@Header("Authorization") String token, @Body MarinePostHead marinePostHead);

    //Buy Swiss Policy
    @POST("buy-swiss-policy")
    Call<BuyQuoteFormGetHead_Swiss> swiss_policy(@Header("Authorization") String token, @Body SwissPostHead swissPostHead);

    //make claim
    @POST("make-claim")
    Call<ResponseBody> claim(@Header("Authorization") String token, @Body ClaimPost claimPost);

    //get transaction history
    @GET("transaction-history")
    Call<TransactionHead> transaction_hist(@Header("Authorization") String token);

    //Get Paid Policies
    @GET("policies")
    Call<PolicyHead> get_paid_policies(@Header("Authorization") String token);

    //Get Paid Policies
    @GET("my-claims")
    Call<ClaimHead> get_my_claims(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("track-claim")
    Call<GetClaimStatus> track_claim(@Header("Authorization") String token, @Field("claim_number") String claim_number);


    @FormUrlEncoded
    @POST("renew-vehicle-policy")
    Call<ResponseBody> renew_vehicle_policy(@Header("Authorization") String token, @Field("policy_number") String policy_number,
                                            @Field("payment_amount") String payment_amount,
                                            @Field("payment_mode") String payment_mode);


    @POST("set-pin")
    Call<ResponseBody> set_pin(@Header("Authorization") String token, @Body setPin set_pin);

    @POST("change-pin")
    Call<ResponseBody> change_pin(@Header("Authorization") String token, @Body changePin change_pin);

    @FormUrlEncoded
    @PUT("user")
    Call<UserGetUpdateHead> update_profile(@Header("Authorization") String token, @Body UserEditHead userEditHead);

    @POST("users/initiate-password")
    Call<UserHName> initiate_forget_pass(@Body UserHead userHead);


    @POST("users/update-password")
    Call<ResponseBody> reset_pass(@Body UserHNew userHNew);

    @FormUrlEncoded
    @POST("update-transaction")
    Call<ResponseBody> update_payment(@Header("Authorization") String token, @Field("reference") String reference,
                                      @Field("provider_reference") String provider_reference,
                                      @Field("status") String status,
                                      @Field("policy_type") String policy_type);


    @FormUrlEncoded
    @POST("renew-vehicle-policy")
    Call<RenewPolicyGet> renew_policy(@Header("Authorization") String token, @Field("policy_number") String policy_number,
                                      @Field("payment_amount") String payment_amount,
                                      @Field("payment_mode") String payment_mode);


  /*  {
        "policy_number":"2010/12/MB/006",
            "payment_amount":"5000",
            "payment_mode":"paystack"
    }*/






}
