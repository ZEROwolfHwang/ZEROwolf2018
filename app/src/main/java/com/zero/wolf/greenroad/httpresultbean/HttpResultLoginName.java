package com.zero.wolf.greenroad.httpresultbean;

/**
 * Created by Administrator on 2017/7/4.
 */

public class HttpResultLoginName {

    /**
     * code : 201
     * msg : 账号错误
     */

    private int code;
    private String msg;
    private String operator;
    private String stationName;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
