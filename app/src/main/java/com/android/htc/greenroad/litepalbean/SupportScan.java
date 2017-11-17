package com.android.htc.greenroad.litepalbean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/24.
 */

public class SupportScan extends DataSupport implements Parcelable{

    private int lite_ID;

    private String scan_01Q;
    private String scan_04Q;
    private String scan_05Q;
    private String scan_06Q;
    private String scan_10Q;
    private String scan_12Q;
    private int isLimit;

    public SupportScan() {
    }

    protected SupportScan(Parcel in) {
        lite_ID = in.readInt();
        scan_01Q = in.readString();
        scan_04Q = in.readString();
        scan_05Q = in.readString();
        scan_06Q = in.readString();
        scan_10Q = in.readString();
        scan_12Q = in.readString();
        isLimit = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lite_ID);
        dest.writeString(scan_01Q);
        dest.writeString(scan_04Q);
        dest.writeString(scan_05Q);
        dest.writeString(scan_06Q);
        dest.writeString(scan_10Q);
        dest.writeString(scan_12Q);
        dest.writeInt(isLimit);
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

    public int getIsLimit() {
        return isLimit;
    }

    public void setIsLimit(int isLimit) {
        this.isLimit = isLimit;
    }

    public int getLite_ID() {
        return lite_ID;
    }

    public void setLite_ID(int lite_ID) {
        this.lite_ID = lite_ID;
    }

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
}
