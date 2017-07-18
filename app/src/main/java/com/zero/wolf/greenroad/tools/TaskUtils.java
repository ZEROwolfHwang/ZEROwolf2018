package com.zero.wolf.greenroad.tools;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Administrator on 2017/7/18.
 */

public class TaskUtils {
    /**
     * 判断某一个类是否存在任务栈里面
     * @return
     */
    public static boolean isExsitMianActivity(Class<?> cls, Context context){
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }


}
