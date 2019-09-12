package com.mike4christ.sti_mobile.retrofit_interface;



import com.mike4christ.sti_mobile.Model.Auth.UserDataHead;
import com.mike4christ.sti_mobile.Model.Auth.ChangePassPost;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.UserGetObj;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.UserPostObj;
import com.mike4christ.sti_mobile.Model.Auth.RegisterObj;
import com.mike4christ.sti_mobile.Model.Vehicle.BrandType.VehicleBrandType;
import com.mike4christ.sti_mobile.Model.Vehicle.Quote.QouteHead;
import com.mike4christ.sti_mobile.Model.Vehicle.VehicleBrand.Vehicles_Brand;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePost.VehiclePostHead;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    Call<ResponseBody> vehicle_policy(@Header("Authorization") String token, @Body VehiclePostHead vehiclePostHead);

    //Vehicle Quote
    @GET("get-vehicle-quote")
    Call<QouteHead> vehicle_quote(@Header("Authorization") String token, @Body VehiclePostHead vehiclePostHead);



}
