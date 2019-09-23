package com.mike4christ.sti_mobile.Model.Auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PayoutAccount implements Serializable
{

    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("account_name")
    @Expose
    private String accountName;

    public String getBank() {
        return bank;
    }


    public String getAccountNumber() {
        return accountNumber;
    }


    public String getAccountName() {
        return accountName;
    }


}