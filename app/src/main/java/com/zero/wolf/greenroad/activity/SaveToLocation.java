package com.zero.wolf.greenroad.activity;

import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/7/23 22:42
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/7/23
 * @updataDes ${描述更新内容}
 */

public class SaveToLocation {

    /**
     * 保存到本地数据库
     *
     * @param currentTime
     * @param
     */
    public static void saveLocalLite(String currentTime, String mCar_type, String mUsername, String mColor,
                                     String mCar_number, String mCar_station, String mCar_goods,
                                     String mPhotoPath1, String mPhotoPath2, String mPhotoPath3, int isPost) {
        SupportPhotoLite supportPhotoLite = new SupportPhotoLite();
        supportPhotoLite.setShutTime(currentTime);
        supportPhotoLite.setCar_type(mCar_type);
        supportPhotoLite.setUsername(mUsername);
        supportPhotoLite.setLicense_color(mColor);
        supportPhotoLite.setLicense_plate(mCar_number);
        supportPhotoLite.setStation(mCar_station);
        supportPhotoLite.setGoods(mCar_goods);
        supportPhotoLite.setPhotoPath1(mPhotoPath1);
        supportPhotoLite.setPhotoPath2(mPhotoPath2);
        supportPhotoLite.setPhotoPath3(mPhotoPath3);
        supportPhotoLite.setIsPost(isPost);
        supportPhotoLite.save();
    }
}
