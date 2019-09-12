package com.mike4christ.sti_mobile.Model.Vehicle.Quote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable
{

    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("vehicle_value")
    @Expose
    private String vehicleValue;
    private final static long serialVersionUID = 8575967250047763141L;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVehicleValue() {
        return vehicleValue;
    }

    public void setVehicleValue(String vehicleValue) {
        this.vehicleValue = vehicleValue;
    }

}
