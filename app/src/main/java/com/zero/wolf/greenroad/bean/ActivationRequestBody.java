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

    private String macCode;
    private String mCode;
    private String regKey;

    public String getMacCode() {
        return macCode;
    }

    public void setMacCode(String macCode) {
        this.macCode = macCode;
    }

    public String getMCode() {
        return mCode;
    }

    public void setMCode(String mCode) {
        this.mCode = mCode;
    }

    public String getRegKey() {
        return regKey;
    }

    public void setRegKey(String regKey) {
        this.regKey = regKey;
    }
}
