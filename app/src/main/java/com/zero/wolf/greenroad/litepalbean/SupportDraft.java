package com.zero.wolf.greenroad.litepalbean;

import android.os.Parcel;
import android.os.Parcelable;

import com.zero.wolf.greenroad.bean.PathTitleBean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class SupportDraft extends DataSupport implements Parcelable{


    private String current_time;
    private String siteCheck;
    private String siteLogin;
    private String station;
    private String lane;
    private String road;

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

    private String color;
    private String number;
    private String goods;
    private int isRoom;
    private int isFree;
    private String conclusion;
    private String description;

    private List<PathTitleBean> mPath_sanzheng;
    private List<PathTitleBean> mPath_cheshen;
    private List<PathTitleBean> mPath_huowu;

    public SupportDraft() {
    }

    protected SupportDraft(Parcel in) {
        current_time = in.readString();
        siteCheck = in.readString();
        siteLogin = in.readString();
        station = in.readString();
        lane = in.readString();
        road = in.readString();
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
        color = in.readString();
        number = in.readString();
        goods = in.readString();
        isRoom = in.readInt();
        isFree = in.readInt();
        conclusion = in.readString();
        description = in.readString();
    }

    public static final Creator<SupportDraft> CREATOR = new Creator<SupportDraft>() {
        @Override
        public SupportDraft createFromParcel(Parcel in) {
            return new SupportDraft(in);
        }

        @Override
        public SupportDraft[] newArray(int size) {
            return new SupportDraft[size];
        }
    };

    @Override
    public String toString() {
        return "SupportDraft{" +
                "current_time='" + current_time + '\'' +
                ", siteCheck='" + siteCheck + '\'' +
                ", siteLogin='" + siteLogin + '\'' +
                ", station='" + station + '\'' +
                ", lane='" + lane + '\'' +
                ", road='" + road + '\'' +
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
                ", color='" + color + '\'' +
                ", number='" + number + '\'' +
                ", goods='" + goods + '\'' +
                ", isRoom=" + isRoom +
                ", isFree=" + isFree +
                ", conclusion='" + conclusion + '\'' +
                ", description='" + description + '\'' +
                ", mPath_sanzheng=" + mPath_sanzheng +
                ", mPath_cheshen=" + mPath_cheshen +
                ", mPath_huowu=" + mPath_huowu +
                '}';
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
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

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
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

    public List<PathTitleBean> getPath_sanzheng() {
        return mPath_sanzheng;
    }

    public void setPath_sanzheng(List<PathTitleBean> path_sanzheng) {
        mPath_sanzheng = path_sanzheng;
    }

    public List<PathTitleBean> getPath_cheshen() {
        return mPath_cheshen;
    }

    public void setPath_cheshen(List<PathTitleBean> path_cheshen) {
        mPath_cheshen = path_cheshen;
    }

    public List<PathTitleBean> getPath_huowu() {
        return mPath_huowu;
    }

    public void setPath_huowu(List<PathTitleBean> path_huowu) {
        mPath_huowu = path_huowu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(current_time);
        dest.writeString(siteCheck);
        dest.writeString(siteLogin);
        dest.writeString(station);
        dest.writeString(lane);
        dest.writeString(road);
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
        dest.writeString(color);
        dest.writeString(number);
        dest.writeString(goods);
        dest.writeInt(isRoom);
        dest.writeInt(isFree);
        dest.writeString(conclusion);
        dest.writeString(description);
    }
}
