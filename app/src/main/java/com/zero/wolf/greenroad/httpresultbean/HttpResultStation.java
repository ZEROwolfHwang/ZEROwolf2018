package com.zero.wolf.greenroad.httpresultbean;

/**
 * Created by Administrator on 2017/7/5.
 */

public class HttpResultStation<T> {



    private int code;
    private String msg;
    private T data;

    @Override
    public String toString() {
        return "StationLite{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
