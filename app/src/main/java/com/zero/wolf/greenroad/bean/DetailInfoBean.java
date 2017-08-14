package com.zero.wolf.greenroad.bean;

/**
 * Created by Administrator on 2017/8/14.
 */

public class DetailInfoBean {
    private String color;
    private String number;
    private String goods;
    private int isRoom;
    private int isFree;
    private String conclusion;
    private String description;

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

    @Override
    public String toString() {
        return "ConfigInfoBean{" +
                "color='" + color + '\'' +
                ", number='" + number + '\'' +
                ", goods='" + goods + '\'' +
                ", isRoom=" + isRoom +
                ", isFree=" + isFree +
                ", conclusion='" + conclusion + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
