package com.zero.wolf.greenroad.litepalbean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/24.
 */

public class SupportDetail extends DataSupport implements Parcelable{

    private int lite_ID;


    private String station;
    private String lane;
    private String road;
    private String color;
    private String number;
    private String goods;

    public SupportDetail() {
    }

    protected SupportDetail(Parcel in) {
        lite_ID = in.readInt();

        station = in.readString();
        lane = in.readString();
        road = in.readString();
        color = in.readString();
        number = in.readString();
        goods = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lite_ID);
        dest.writeString(station);
        dest.writeString(lane);
        dest.writeString(road);
        dest.writeString(color);
        dest.writeString(number);
        dest.writeString(goods);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SupportDetail> CREATOR = new Creator<SupportDetail>() {
        @Override
        public SupportDetail createFromParcel(Parcel in) {
            return new SupportDetail(in);
        }

        @Override
        public SupportDetail[] newArray(int size) {
            return new SupportDetail[size];
        }
    };

    public int getLite_ID() {
        return lite_ID;
    }

    public void setLite_ID(int lite_ID) {
        this.lite_ID = lite_ID;
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
        return "SupportDetail{" +
                "lite_ID=" + lite_ID +
                ", station='" + station + '\'' +
                ", lane='" + lane + '\'' +
                ", road='" + road + '\'' +
                ", color='" + color + '\'' +
                ", number='" + number + '\'' +
                ", goods='" + goods + '\'' +
                '}';
    }
}
