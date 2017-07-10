package com.zero.wolf.greenroad.httpresultbean;

/**
 * Created by Administrator on 2017/7/5.
 */

public class StationDataBean {

    private int id;
    private String zhanname;

    @Override
    public String toString() {
        return "StationDataBean{" +
                "id=" + id +
                ", zhanname='" + zhanname + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZhanname() {
        return zhanname;
    }

    public void setZhanname(String zhanname) {
        this.zhanname = zhanname;
    }
}
