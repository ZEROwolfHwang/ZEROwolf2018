package com.zero.wolf.greenroad;

import android.app.Application;

import com.zero.wolf.greenroad.interfacy.ThemeChangeObserver;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


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

    private List<ThemeChangeObserver> mThemeChangeObserverStack; //  主题切换监听栈

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

        LitePal.initialize(this);

    }

    /**
     * 向堆栈中所有对象发送更新UI的指令
     * */
    public void notifyByThemeChanged() {
        List<ThemeChangeObserver> observers = obtainThemeChangeObserverStack();
        for (ThemeChangeObserver observer : observers) {
            observer.loadingCurrentTheme(); //
            observer.notifyByThemeChanged(); //
        }
    }

    /**
     * 获得observer堆栈
     * */
    private List<ThemeChangeObserver> obtainThemeChangeObserverStack() {
        if (mThemeChangeObserverStack == null) mThemeChangeObserverStack = new ArrayList<>();
        return mThemeChangeObserverStack;
    }

    /**
     * 向堆栈中添加observer
     * */
    public void registerObserver(ThemeChangeObserver observer) {
        if (observer == null || obtainThemeChangeObserverStack().contains(observer)) return ;
        obtainThemeChangeObserverStack().add(observer);
    }

    /**
     * 从堆栈中移除observer
     * */
    public void unregisterObserver(ThemeChangeObserver observer) {
        if (observer == null || !(obtainThemeChangeObserverStack().contains(observer))) return ;
        obtainThemeChangeObserverStack().remove(observer);
    }
}
