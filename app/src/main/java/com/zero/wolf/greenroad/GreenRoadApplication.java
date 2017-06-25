package com.zero.wolf.greenroad;

import android.app.Application;



/**
 * @author sineom
 * @version 1.0
 * @time 2017/6/25 12:28
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/6/25
 * @updataDes ${描述更新内容}
 */

public class GreenRoadApplication extends Application {
    public static GreenRoadApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

    }
}
