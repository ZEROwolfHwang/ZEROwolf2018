package com.zero.wolf.greenroad.bean;

/**
 * Created by shadow on 2016/3/4.
 */
public class SerializablePreview {


    private int isPost;

    private String shutTime;

    private String station;

    private String operator;

    private String car_number;

    private String car_goods;



    public int getIsPost() {
        return isPost;
    }

    public void setIsPost(int isPost) {
        this.isPost = isPost;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getShutTime() {
        return shutTime;
    }

    public void setShutTime(String shutTime) {
        this.shutTime = shutTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getCar_goods() {
        return car_goods;
    }

    public void setCar_goods(String car_goods) {
        this.car_goods = car_goods;
    }


    @Override
    public String toString() {
        return "SerializablePreview{" +
                "isPost=" + isPost +
                ", shutTime='" + shutTime + '\'' +
                ", station='" + station + '\'' +
                ", operator='" + operator + '\'' +
                ", car_number='" + car_number + '\'' +
                ", car_goods='" + car_goods + '\'' +
                '}';
    }
}
