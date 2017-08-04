package com.zero.wolf.greenroad.litepalbean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/7/6.
 */

public class SupportOperator extends DataSupport {

    private String job_number;
    private String operator_name;
    private boolean check_select;
    private boolean login_select;

    public boolean isCheck_select() {
        return check_select;
    }

    public void setCheck_select(boolean check_select) {
        this.check_select = check_select;
    }

    public boolean isLogin_select() {
        return login_select;
    }

    public void setLogin_select(boolean login_select) {
        this.login_select = login_select;
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
        return "SupportOperator{" +
                "job_number='" + job_number + '\'' +
                ", operator_name='" + operator_name + '\'' +
                '}';
    }
}
