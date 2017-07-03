package com.zero.wolf.greenroad.bean;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/1/5 10:54
 * @updateAuthor ${Author}
 * @updataTIme 2017/1/5
 * @updataDes ${描述更新内容}
 */

public class LogBean {

    /**
     * ExMessage : 出错信息（100）
     * ExStack : 出错堆栈（500）
     * MachineInfo : 机器信息（500）
     * RegKey : 注册码
     */

    private String ExMessage;
    private String ExStack;
    private String MachineInfo;
    private String RegKey;

    public String getExMessage() {
        return ExMessage;
    }

    public void setExMessage(String ExMessage) {
        this.ExMessage = ExMessage;
    }

    public String getExStack() {
        return ExStack;
    }

    public void setExStack(String ExStack) {
        this.ExStack = ExStack;
    }

    public String getMachineInfo() {
        return MachineInfo;
    }

    public void setMachineInfo(String MachineInfo) {
        this.MachineInfo = MachineInfo;
    }

    public String getRegKey() {
        return RegKey;
    }

    public void setRegKey(String RegKey) {
        this.RegKey = RegKey;
    }

    @Override
    public String toString() {
        return "LogBean{" +
                "ExMessage='" + ExMessage + '\'' +
                ", ExStack='" + ExStack + '\'' +
                ", MachineInfo='" + MachineInfo + '\'' +
                ", RegKey='" + RegKey + '\'' +
                '}';
    }
}
