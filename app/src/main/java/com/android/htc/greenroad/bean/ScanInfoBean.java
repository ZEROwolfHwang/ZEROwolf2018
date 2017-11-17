package com.android.htc.greenroad.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/14.
 */

public class ScanInfoBean implements Serializable{

    private String scan_01Q;
    private String scan_04Q;
    private String scan_05Q;
    private String scan_06Q;
    private String scan_10Q;
    private String scan_12Q;
    private int isLimit;


    public String getScan_01Q() {
        return scan_01Q;
    }

    public void setScan_01Q(String scan_01Q) {
        this.scan_01Q = scan_01Q;
    }


    public String getScan_04Q() {
        return scan_04Q;
    }

    public void setScan_04Q(String scan_04Q) {
        this.scan_04Q = scan_04Q;
    }

    public String getScan_05Q() {
        return scan_05Q;
    }

    public void setScan_05Q(String scan_05Q) {
        this.scan_05Q = scan_05Q;
    }

    public String getScan_06Q() {
        return scan_06Q;
    }

    public void setScan_06Q(String scan_06Q) {
        this.scan_06Q = scan_06Q;
    }


    public String getScan_10Q() {
        return scan_10Q;
    }

    public void setScan_10Q(String scan_10Q) {
        this.scan_10Q = scan_10Q;
    }


    public String getScan_12Q() {
        return scan_12Q;
    }

    public void setScan_12Q(String scan_12Q) {
        this.scan_12Q = scan_12Q;
    }

    public int getIsLimit() {
        return isLimit;
    }

    public void setIsLimit(int isLimit) {
        this.isLimit = isLimit;
    }

    @Override
    public String toString() {
        return "ScanInfoBean{" +
                "scan_01Q='" + scan_01Q + '\'' +
                ", scan_04Q='" + scan_04Q + '\'' +
                ", scan_05Q='" + scan_05Q + '\'' +
                ", scan_06Q='" + scan_06Q + '\'' +
                ", scan_10Q='" + scan_10Q + '\'' +
                ", scan_12Q='" + scan_12Q + '\'' +
                ", isLimit=" + isLimit +
                '}';
    }
}
