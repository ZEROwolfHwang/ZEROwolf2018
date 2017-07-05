package com.zero.wolf.greenroad.bean;

/**
 * Created by Administrator on 2017/7/4.
 */

public class LoginName<T> {

    /**
     * code : 201
     * msg : 账号错误
     */

    private int code;
    private String msg;
    private String data;

    @Override
    public String toString() {
        return "LoginName{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
