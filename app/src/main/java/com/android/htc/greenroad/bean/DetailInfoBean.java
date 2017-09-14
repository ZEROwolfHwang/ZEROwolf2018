package com.android.htc.greenroad.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class DetailInfoBean{
    private String color;
    private String number;
    private String goods;
    private List<PathTitleBean> mPath_and_title;


    public DetailInfoBean() {
    }

    @Override
    public String toString() {
        return "DetailInfoBean{" +
                "color='" + color + '\'' +
                ", number='" + number + '\'' +
                ", goods='" + goods + '\'' +
                ", mPath_and_title=" + mPath_and_title +
                '}';
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

    public List<PathTitleBean> getPath_and_title() {
        return mPath_and_title;
    }

    public void setPath_and_title(List<PathTitleBean> path_and_title) {
        mPath_and_title = path_and_title;
    }
}
