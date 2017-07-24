package com.zero.wolf.greenroad.servicy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.tools.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */

public class NotPostReceiver extends BroadcastReceiver {

    private List<String> mList;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case "com.zero.wolf.greenroad.servicy.action.TYPE_THREAD":
                // tvServiceStatus.setText("服务状态：" + intent.getStringExtra("status"));
                mList = intent.getStringArrayListExtra("status");

                ToastUtils.singleToast(mList.toString());
                Logger.i("" + mList.size() + "=====" + mList.toString());
//                Logger.i(status);
                break;
        }
    }

}
