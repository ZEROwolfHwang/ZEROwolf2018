package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.fragment.CarNumberFragment;
import com.zero.wolf.greenroad.fragment.CheckFragment;
import com.zero.wolf.greenroad.fragment.GoodsFragment;
import com.zero.wolf.greenroad.fragment.MyBitmap;
import com.zero.wolf.greenroad.fragment.PhotoFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class SureViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private final SerializableMain2Sure mMain2Sure;
    private final List<MyBitmap> mMyBitmaps;


    public SureViewPagerAdapter(FragmentManager manager, SerializableMain2Sure main2Sure,
                                List<MyBitmap> myBitmaps) {
        super(manager);
        mMain2Sure = main2Sure;
        mMyBitmaps = myBitmaps;
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
                fragment = PhotoFragment.newInstance(mMyBitmaps);
                break;
            case 3:
                fragment =  CheckFragment.newInstance(mMain2Sure.getConclusion_I(),mMain2Sure.getDescription_I());
                break;

            default:
                fragment = CarNumberFragment.newInstance(mMain2Sure.getCarNumber_I());
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
