package com.mike4christ.sti_mobile.Model.Auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.PolicyData;

import java.io.Serializable;

public class UserDataHead implements Serializable
{

    @SerializedName("user")
    @Expose
    public UserAsUser user;
    @SerializedName("wallet_balance")
    @Expose
    public String walletBalance;
    @SerializedName("policy_data")
    @Expose
    private PolicyData policyData;
    @SerializedName("payout_account")
    @Expose
    public PayoutAccount payoutAccount;

    public UserAsUser getUser() {
        return user;
    }



    public String getWalletBalance() {
        return walletBalance;
    }

    public PolicyData getPolicyData() {
        return policyData;
    }


    public PayoutAccount getPayoutAccount() {
        return payoutAccount;
    }


}
