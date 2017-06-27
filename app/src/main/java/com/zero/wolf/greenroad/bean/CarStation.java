package com.zero.wolf.greenroad.bean;

/**
 * Created by Administrator on 2017/6/27.
 */

public class CarStation {

    /**
     * 是否置顶
     */
    public boolean isTop;

    /**
     * 收费站名
     */
    public String stationName;


    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
