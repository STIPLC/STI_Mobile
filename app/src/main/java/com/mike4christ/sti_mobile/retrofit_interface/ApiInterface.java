package com.mike4christ.sti_mobile.retrofit_interface;



import com.mike4christ.sti_mobile.Model.AllRisk.AllRiskPost.AllRiskPostHead;
import com.mike4christ.sti_mobile.Model.AllRisk.FormSuccessDetail.BuyQuoteFormGetHead_AllRisk;
import com.mike4christ.sti_mobile.Model.Auth.UserDataHead;
import com.mike4christ.sti_mobile.Model.Auth.ChangePassPost;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.UserGetObj;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.UserPostObj;
import com.mike4christ.sti_mobile.Model.Auth.RegisterObj;
import com.mike4christ.sti_mobile.Model.Claim.ClaimPost;
import com.mike4christ.sti_mobile.Model.Claim.TrackClaim.GetClaimStatus;
import com.mike4christ.sti_mobile.Model.Etic.EticPost.EticPostHead;
import com.mike4christ.sti_mobile.Model.Etic.FormSuccessDetail.BuyQuoteFormGetHead_Etic;
import com.mike4christ.sti_mobile.Model.Marine.FormSuccessDetail.BuyQuoteFormGetHead_Marine;
import com.mike4christ.sti_mobile.Model.Marine.MarinePost.MarinePostHead;
import com.mike4christ.sti_mobile.Model.Swiss.FormSuccessDetail.BuyQuoteFormGetHead_Swiss;
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
    Call<QouteHead> allrisk_quote(@Header("Authorization") String token, @Field("sum_insured") String sum_insrured);

    //Travel Quote
    @FormUrlEncoded
    @POST("get-travel-quote")
    Call<QouteHead> etic_quote(@Header("Authorization") String token, @Field("arrival_place") String arrival_place);

    //Swiss Quote
    @FormUrlEncoded
    @POST("get-swiss-quote")
    Call<QouteHead> swiss_quote(@Header("Authorization") String token, @Field("date_of_birth") String date_of_birth);

    //Marine Quote
    @FormUrlEncoded
    @POST("get-marine-quote")
    Call<QouteHead> marine_quote(@Header("Authorization") String token, @Field("value") String value,
                                @Field("conversion_rate") double conversion_rate);

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

    //Buy Swiss Policy
    @POST("make-claim")
    Call<ResponseBody> claim(@Header("Authorization") String token, @Body ClaimPost claimPost);

    //Buy Swiss Policy
    @GET("transaction-history")
    Call<TransactionHead> transaction_hist(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("track-claim")
    Call<GetClaimStatus> track_claim(@Header("Authorization") String token, @Field("claim_number") String claim_number);




}
