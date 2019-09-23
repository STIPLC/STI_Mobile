package com.mike4christ.sti_mobile.Model.Pin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mike4christ.sti_mobile.Model.Auth.User;

import java.io.Serializable;

public class setPin implements Serializable
{

    @SerializedName("user")
    @Expose
    private UserPin user;

    public setPin(UserPin user) {
        this.user = user;
    }
}