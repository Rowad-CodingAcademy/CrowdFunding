package com.abood.crowdfunding;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.firestore.v1.DocumentTransform;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class DonationModel {
    String targetAmount;
    String cardNo;
    String holderName;
    String expirationDate;
    String code;
    String userId;
    String campaignId;
    public boolean expanded = false;

    public DonationModel() {

    }

    public String getTargetAmount() {
        return targetAmount;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCode() {
        return code;
    }

    public String getUserId() {
        return userId;
    }

    public String getCampaignId() {
        return campaignId;
    }
}
