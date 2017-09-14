package com.android.htc.greenroad.litepalbean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/24.
 */

public class SupportScan extends DataSupport implements Parcelable{

    private int lite_ID;

    private String scan_code;
    private String scan_01Q;
    private String scan_02Q;
    private String scan_03Q;
    private String scan_04Q;
    private String scan_05Q;
    private String scan_06Q;
    private String scan_07Q;
    private String scan_08Q;
    private String scan_09Q;
    private String scan_10Q;
    private String scan_11Q;
    private String scan_12Q;

    public SupportScan() {
    }

    protected SupportScan(Parcel in) {
        lite_ID = in.readInt();
        scan_code = in.readString();
        scan_01Q = in.readString();
        scan_02Q = in.readString();
        scan_03Q = in.readString();
        scan_04Q = in.readString();
        scan_05Q = in.readString();
        scan_06Q = in.readString();
        scan_07Q = in.readString();
        scan_08Q = in.readString();
        scan_09Q = in.readString();
        scan_10Q = in.readString();
        scan_11Q = in.readString();
        scan_12Q = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lite_ID);
        dest.writeString(scan_code);
        dest.writeString(scan_01Q);
        dest.writeString(scan_02Q);
        dest.writeString(scan_03Q);
        dest.writeString(scan_04Q);
        dest.writeString(scan_05Q);
        dest.writeString(scan_06Q);
        dest.writeString(scan_07Q);
        dest.writeString(scan_08Q);
        dest.writeString(scan_09Q);
        dest.writeString(scan_10Q);
        dest.writeString(scan_11Q);
        dest.writeString(scan_12Q);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SupportScan> CREATOR = new Creator<SupportScan>() {
        @Override
        public SupportScan createFromParcel(Parcel in) {
            return new SupportScan(in);
        }

        @Override
        public SupportScan[] newArray(int size) {
            return new SupportScan[size];
        }
    };

    @Override
    public String toString() {
        return "SupportScan{" +
                "lite_ID=" + lite_ID +
                ", scan_code='" + scan_code + '\'' +
                ", scan_01Q='" + scan_01Q + '\'' +
                ", scan_02Q='" + scan_02Q + '\'' +
                ", scan_03Q='" + scan_03Q + '\'' +
                ", scan_04Q='" + scan_04Q + '\'' +
                ", scan_05Q='" + scan_05Q + '\'' +
                ", scan_06Q='" + scan_06Q + '\'' +
                ", scan_07Q='" + scan_07Q + '\'' +
                ", scan_08Q='" + scan_08Q + '\'' +
                ", scan_09Q='" + scan_09Q + '\'' +
                ", scan_10Q='" + scan_10Q + '\'' +
                ", scan_11Q='" + scan_11Q + '\'' +
                ", scan_12Q='" + scan_12Q + '\'' +
                '}';
    }

    public int getLite_ID() {
        return lite_ID;
    }

    public void setLite_ID(int lite_ID) {
        this.lite_ID = lite_ID;
    }

    public String getScan_code() {
        return scan_code;
    }

    public void setScan_code(String scan_code) {
        this.scan_code = scan_code;
    }

    public String getScan_01Q() {
        return scan_01Q;
    }

    public void setScan_01Q(String scan_01Q) {
        this.scan_01Q = scan_01Q;
    }

    public String getScan_02Q() {
        return scan_02Q;
    }

    public void setScan_02Q(String scan_02Q) {
        this.scan_02Q = scan_02Q;
    }

    public String getScan_03Q() {
        return scan_03Q;
    }

    public void setScan_03Q(String scan_03Q) {
        this.scan_03Q = scan_03Q;
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

    public String getScan_07Q() {
        return scan_07Q;
    }

    public void setScan_07Q(String scan_07Q) {
        this.scan_07Q = scan_07Q;
    }

    public String getScan_08Q() {
        return scan_08Q;
    }

    public void setScan_08Q(String scan_08Q) {
        this.scan_08Q = scan_08Q;
    }

    public String getScan_09Q() {
        return scan_09Q;
    }

    public void setScan_09Q(String scan_09Q) {
        this.scan_09Q = scan_09Q;
    }

    public String getScan_10Q() {
        return scan_10Q;
    }

    public void setScan_10Q(String scan_10Q) {
        this.scan_10Q = scan_10Q;
    }

    public String getScan_11Q() {
        return scan_11Q;
    }

    public void setScan_11Q(String scan_11Q) {
        this.scan_11Q = scan_11Q;
    }

    public String getScan_12Q() {
        return scan_12Q;
    }

    public void setScan_12Q(String scan_12Q) {
        this.scan_12Q = scan_12Q;
    }
}
