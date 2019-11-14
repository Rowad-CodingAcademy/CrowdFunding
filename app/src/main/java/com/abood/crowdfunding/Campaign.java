package com.abood.crowdfunding;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Campaign {
    private String campTitle;
    private String campDescription;
    private Date campStart;
    private Date campEnd;
    private String campLocation;
    private String campCountry;
    private ArrayList<String> campImageUrl;
    private String campVideoUrl;
    private UUID campOwnerId;
    private UUID campDownersNum;
    private Double campCost;

    public Double getCampCost() {
        return campCost;
    }

    public void setCampCost(Double campCost) {
        this.campCost = campCost;
    }

    public UUID getCampDownersNum() {
        return campDownersNum;
    }

    public void setCampDownersNum(UUID campDownersNum) {
        this.campDownersNum = campDownersNum;
    }


    public UUID getCampOwnerId() {
        return campOwnerId;
    }

    public void setCampOwnerId(UUID campOwnerId) {
        this.campOwnerId = campOwnerId;
    }


    public Campaign() {
    }

    public Campaign(String campTitle, String campDescription, Date campStart, Date campEnd,
                    String campLocation, String campCountry, ArrayList<String> campImageUrl, String campVideoUrl) {
        this.campTitle = campTitle;
        this.campDescription = campDescription;
        this.campStart = campStart;
        this.campEnd = campEnd;
        this.campLocation = campLocation;
        this.campCountry = campCountry;
        this.campImageUrl = campImageUrl;
        this.campVideoUrl = campVideoUrl;
    }

    public Campaign(String campTitle, String campDescription, String campTarget,
                    String campLocation, String campCountry, ArrayList<String> campImageUrl) {
        this.campTitle = campTitle;
        this.campDescription = campDescription;
        this.campLocation = campLocation;
        this.campCountry = campCountry;
        this.campImageUrl = campImageUrl;
    }

    public String getCampTitle() {
        return campTitle;
    }

    public void setCampTitle(String campTitle) {
        this.campTitle = campTitle;
    }

    public String getCampDescription() {
        return campDescription;
    }

    public void setCampDescription(String campDescription) {
        this.campDescription = campDescription;
    }

    public Date getCampStart() {
        return campStart;
    }

    public void setCampStart(Date campStart) {
        this.campStart = campStart;
    }

    public Date getCampEnd() {
        return campEnd;
    }

    public void setCampEnd(Date campEnd) {
        this.campEnd = campEnd;
    }

    public String getCampLocation() {
        return campLocation;
    }

    public void setCampLocation(String campLocation) {
        this.campLocation = campLocation;
    }

    public String getCampCountry() {
        return campCountry;
    }

    public void setCampCountry(String campCountry) {
        this.campCountry = campCountry;
    }

    public ArrayList<String> getCampImageUrl() {
        return campImageUrl;
    }

    public void setCampImageUrl(ArrayList<String> campImageUrl) {
        this.campImageUrl = campImageUrl;
    }

    public String getCampVideoUrl() {
        return campVideoUrl;
    }

    public void setCampVideoUrl(String campVideoUrl) {
        this.campVideoUrl = campVideoUrl;
    }
}
