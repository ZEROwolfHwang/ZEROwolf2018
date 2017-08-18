package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.fragment.CarNumberFragment;
import com.zero.wolf.greenroad.fragment.GoodsFragment;
import com.zero.wolf.greenroad.fragment.PhotoFragment;

/**
 * Created by Administrator on 2017/7/17.
 */

public class SureViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private final SerializableMain2Sure mMain2Sure;



    public SureViewPagerAdapter(FragmentManager manager, SerializableMain2Sure main2Sure) {
        super(manager);
        mMain2Sure = main2Sure;

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
                fragment = PhotoFragment.newInstance();
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
