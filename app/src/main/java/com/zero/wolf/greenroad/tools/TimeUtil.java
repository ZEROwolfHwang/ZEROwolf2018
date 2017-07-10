package com.zero.wolf.greenroad.tools;

import android.content.Context;

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String shutTime = formatter.format(curDate);
        return shutTime;
    }

    public static Date getCurrentTimeToDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        // String shutTime = formatter.format(curDate);
        return curDate;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }


}
