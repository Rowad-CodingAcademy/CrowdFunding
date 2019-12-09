package com.abood.crowdfunding;


import java.util.UUID;

public class Campaigns
{

    private UUID campaignID;
    private String campaignTitle;
    private String campaignDescription;
    private String campaignLocation;
    private String campaignCountry;
    private String campaignImage;
    private String campaignCost;

    public UUID getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(UUID campaignID) {
        this.campaignID = campaignID;
    }


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

    public String getCampaignLocation() {
        return campaignLocation;
    }

    public void setCampaignLocation(String campaignLocation) {
        this.campaignLocation = campaignLocation;
    }

    public String getCampaignCountry() {
        return campaignCountry;
    }

    public void setCampaignCountry(String campaignCountry) {
        this.campaignCountry = campaignCountry;
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

    public void setCampaignCost(String campaignCost) {
        this.campaignCost = campaignCost;
    }
}
