package com.abood.crowdfunding;

import java.util.Date;

public class Campaign {
    private String campTitle;
    private String campDescription;
    private Date campStart;
    private Date campEnd;
    private String campTarget;
    private String campLocation;
    private String campCountry;
    private String campImageUrl;
    private String campVideoUrl;

    public Campaign() {
    }

    public Campaign(String campTitle, String campDescription, Date campStart, Date campEnd, String campTarget,
                    String campLocation, String campCountry, String campImageUrl, String campVideoUrl) {
        this.campTitle = campTitle;
        this.campDescription = campDescription;
        this.campStart = campStart;
        this.campEnd = campEnd;
        this.campTarget = campTarget;
        this.campLocation = campLocation;
        this.campCountry = campCountry;
        this.campImageUrl = campImageUrl;
        this.campVideoUrl = campVideoUrl;
    }

    public Campaign(String campTitle, String campDescription, String campTarget,
                    String campLocation, String campCountry, String campImageUrl) {
        this.campTitle = campTitle;
        this.campDescription = campDescription;
        this.campTarget = campTarget;
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

    public String getCampTarget() {
        return campTarget;
    }

    public void setCampTarget(String campTarget) {
        this.campTarget = campTarget;
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

    public String getCampImageUrl() {
        return campImageUrl;
    }

    public void setCampImageUrl(String campImageUrl) {
        this.campImageUrl = campImageUrl;
    }

    public String getCampVideoUrl() {
        return campVideoUrl;
    }

    public void setCampVideoUrl(String campVideoUrl) {
        this.campVideoUrl = campVideoUrl;
    }
}
