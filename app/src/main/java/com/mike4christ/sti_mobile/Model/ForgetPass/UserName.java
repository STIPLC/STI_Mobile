package com.mike4christ.sti_mobile.Model.ForgetPass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserName implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }
}
