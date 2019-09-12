package com.mike4christ.sti_mobile.Model.Vehicle.VehiclePost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Vehicle implements Serializable
{

    @SerializedName("period")
    @Expose
    public String period;
    @SerializedName("policy_type")
    @Expose
    public String policyType;
    @SerializedName("enhanced_third_party")
    @Expose
    public String enhancedThirdParty;
    @SerializedName("public_commercial")
    @Expose
    public String publicCommercial;
    @SerializedName("motor_cycle_policy")
    @Expose
    public String motorCyclePolicy;
    @SerializedName("make")
    @Expose
    public String make;
    @SerializedName("body_type")
    @Expose
    public String bodyType;
    @SerializedName("year")
    @Expose
    public String year;
    @SerializedName("home_address")
    @Expose
    public String homeAddress;
    @SerializedName("registration_number")
    @Expose
    public String registrationNumber;
    @SerializedName("chasis_number")
    @Expose
    public String chasisNumber;
    @SerializedName("engine_number")
    @Expose
    public String engineNumber;
    @SerializedName("vehicle_value")
    @Expose
    public String vehicleValue;
    @SerializedName("pictures")
    @Expose
    public List<String> pictures = null;

    public Vehicle(String period, String policyType, String enhancedThirdParty, String publicCommercial, String motorCyclePolicy, String make, String bodyType, String year, String homeAddress, String registrationNumber, String chasisNumber, String engineNumber, String vehicleValue, List<String> pictures) {
        this.period = period;
        this.policyType = policyType;
        this.enhancedThirdParty = enhancedThirdParty;
        this.publicCommercial = publicCommercial;
        this.motorCyclePolicy = motorCyclePolicy;
        this.make = make;
        this.bodyType = bodyType;
        this.year = year;
        this.homeAddress = homeAddress;
        this.registrationNumber = registrationNumber;
        this.chasisNumber = chasisNumber;
        this.engineNumber = engineNumber;
        this.vehicleValue = vehicleValue;
        this.pictures = pictures;
    }
}
