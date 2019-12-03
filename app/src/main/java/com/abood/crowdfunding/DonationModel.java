package com.abood.crowdfunding;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.firestore.v1.DocumentTransform;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class DonationModel {
    String donationID;
    String CampaignID;
    String amount_reward;
    String userID;
    String cardHolder;
    String securityCode;

    public DonationModel() {
    }

    public String getDonationID() {
        return donationID;
    }

    public String getCampaignID() {
        return CampaignID;
    }

    public String getAmount_reward() {
        return amount_reward;
    }

    public String getUserID() {
        return userID;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getSecurityCode() {
        return securityCode;
    }
}
