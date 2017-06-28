package com.zero.wolf.greenroad.presenter;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/8/4 15:13
 * @updateAuthor ${Author}
 * @updataTIme 2016/8/4
 * @updataDes ${描述更新内容}
 */
public class UpdateApp {

    private int versionCode;
    private String versionName;
    private String url;
    private String note;
    private String md5;
    private int size;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
