package com.zero.wolf.greenroad.tools;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/7/10 22:54
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/7/10
 * @updataDes ${描述更新内容}
 */

public class TimeUtil {

    private final Context mContext;

    public TimeUtil(Context context) {
        mContext = context;
    }

    /**
     * 得到当前的系统时间
     */
    public static String getCurrentTimeTos() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String shutTime = formatter.format(curDate);
        return shutTime;
    }

    public static String getCurrentTimeToDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
        String curTime = formatter.format(new Date(System.currentTimeMillis()));
        // String shutTime = formatter.format(curDate);
        return curTime;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *time1 保存在数据库中的时间
     *time2 当前登陆的时间的时间
     * @return
     */
    public static int differentDaysByMillisecond(String time1, String time2) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));//天数
           // int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600));//小时
           // int days = (int) ((date2.getTime() - date1.getTime()) / (1000*60));//分钟
            Logger.i(days + "-----days");
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
