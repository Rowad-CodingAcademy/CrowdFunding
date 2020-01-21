package com.abood.crowdfunding;


public class DonationModel {

    String targetAmount;
    String userId;
    String campaignId;
    public boolean expanded = false;

    public DonationModel() {

    }

    public String getTargetAmount() {
        return targetAmount;
    }

    public String getUserId() {
        return userId;
    }

    public String getCampaignId() {
        return campaignId;
    }
}
