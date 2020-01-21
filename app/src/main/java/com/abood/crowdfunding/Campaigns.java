package com.abood.crowdfunding;


public class Campaigns
{

    private String campaignTitle;
    private String campaignDescription;
    private String campaignImage;
    private String campaignCost;
    private String campaignFunds;
    private String campaignDonationDays;


    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public String getCampaignImage() {
        return campaignImage;
    }

    public void setCampaignImage(String campaignImage) {
        this.campaignImage = campaignImage;
    }

    public String getCampaignCost() {
        return campaignCost;
    }

    public String getCampaignFunds() {
        return campaignFunds;
    }

    public String getCampaignDonationDays() {
        return campaignDonationDays;
    }

}
