package com.zero.wolf.greenroad.bean;

import android.graphics.Bitmap;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/8/19 19:11
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/8/19
 * @updataDes ${描述更新内容}
 */

public class PhotoImageBean {
    private Bitmap mBitmap;
    private String title;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PhotoImageBean{" +
                "mBitmap=" + mBitmap +
                ", title='" + title + '\'' +
                '}';
    }
}
