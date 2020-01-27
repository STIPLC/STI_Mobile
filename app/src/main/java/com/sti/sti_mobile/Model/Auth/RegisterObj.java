package com.sti.sti_mobile.Model.Auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterObj implements Serializable
{

    @SerializedName("user")
    @Expose
    public User user;

    public RegisterObj(User user) {
        super();
        this.user = user;
    }




}