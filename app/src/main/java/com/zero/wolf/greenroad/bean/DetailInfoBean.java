package com.zero.wolf.greenroad.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class DetailInfoBean{
    private String color;
    private String number;
    private String goods;
    private List<String> bitmapPaths;

    public DetailInfoBean() {
    }


    public List<String> getBitmapPaths() {
        return bitmapPaths;
    }

    public void setBitmapPaths(List<String> bitmapPaths) {
        this.bitmapPaths = bitmapPaths;
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
                ", bitmapPaths=" + bitmapPaths +
                '}';
    }
}
