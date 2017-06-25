package com.zero.wolf.greenroad;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/6/25 16:18
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/6/25
 * @updataDes ${描述更新内容}
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "GreenRoadApp";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.init(TAG).logLevel(LogLevel.FULL);

    }
}
