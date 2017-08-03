/*
package com.zero.wolf.greenroad.polling;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.orhanobut.logger.Logger;

import java.util.concurrent.atomic.AtomicInteger;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

*/
/**
 * 后台长连接服务
 **//*

public class LongConnService extends Service {

    public static String ACTION = "com.youyou.uuelectric.renter.Service.LongConnService";
    private static MinaLongConnectManager minaLongConnectManager;
    public String tag = "LongConnService";
    private Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        // 执行启动长连接的操作
        startLongConnect();
        ObserverManager.addObserver("LongConnService", stopListener);
        return START_STICKY;
    }

    public ObserverListener stopListener = new ObserverListener() {
        @Override
        public void observer(String from, Object obj) {
            closeConnect();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeConnect();
    }

    */
/**
     * 开始执行启动长连接的操作
     *//*

    private void startLongConnect() {
        if (Config.isNetworkConnected(context)) {
            if (minaLongConnectManager != null && minaLongConnectManager.checkConnectStatus()) {
                Logger.i("长连接状态正常...");
                return;
            }
            if (minaLongConnectManager == null) {
                startThreadCreateConnect();
            } else {
                if (minaLongConnectManager.connectIsNull() && minaLongConnectManager.isNeedRestart()) {
                    Logger.i("session已关闭，需要重新创建一个session");
                    minaLongConnectManager.startConnect();
                } else {
                    Logger.i("长连接已关闭，需要重开一个线程来重新创建长连接");
                    startThreadCreateConnect();
                }
            }
        }

    }

    private final AtomicInteger mCount = new AtomicInteger(1);

    private void startThreadCreateConnect() {
        if (UserConfig.getUserInfo().getB3Key() != null && UserConfig.getUserInfo().getSessionKey() != null) {
            System.gc();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 执行具体启动长连接操作
                    minaLongConnectManager = MinaLongConnectManager.getInstance(context);
                    minaLongConnectManager.crateLongConnect();
                }
            }, "longConnectThread" + mCount.getAndIncrement()).start();
        }
    }


    private void closeConnect() {

        if (minaLongConnectManager != null) {
            minaLongConnectManager.closeConnect();
        }
        minaLongConnectManager = null;

        // 停止长连接服务LongConnService
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
*/
