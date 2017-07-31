package com.zero.wolf.greenroad.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import okhttp3.MultipartBody;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/7/23 18:34
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/7/23
 * @updataDes ${描述更新内容}
 */

public class PostContent implements Parcelable {
    /* sColor, currentTime, sUsername,
     mCar_station, mCar_number, mCar_goods, parts
     */
    private String car_type;
    private String color;
    private String currentTime;
    private String username;
    private String operator;
    private String car_station;
    private String car_number;
    private String car_goods;
    private String photoPath1;
    private String photoPath2;
    private String photoPath3;
    private String statiomName;
    private List<MultipartBody.Part> parts;

    public PostContent() {

    }

    protected PostContent(Parcel in) {
        car_type = in.readString();
        color = in.readString();
        currentTime = in.readString();
        username = in.readString();
        operator = in.readString();
        car_station = in.readString();
        car_number = in.readString();
        car_goods = in.readString();
        photoPath1 = in.readString();
        photoPath2 = in.readString();
        photoPath3 = in.readString();
        statiomName = in.readString();
    }

    public static final Creator<PostContent> CREATOR = new Creator<PostContent>() {
        @Override
        public PostContent createFromParcel(Parcel in) {
            return new PostContent(in);
        }

        @Override
        public PostContent[] newArray(int size) {
            return new PostContent[size];
        }
    };

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCar_station() {
        return car_station;
    }

    public void setCar_station(String car_station) {
        this.car_station = car_station;
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

    public String getPhotoPath1() {
        return photoPath1;
    }

    public void setPhotoPath1(String photoPath1) {
        this.photoPath1 = photoPath1;
    }

    public String getPhotoPath2() {
        return photoPath2;
    }

    public void setPhotoPath2(String photoPath2) {
        this.photoPath2 = photoPath2;
    }

    public String getPhotoPath3() {
        return photoPath3;
    }

    public void setPhotoPath3(String photoPath3) {
        this.photoPath3 = photoPath3;
    }

    public List<MultipartBody.Part> getParts() {
        return parts;
    }

    public void setParts(List<MultipartBody.Part> parts) {
        this.parts = parts;
    }

    public String getStatiomName() {
        return statiomName;
    }

    public void setStatiomName(String statiomName) {
        this.statiomName = statiomName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(car_type);
        dest.writeString(color);
        dest.writeString(currentTime);
        dest.writeString(username);
        dest.writeString(operator);
        dest.writeString(car_station);
        dest.writeString(car_number);
        dest.writeString(car_goods);
        dest.writeString(photoPath1);
        dest.writeString(photoPath2);
        dest.writeString(photoPath3);
        dest.writeString(statiomName);
    }
}
