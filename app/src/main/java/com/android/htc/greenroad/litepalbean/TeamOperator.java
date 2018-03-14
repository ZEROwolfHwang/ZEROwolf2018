package com.android.htc.greenroad.litepalbean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/7/6.
 */

public class TeamOperator extends DataSupport {



    private String username;
    private TeamItem mTeamItem;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TeamItem getTeamItem() {
        return DataSupport.where("username = ?", String.valueOf(username)).findFirst(TeamItem.class);

    }

    public void setTeamItem(TeamItem teamItem) {
        mTeamItem = teamItem;
    }

    @Override
    public String toString() {
        return "TeamOperator{" +
                "username=" + username +
                ", mTeamItem=" + mTeamItem +
                '}';
    }
}
