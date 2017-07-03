package com.zero.wolf.greenroad.litepalbean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/7/3.
 */

public class PhotoLite {

    private int id;
    private Bitmap bitmap_number;
    private Bitmap bitmap_body;
    private Bitmap bitmap_goods;


    private String license_color;
    private String name;
    private String station;
    private String license_plate;
    private String goods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap_number() {
        return bitmap_number;
    }

    public void setBitmap_number(Bitmap bitmap_number) {
        this.bitmap_number = bitmap_number;
    }

    public Bitmap getBitmap_body() {
        return bitmap_body;
    }

    public void setBitmap_body(Bitmap bitmap_body) {
        this.bitmap_body = bitmap_body;
    }

    public Bitmap getBitmap_goods() {
        return bitmap_goods;
    }

    public void setBitmap_goods(Bitmap bitmap_goods) {
        this.bitmap_goods = bitmap_goods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }
    public String getLicense_color() {
        return license_color;
    }

    public void setLicense_color(String license_color) {
        this.license_color = license_color;
    }
}
