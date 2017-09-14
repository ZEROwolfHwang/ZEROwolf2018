package com.android.htc.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.htc.greenroad.activity.ShowActivity;
import com.android.htc.greenroad.bean.SerializableMain2Sure;
import com.android.htc.greenroad.fragment.CarNumberFragment;
import com.android.htc.greenroad.fragment.GoodsFragment;
import com.android.htc.greenroad.fragment.PhotoFragment;

/**
 * Created by Administrator on 2017/7/17.
 */

public class SureViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private final SerializableMain2Sure mMain2Sure;
    private int mLite_ID;
    private String mEnterType;


    public SureViewPagerAdapter(FragmentManager manager, SerializableMain2Sure main2Sure,String enterType) {
        super(manager);
        mMain2Sure = main2Sure;
        mEnterType = enterType;
    }
    public SureViewPagerAdapter(FragmentManager manager, SerializableMain2Sure main2Sure,String enterType,int lite_ID) {
        super(manager);
        mMain2Sure = main2Sure;
        mEnterType = enterType;
        mLite_ID = lite_ID;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = CarNumberFragment.newInstance(mMain2Sure.getCarNumber_I());
                break;
            case 1:
                fragment =  GoodsFragment.newInstance(mMain2Sure.getGoods_I());
                break;
            case 2:
                if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(mEnterType)) {
                fragment = PhotoFragment.newInstance(mEnterType,mLite_ID);
                } else {
                fragment = PhotoFragment.newInstance(mEnterType);
                }
                break;

            default:
                fragment = CarNumberFragment.newInstance(mMain2Sure.getCarNumber_I());
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
                return "车牌号";
            case 1:
                return "载物";
            case 2:
                return "照片";

        }
        return null;
    }
}
