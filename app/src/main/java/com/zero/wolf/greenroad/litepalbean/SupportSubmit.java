package com.zero.wolf.greenroad.litepalbean;

import android.os.Parcel;
import android.os.Parcelable;

import com.zero.wolf.greenroad.bean.PathTitleBean;
import com.zero.wolf.greenroad.bean.ScanInfoBean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class SupportSubmit extends DataSupport implements Parcelable{


    private String current_time;
    private String siteCheck;
    private String siteLogin;
    private String station;
    private String lane;
    private String road;

    private ScanInfoBean scanbean;

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
    public SupportSubmit() {
    }


    protected SupportSubmit(Parcel in) {
        current_time = in.readString();
        siteCheck = in.readString();
        siteLogin = in.readString();
        station = in.readString();
        lane = in.readString();
        road = in.readString();
        color = in.readString();
        number = in.readString();
        goods = in.readString();
        isRoom = in.readInt();
        isFree = in.readInt();
        conclusion = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(current_time);
        dest.writeString(siteCheck);
        dest.writeString(siteLogin);
        dest.writeString(station);
        dest.writeString(lane);
        dest.writeString(road);
        dest.writeString(color);
        dest.writeString(number);
        dest.writeString(goods);
        dest.writeInt(isRoom);
        dest.writeInt(isFree);
        dest.writeString(conclusion);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SupportSubmit> CREATOR = new Creator<SupportSubmit>() {
        @Override
        public SupportSubmit createFromParcel(Parcel in) {
            return new SupportSubmit(in);
        }

        @Override
        public SupportSubmit[] newArray(int size) {
            return new SupportSubmit[size];
        }
    };

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

    public ScanInfoBean getScanbean() {
        return scanbean;
    }

    public void setScanbean(ScanInfoBean scanbean) {
        this.scanbean = scanbean;
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
}
