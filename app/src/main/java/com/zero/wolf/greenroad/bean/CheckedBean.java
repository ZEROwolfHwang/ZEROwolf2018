package com.zero.wolf.greenroad.bean;

/**
 * Created by Administrator on 2017/8/18.
 */

public class CheckedBean {

    private String conclusion;
    private String description;
    private String siteCheck;
    private String siteLogin;
    private int isRoom;
    private int isFree;

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteCheck() {
        return siteCheck;
    }

    public void setSiteCheck(String siteCheck) {
        this.siteCheck = siteCheck;
    }

    public String getSiteLogin() {
        return siteLogin;
    }

    public void setSiteLogin(String siteLogin) {
        this.siteLogin = siteLogin;
    }

    public int getIsRoom() {
        return isRoom;
    }

    public void setIsRoom(int isRoom) {
        this.isRoom = isRoom;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    @Override
    public String toString() {
        return "CheckedBean{" +
                "conclusion='" + conclusion + '\'' +
                ", description='" + description + '\'' +
                ", siteCheck='" + siteCheck + '\'' +
                ", siteLogin='" + siteLogin + '\'' +
                ", isRoom=" + isRoom +
                ", isFree=" + isFree +
                '}';
    }
}
