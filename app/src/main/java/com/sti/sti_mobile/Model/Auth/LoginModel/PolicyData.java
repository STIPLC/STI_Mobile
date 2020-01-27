package com.sti.sti_mobile.Model.Auth.LoginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PolicyData implements Serializable
{

    @SerializedName("my_policies")
    @Expose
    private MyPolicies myPolicies;
    @SerializedName("my_claims")
    @Expose
    private List<MyClaim> myClaims = null;

    public MyPolicies getMyPolicies() {
        return myPolicies;
    }

    public void setMyPolicies(MyPolicies myPolicies) {
        this.myPolicies = myPolicies;
    }

    public List<MyClaim> getMyClaims() {
        return myClaims;
    }

    public void setMyClaims(List<MyClaim> myClaims) {
        this.myClaims = myClaims;
    }


}