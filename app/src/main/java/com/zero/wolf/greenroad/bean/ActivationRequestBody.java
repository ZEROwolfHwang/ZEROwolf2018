package com.zero.wolf.greenroad.bean;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/7/5 10:06
 * @updateAuthor ${Author}
 * @updataTIme 2016/7/5
 * @updataDes ${描述更新内容}
 */
public class ActivationRequestBody {

    private String macID;
    private String macName;
    private String regKey;

    public String getMacID() {
        return macID;
    }

    public void setMacID(String macID) {
        this.macID = macID;
    }

    public String getMacName() {
        return macName;
    }

    public void setMacName(String macName) {
        this.macName = macName;
    }

    public String getRegKey() {
        return regKey;
    }

    public void setRegKey(String regKey) {
        this.regKey = regKey;
    }
}
