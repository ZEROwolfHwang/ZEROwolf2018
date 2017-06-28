package com.zero.wolf.greenroad.presenter;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/10/21 13:09
 * @updateAuthor ${Author}
 * @updataTIme 2016/10/21
 * @updataDes ${描述更新内容}
 */

public class GetActivationCodeBean {

    private String DiskAndBaseBoard = "";
    private String Info = "";
    private String MAC = "";
    private String MD5Check = "";
    private String RegDateTime = "/Date(928120800000+0800)/";
    private String RegKey = "";
    private String UBMVersion = "pad";
    private String WinVersion = "";

    public String getDiskAndBaseBoard() {
        return DiskAndBaseBoard;
    }

    public void setDiskAndBaseBoard(String DiskAndBaseBoard) {
        this.DiskAndBaseBoard = DiskAndBaseBoard;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String Info) {
        this.Info = Info;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getMD5Check() {
        return MD5Check;
    }

    public void setMD5Check(String MD5Check) {
        this.MD5Check = MD5Check;
    }

    public String getRegDateTime() {
        return RegDateTime;
    }

    public void setRegDateTime(String RegDateTime) {
        this.RegDateTime = RegDateTime;
    }

    public String getRegKey() {
        return RegKey;
    }

    public void setRegKey(String RegKey) {
        this.RegKey = RegKey;
    }

    public String getUBMVersion() {
        return UBMVersion;
    }

    public void setUBMVersion(String UBMVersion) {
        this.UBMVersion = UBMVersion;
    }

    public String getWinVersion() {
        return WinVersion;
    }

    public void setWinVersion(String WinVersion) {
        this.WinVersion = WinVersion;
    }
}
