package com.mike4christ.sti_mobile.Model.ForgetPass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserEmail implements Serializable
{

    @SerializedName("email")
    @Expose
    private String email;

    public UserEmail(String email) {
        this.email = email;
    }
}
