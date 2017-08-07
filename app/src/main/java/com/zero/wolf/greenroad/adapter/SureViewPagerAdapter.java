package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zero.wolf.greenroad.fragment.CarNumberFragment;
import com.zero.wolf.greenroad.fragment.CheckFragment;
import com.zero.wolf.greenroad.fragment.GoodsFragment;
import com.zero.wolf.greenroad.fragment.PhotoFragment;
import com.zero.wolf.greenroad.smartsearch.SortModel;

import java.util.List;

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
    private final List<SortModel> mGoodsList;
    private final String mOperator;

    public SureViewPagerAdapter(FragmentManager fm, String operator, String username, String stationName,
                                String color, String photoPath1, String photoPath2,
                                String photoPath3, List<SortModel> goodsList, Context context) {
        super(fm);
        this.context = context;
        mGoodsList = goodsList;
        mOperator = operator;
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
                fragment =  GoodsFragment.newInstance(mOperator,mUsername,mStationName,mColor,mPhotoPath1,
                        mPhotoPath2,mPhotoPath3,mGoodsList, context);
                break;
            case 2:
                fragment = PhotoFragment.newInstance("111","...");
                break;
            case 3:
                fragment =  CheckFragment.newInstance("dvdfrad","orjk");
                break;

            default:
                fragment = CarNumberFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "车牌号";
            case 1:
                return "货物";
            case 2:
                return "照片";
            case 3:
                return "检查结果";

        }
        return null;
    }
}
