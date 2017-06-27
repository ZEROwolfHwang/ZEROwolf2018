package com.zero.wolf.greenroad.litepalbean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/6/27.
 */

public class CarStationName extends DataSupport {

    private String stationName;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
