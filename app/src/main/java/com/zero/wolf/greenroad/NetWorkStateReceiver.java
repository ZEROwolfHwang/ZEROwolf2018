package com.zero.wolf.greenroad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

public class NetWorkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetWorkStateReceiver";

    private final TextView mTextView;
    private NetworkStation mListener;
    private StringBuilder mSb;

    @Override
    public void onReceive(Context context, Intent intent) {

        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mSb == null) {
                mSb = new StringBuilder();
            }
            mSb = null;
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                mSb.append("WIFIandDATA");
            } else {
                if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                    mSb.append("WIFI");

                } else {
                    if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                        mSb.append("DATA");
                    } else {
                        mSb.append("");
                    }
                }
            }
        } else {
            //这里的就不写了，前面有写，大同小异
            System.out.println("API level 大于21");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();

            //用于存放网络连接信息
            mSb = new StringBuilder();
            //通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[0]);
                mSb.append(networkInfo.getTypeName() + networkInfo.isConnected());
                if (networkInfo.isConnected()) {
                    mSb.append("DATA");
                } else {
                    mSb.append("");
                }
                //networkInfo.getType()+networkInfo.isConnected()
                Log.i(TAG, "onReceive: " + networkInfo.getType() + networkInfo.isConnected());
            }
            if (networks.length == 0) {
                mTextView.setText("网络无连接");
                mTextView.setTextColor(Color.RED);

            } else if (mSb.toString().contains("MOBILE")) {
                //// TODO: 2017/6/23
                mTextView.setText("数据连接");
                mTextView.setTextColor(Color.BLUE);
            }
        }
        mListener.onStation(mSb.toString());
    }

    public NetWorkStateReceiver(TextView textView) {
        super();
        mTextView = textView;
    }

    public interface NetworkStation {

        void onStation(String netStation);
    }

    public void setNetworkStationListener(NetworkStation stationListener) {
        mListener = stationListener;
    }

}
