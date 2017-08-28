package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.activity.ShowActivity;
import com.zero.wolf.greenroad.fragment.CheckedFragment;
import com.zero.wolf.greenroad.fragment.DetailsFragment;
import com.zero.wolf.greenroad.fragment.ScanFragment;
import com.zero.wolf.greenroad.litepalbean.SupportChecked;
import com.zero.wolf.greenroad.litepalbean.SupportDetail;
import com.zero.wolf.greenroad.litepalbean.SupportMedia;
import com.zero.wolf.greenroad.litepalbean.SupportScan;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ShowViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private SupportScan mSupportScan;
    private String enterType;
    private SupportDetail mSupportDetail;
    private SupportChecked mSupportChecked;
    private SupportMedia mSupportMedia;


    public ShowViewPagerAdapter(FragmentManager fm, Context context, String type) {
        super(fm);
        this.context = context;
        enterType = type;
    }

    public ShowViewPagerAdapter(FragmentManager fm, Context context, SupportDetail supportDetail,
                                SupportScan supportScan, SupportChecked supportChecked,
                                SupportMedia supportMedia,String type) {
        super(fm);
        this.context = context;
        mSupportDetail = supportDetail;
        mSupportScan = supportScan;
        mSupportChecked = supportChecked;
        mSupportMedia = supportMedia;
        for (int i = 0; i < mSupportMedia.getPaths().size(); i++) {
            Logger.i(mSupportMedia.getPaths().get(i));
            Logger.i(mSupportMedia.getHeights().get(i)+"-----");
        }
        enterType = type;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (ShowActivity.TYPE_MAIN_ENTER_SHOW.equals(enterType)) {

            switch (position) {
                case 0:
                    fragment = DetailsFragment.newInstance(enterType);
                    break;
                case 1:
                    fragment = ScanFragment.newInstance(enterType);
                    break;
                case 2:
                    fragment = CheckedFragment.newInstance(enterType);
                    break;

                default:
                    fragment = DetailsFragment.newInstance(enterType);
                    break;
            }
        } else if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(enterType)) {
            switch (position) {
                case 0:
                    fragment = DetailsFragment.newInstance(enterType,mSupportDetail,mSupportMedia);
                    break;
                case 1:
                    fragment = ScanFragment.newInstance(enterType,mSupportScan);
                    break;
                case 2:
                    fragment = CheckedFragment.newInstance(enterType,mSupportChecked);
                    break;

                default:
                    fragment = DetailsFragment.newInstance(enterType);
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
