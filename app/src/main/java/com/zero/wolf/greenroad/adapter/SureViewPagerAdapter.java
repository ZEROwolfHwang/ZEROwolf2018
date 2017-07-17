package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zero.wolf.greenroad.bean.SerializableNumber;
import com.zero.wolf.greenroad.bean.SerializableStation;
import com.zero.wolf.greenroad.fragment.CarNumberFragment;
import com.zero.wolf.greenroad.fragment.GoodsFragment;
import com.zero.wolf.greenroad.fragment.StationFragment;
import com.zero.wolf.greenroad.smartsearch.SortModel;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class SureViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private final List<SerializableNumber> mNumberList;
    private final List<SerializableStation> mStationList;
    private final List<SortModel> mGoodsList;

    public SureViewPagerAdapter(FragmentManager fm, List<SerializableNumber> numberList, List<SerializableStation> stationList, List<SortModel> goodsList, Context context) {
        super(fm);
        this.context = context;
        mNumberList = numberList;
        mStationList = stationList;
        mGoodsList = goodsList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = CarNumberFragment.newInstance(mNumberList);
                break;
            case 1:
                fragment = StationFragment.newInstance(mStationList);
                break;
            case 2:
                fragment =  GoodsFragment.newInstance(mGoodsList, context);
                break;
            default:
                fragment = CarNumberFragment.newInstance(mNumberList);
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
