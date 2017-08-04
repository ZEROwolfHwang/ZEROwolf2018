package com.zero.wolf.greenroad.bean;

/**
 * Created by Administrator on 2017/8/3.
 */

public class SettingOperatorInfo {
    private boolean isLoginSelected;
    private boolean isCheckSelected;
    private String job_number;
    private String operator_name;

    public boolean isLoginSelected() {
        return isLoginSelected;
    }

    public void setLoginSelected(boolean loginSelected) {
        isLoginSelected = loginSelected;
    }

    public boolean isCheckSelected() {
        return isCheckSelected;
    }

    public void setCheckSelected(boolean checkSelected) {
        isCheckSelected = checkSelected;
    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    @Override
    public String toString() {
        return "SettingOperatorInfo{" +
                "isLoginSelected=" + isLoginSelected +
                ", isCheckSelected=" + isCheckSelected +
                ", job_number='" + job_number + '\'' +
                ", operator_name='" + operator_name + '\'' +
                '}';
    }
}
