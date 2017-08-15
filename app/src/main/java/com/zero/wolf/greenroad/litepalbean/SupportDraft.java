package com.zero.wolf.greenroad.litepalbean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class SupportDraft extends DataSupport {


    private String draftTime;
    private String checkOperator;
    private String loginOperator;
    private String station;
    private String lane;
    private String road;

    private String scan_time;
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
    private List<String> bitmapPaths;

    public String getScan_time() {
        return scan_time;
    }

    public void setScan_time(String scan_time) {
        this.scan_time = scan_time;
    }

    public String getDraftTime() {
        return draftTime;
    }

    public void setDraftTime(String draftTime) {
        this.draftTime = draftTime;
    }

    public String getCheckOperator() {
        return checkOperator;
    }

    public void setCheckOperator(String checkOperator) {
        this.checkOperator = checkOperator;
    }

    public String getLoginOperator() {
        return loginOperator;
    }

    public void setLoginOperator(String loginOperator) {
        this.loginOperator = loginOperator;
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

    public List<String> getBitmapPaths() {
        return bitmapPaths;
    }

    public void setBitmapPaths(List<String> bitmapPaths) {
        this.bitmapPaths = bitmapPaths;
    }

    @Override
    public String toString() {
        return "SupportDraft{" +
                "draftTime='" + draftTime + '\'' +
                ", checkOperator='" + checkOperator + '\'' +
                ", loginOperator='" + loginOperator + '\'' +
                ", station='" + station + '\'' +
                ", lane='" + lane + '\'' +
                ", road='" + road + '\'' +
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
                ", bitmapPaths=" + bitmapPaths +
                '}';
    }
}
