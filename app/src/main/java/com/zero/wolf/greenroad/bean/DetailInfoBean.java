package com.zero.wolf.greenroad.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class DetailInfoBean{
    private String color;
    private String number;
    private String goods;
    private List<PathTitleBean> mPath_sanzheng;
    private List<PathTitleBean> mPath_cheshen;
    private List<PathTitleBean> mPath_huowu;

    public DetailInfoBean() {
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

    @Override
    public String toString() {
        return "DetailInfoBean{" +
                "color='" + color + '\'' +
                ", number='" + number + '\'' +
                ", goods='" + goods + '\'' +
                ", mPath_sanzheng=" + mPath_sanzheng +
                ", mPath_cheshen=" + mPath_cheshen +
                ", mPath_huowu=" + mPath_huowu +
                '}';
    }
}
