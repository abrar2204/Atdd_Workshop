package com.tw.atdd_workshop.domains;

public class Customer {
    private final String id;
    private String phoneNumber;
    private Boolean isVerified;

    public Customer(String id, String phoneNumber, Boolean isVerified) {
       this.id = id;
       this.phoneNumber = phoneNumber;
       this.isVerified = isVerified;
    }

    public String getId() {
        return id;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}   
