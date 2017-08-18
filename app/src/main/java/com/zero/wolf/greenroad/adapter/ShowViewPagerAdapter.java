package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zero.wolf.greenroad.fragment.CheckedFragment;
import com.zero.wolf.greenroad.fragment.DetailsFragment;
import com.zero.wolf.greenroad.fragment.ScanFragment;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ShowViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;


    public ShowViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = DetailsFragment.newInstance();
                break;
            case 1:
                fragment = ScanFragment.newInstance();
                break;
            case 2:
                fragment = CheckedFragment.newInstance();
                break;

            default:
                fragment = DetailsFragment.newInstance();
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
                return "登记信息";
            case 1:
                return "收费信息";
            case 2:
                return "检查结论";

        }
        return null;
    }
}
