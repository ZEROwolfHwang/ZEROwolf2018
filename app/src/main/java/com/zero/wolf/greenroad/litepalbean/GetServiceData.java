package com.zero.wolf.greenroad.litepalbean;

/**
 * Created by Administrator on 2017/6/27.
 */

public class GetServiceData {

    private static GetServiceData mServiceData;

    public static GetServiceData getInstance() {
        if (mServiceData == null) {
            mServiceData = new GetServiceData();
        }
        return mServiceData;
    }
}
