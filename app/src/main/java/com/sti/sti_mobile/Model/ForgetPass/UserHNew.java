package com.sti.sti_mobile.Model.ForgetPass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserHNew implements Serializable {

    @SerializedName("user")
    @Expose
    private UserNewPassPost userNewPassPost;

    public UserHNew(UserNewPassPost userNewPassPost) {
        this.userNewPassPost = userNewPassPost;
    }

}
