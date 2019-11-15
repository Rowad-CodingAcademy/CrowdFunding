package com.abood.crowdfunding;

import java.util.UUID;

public class Users
{
    private UUID uId;
    private String uName;
    private String uEmail;
    private String uPhotoUrl;

    public Users(String uName, String uPhotoUrl) {
        this.uName = uName;
        this.uPhotoUrl = uPhotoUrl;
    }

    public UUID getuId() {
        return uId;
    }

    public void setuId(UUID uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuPhotoUrl() {
        return uPhotoUrl;
    }

    public void setuPhotoUrl(String uPhotoUrl) {
        this.uPhotoUrl = uPhotoUrl;
    }
}
