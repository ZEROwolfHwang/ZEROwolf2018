package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zero.wolf.greenroad.fragment.ConfigFragment;
import com.zero.wolf.greenroad.fragment.DetailsFragment;

/**
 * Created by Administrator on 2017/7/17.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;


    public MainViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = ConfigFragment.newInstance();
                break;
            case 1:
                fragment = DetailsFragment.newInstance();
                break;

            default:
                fragment = ConfigFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "配置信息";
            case 1:
                return "登记信息";

        }
        return null;
    }
}
