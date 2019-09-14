package com.mike4christ.sti_mobile.Model.Marine.FormSuccessDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerDetails implements Serializable
{

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("company_name")
    @Expose
    private Object companyName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("account_number")
    @Expose
    private Integer accountNumber;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("marital_status")
    @Expose
    private String maritalStatus;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("next_of_kin_address")
    @Expose
    private String nextOfKinAddress;
    @SerializedName("next_of_kin")
    @Expose
    private String nextOfKin;
    @SerializedName("next_of_kin_phone")
    @Expose
    private String nextOfKinPhone;
    @SerializedName("identification_means")
    @Expose
    private String identificationMeans;
    @SerializedName("employer")
    @Expose
    private String employer;
    @SerializedName("employer_address")
    @Expose
    private String employerAddress;
    @SerializedName("customer_type")
    @Expose
    private String customerType;
    @SerializedName("contact_person")
    @Expose
    private Object contactPerson;
    @SerializedName("tin_number")
    @Expose
    private Object tinNumber;
    @SerializedName("mailing_address")
    @Expose
    private Object mailingAddress;
    @SerializedName("office_address")
    @Expose
    private Object officeAddress;
    @SerializedName("business")
    @Expose
    private Object business;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    private final static long serialVersionUID = -5736052116718728823L;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Object companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getNextOfKinAddress() {
        return nextOfKinAddress;
    }

    public void setNextOfKinAddress(String nextOfKinAddress) {
        this.nextOfKinAddress = nextOfKinAddress;
    }

    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public String getNextOfKinPhone() {
        return nextOfKinPhone;
    }

    public void setNextOfKinPhone(String nextOfKinPhone) {
        this.nextOfKinPhone = nextOfKinPhone;
    }

    public String getIdentificationMeans() {
        return identificationMeans;
    }

    public void setIdentificationMeans(String identificationMeans) {
        this.identificationMeans = identificationMeans;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Object getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Object contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Object getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(Object tinNumber) {
        this.tinNumber = tinNumber;
    }

    public Object getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Object mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public Object getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(Object officeAddress) {
        this.officeAddress = officeAddress;
    }

    public Object getBusiness() {
        return business;
    }

    public void setBusiness(Object business) {
        this.business = business;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}