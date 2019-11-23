package com.abood.crowdfunding;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Campaigns {

    private String campaignTitle;
    private String campaignDescription;
    private String campaignLocation;
    private String campaignCountry;
    private String campaignImage;

    public Campaigns() {
    }


    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void campaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public void campaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public String getCampaignLocation() {
        return campaignLocation;
    }

    public void campaignLocation(String campaignLocation) {
        this.campaignLocation = campaignLocation;
    }

    public String getCampaignCountry() {
        return campaignCountry;
    }

    public void campaignCountry(String campaignCountry) {
        this.campaignCountry = campaignCountry;
    }

    public String getCampaignImage() {
        return campaignImage;
    }

    public void campaignImage(String campaignImage) {
        this.campaignImage = campaignImage;
    }
}
