package com.abood.crowdfunding;

import java.util.UUID;

public class Users
{

    private String uPhotoUrl;

    public Users(String uName, String uPhotoUrl) {
        this.uPhotoUrl = uPhotoUrl;
    }



    public String getuPhotoUrl() {
        return uPhotoUrl;
    }

}
