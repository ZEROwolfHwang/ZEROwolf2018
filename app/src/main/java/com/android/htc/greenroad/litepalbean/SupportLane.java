package com.android.htc.greenroad.litepalbean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class SupportLane extends DataSupport {


    private List<String> lane;

    @Override
    public String toString() {
        return "SupportLane{" +
                "lane=" + lane +
                '}';
    }

    public List<String> getLane() {
        return lane;
    }

    public void setLane(List<String> lane) {
        this.lane = lane;
    }
}
