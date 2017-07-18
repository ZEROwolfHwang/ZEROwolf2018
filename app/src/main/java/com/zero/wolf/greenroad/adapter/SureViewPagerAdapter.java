package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zero.wolf.greenroad.fragment.CarNumberFragment;
import com.zero.wolf.greenroad.fragment.GoodsFragment;
import com.zero.wolf.greenroad.fragment.StationFragment;

/**
 * Created by Administrator on 2017/7/17.
 */

public class SureViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private final String mUsername;
    private final String mColor;
    private final String mPhotoPath1;
    private final String mPhotoPath2;
    private final String mPhotoPath3;
    private final String mStationName;

    public SureViewPagerAdapter(FragmentManager fm, String username, String stationName,
                                String color, String photoPath1, String photoPath2,
                                String photoPath3, Context context) {
        super(fm);
        this.context = context;
        mUsername = username;
        mStationName = stationName;
        mColor = color;
        mPhotoPath1 = photoPath1;
        mPhotoPath2 = photoPath2;
        mPhotoPath3 = photoPath3;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = CarNumberFragment.newInstance();
                break;
            case 1:
                fragment = StationFragment.newInstance();
                break;
            case 2:
                fragment =  GoodsFragment.newInstance(mUsername,mStationName,mColor,mPhotoPath1,
                        mPhotoPath2,mPhotoPath3, context);
                break;
            default:
                fragment = CarNumberFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "123";
            case 1:
                return "456";
            case 2:
                return "789";
        }
        return null;
    }
}
