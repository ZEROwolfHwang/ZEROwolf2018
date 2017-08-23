package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zero.wolf.greenroad.activity.ShowActivity;
import com.zero.wolf.greenroad.bean.ScanInfoBean;
import com.zero.wolf.greenroad.fragment.CheckedFragment;
import com.zero.wolf.greenroad.fragment.DetailsFragment;
import com.zero.wolf.greenroad.fragment.ScanFragment;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ShowViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private ScanInfoBean mScanInfoBean;
    private String enterType;


    public ShowViewPagerAdapter(FragmentManager fm, Context context, String type) {
        super(fm);
        this.context = context;
        enterType = type;
    }

    public ShowViewPagerAdapter(FragmentManager fm, Context context, ScanInfoBean bean, String type) {
        super(fm);
        this.context = context;
        mScanInfoBean = bean;
        enterType = type;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (ShowActivity.TYPE_MAIN_ENTER_SHOW.equals(enterType)) {

            switch (position) {
                case 0:
                    fragment = DetailsFragment.newInstance();
                    break;
                case 1:
                    fragment = ScanFragment.newInstance(enterType);
                    break;
                case 2:
                    fragment = CheckedFragment.newInstance();
                    break;

                default:
                    fragment = DetailsFragment.newInstance();
                    break;
            }
        } else if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(enterType)) {
            switch (position) {
                case 0:
                    fragment = DetailsFragment.newInstance();
                    break;
                case 1:
                    fragment = ScanFragment.newInstance(enterType,mScanInfoBean);
                    break;
                case 2:
                    fragment = CheckedFragment.newInstance();
                    break;

                default:
                    fragment = DetailsFragment.newInstance();
                    break;
            }


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
