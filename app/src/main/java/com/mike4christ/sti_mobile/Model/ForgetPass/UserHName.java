package com.mike4christ.sti_mobile.Model.ForgetPass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserHName implements Serializable {
    @SerializedName("user")
    @Expose
    private UserName user;


    public UserName getUser_name() {
        return user;
    }

}
