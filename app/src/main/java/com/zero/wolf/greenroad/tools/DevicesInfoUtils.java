package com.zero.wolf.greenroad.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/7/4 17:17
 * @updateAuthor ${Author}
 * @updataTIme 2016/7/4
 * @updataDes ${描述更新内容}
 */
public class DevicesInfoUtils {

    private static DevicesInfoUtils sDevicesInfoUtils;

    public static DevicesInfoUtils getInstance() {
        if (sDevicesInfoUtils == null)
            sDevicesInfoUtils = new DevicesInfoUtils();
        return sDevicesInfoUtils;
    }

    private DevicesInfoUtils() {

    }

    public String getMacAddress(Context context) {
        String macAddress = null;
        try {
            macAddress = loadFileAsString("/sys/class/net/eth0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            Log.d("DevicesInfoUtils", e.getMessage());
        } finally {
            if (null == macAddress) {
                try {
                    macAddress = getPhoneMacAddress(context);
                    return macAddress.replace(":", "-");
                } catch (SocketException e) {
                    e.printStackTrace();
                    return macAddress;
                }

            } else {
                return macAddress.replace(":", "-");
            }
        }
    }

    public static String getPhoneMacAddress(Context context) throws SocketException {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macStr = "";
        boolean wifiEnabled = wifi.isWifiEnabled();
        if (!wifiEnabled)
            wifi.setWifiEnabled(true);
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iF = (NetworkInterface) interfaces.nextElement();
            byte[] addr = iF.getHardwareAddress();
            if (addr == null || addr.length == 0 || !iF.getName().equals("wlan0")) {
                continue;
            }
            StringBuilder buf = new StringBuilder();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macStr = buf.toString();
        }
        if (!wifiEnabled)
            wifi.setWifiEnabled(false);
        Log.d("DevicesInfoUtils", "+++"+macStr);
        return macStr;
    }

    private String loadFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public String getDevicesId(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    public String getVersion(Context context) {
        PackageManager packmanager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packmanager.getPackageInfo(context.getPackageName(), 0);
            String version = packageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getVersionCode(Context context) {
        PackageManager packmanager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packmanager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            int version = packageInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断sdk版本是否在5.0以上
     *
     * @return true表示是的 反之低于或等于5.0
     */
    public static boolean SDKVersionMUp() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
