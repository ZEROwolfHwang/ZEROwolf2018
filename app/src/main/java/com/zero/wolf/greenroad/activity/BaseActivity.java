package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.GreenRoadApplication;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.interfacy.ThemeChangeObserver;
import com.zero.wolf.greenroad.tools.ActivityCollector;
import com.zero.wolf.greenroad.tools.SPUtils;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/6/25 16:18
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/6/25
 * @updataDes ${描述更新内容}
 */

public  class BaseActivity extends AppCompatActivity implements ThemeChangeObserver {
    private static final String TAG = "GreenRoadApp";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setupActivityBeforeCreate();
        super.onCreate(savedInstanceState);

        ActivityCollector.addActivity(this);

        Logger.init(TAG).logLevel(LogLevel.FULL);

    }

    /**
     * */
    private void setupActivityBeforeCreate() {
        ((GreenRoadApplication) getApplication()).registerObserver(this);
        loadingCurrentTheme();
    }


    public Context getContext() {
        return BaseActivity.this;
    }

    /**
     * */
    public void switchCurrentThemeTag() {
        setThemeTag(0 - getThemeTag());
        loadingCurrentTheme();
    }
    /**
     * */
    public void switchCurrentCameraModel() {
        setModelTag(0 - getModelTag());
    }

    @Override
    public void loadingCurrentTheme() {
        switch (getThemeTag()) {
            case  1:
                setTheme(R.style.GreenRoadTheme_Day);
                break;
            case -1:
                setTheme(R.style.GreenRoadTheme_Night);
                break;
        }
    }

    @Override
    public void notifyByThemeChanged() {

    }


    /**
     * 得到当前主题标签
     * */
    protected int getThemeTag() {
       /* SharedPreferences preferences = getSharedPreferences("MarioCache", Context.MODE_PRIVATE);
        return preferences.getInt(KEY_MARIO_CACHE_THEME_TAG, 1);*/
        return (int) SPUtils.get(this, SPUtils.KEY_THEME_TAG, 1);
    }

    /**
     * 设置主题标签并记录下来
     * */
    protected void setThemeTag(int tag) {
      /*  SharedPreferences preferences = getSharedPreferences("MarioCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(SPUtils.KEY_THEME_TAG, tag);*/
        SPUtils.putAndApply(this, SPUtils.KEY_THEME_TAG, tag);
    }
    /**
     * 得到当前模式标签
     * */
    protected int getModelTag() {
        return (int) SPUtils.get(this, SPUtils.KEY_CAMERA_MODEL, 1);
    }

    /**
     * 设置模式标签并记录下来
     * */
    protected void setModelTag(int tag) {

        SPUtils.putAndApply(this, SPUtils.KEY_CAMERA_MODEL, tag);
    }


    /**
     * 得到状态栏的高度
     * */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        ((GreenRoadApplication) getApplication()).unregisterObserver(this);
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }
}
