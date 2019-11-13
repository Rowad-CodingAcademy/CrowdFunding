package com.abood.crowdfunding;

import org.json.JSONException;
import java.util.Date;

public class Campaigns {

    private String pId;
    private String pUser;
    private Date Date;
    private String pDate;
    private String pQuestion;
    private String pType;
    private Boolean pAction;
    private String pImage;

    public Campaigns() {

        Date = new Date();

    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpUser() {
        return pUser;
    }

    public void setpUser(String pUser) {
        this.pUser = pUser;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String getpQuestion() {
        return pQuestion;
    }

    public void setpQuestion(String pQuestion) {
        this.pQuestion = pQuestion;
    }

    public Boolean getpAction() {
        return pAction;
    }

    public void setpAction(Boolean pAction) {
        this.pAction = pAction;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    static Campaigns createTask() throws JSONException {

        Campaigns pPosts = new Campaigns();

//        pPosts.setpId(post.getString("id"));
//        pPosts.setpDate(post.getString("date"));
//        pPosts.setpUser(post.getString("user"));
//        pPosts.setpType(post.getString("type"));
//        pPosts.setpQuestion(post.getString("question"));
//        pPosts.setpImage(post.getString("image"));
////        pPosts.setpAction(post.getBoolean("action"));


        pPosts.setpDate("2020");
        pPosts.setpUser("Abood");
        pPosts.setpType("android");
        pPosts.setpQuestion("Campaign");

        return  pPosts;

    }

}
