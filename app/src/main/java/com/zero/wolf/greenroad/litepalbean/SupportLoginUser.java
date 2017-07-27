package com.zero.wolf.greenroad.litepalbean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/7/6.
 */

public class SupportLoginUser extends DataSupport {

    private String logindate;
    private String stationName;
    private String username;
    private String password;
    private String operator;


    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLogindate() {
        return logindate;
    }

    public void setLogindate(String logindate) {
        this.logindate = logindate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginUserInfo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }
}
