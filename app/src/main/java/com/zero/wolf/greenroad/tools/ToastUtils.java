package com.zero.wolf.greenroad.tools;

import android.widget.Toast;

import com.zero.wolf.greenroad.GreenRoadApplication;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 文 件 名: ToastUtils
 * 创 建 人: sineom
 * 创建日期: 2017/5/11 14:57
 * 邮   箱: h.sineom@gmail.com
 * 修改时间：
 * 修改备注：
 */


public class ToastUtils {

    private static Toast mToast;

    /**
     * 单利土司
     *
     * @param content 土司的内容
     */
    public static void singleToast(String content) {

        Observable.just(content)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (mToast == null) {
                        mToast = Toast.makeText(GreenRoadApplication.sApplication, null, Toast.LENGTH_SHORT);
                    }
                    mToast.setText(content);
                    mToast.show();
                });

    }

}
