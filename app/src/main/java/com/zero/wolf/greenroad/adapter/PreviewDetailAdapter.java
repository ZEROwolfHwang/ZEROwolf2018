package com.zero.wolf.greenroad.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zero.wolf.greenroad.bean.SerializablePreview;
import com.zero.wolf.greenroad.fragment.PreviewBodyFragment;
import com.zero.wolf.greenroad.fragment.PreviewGoodsFragment;
import com.zero.wolf.greenroad.fragment.PreviewNumberFragment;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/7/31 22:26
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/7/31
 * @updataDes ${描述更新内容}
 */

public class PreviewDetailAdapter extends FragmentPagerAdapter{


    private final SerializablePreview mPreviewDetail;

    public PreviewDetailAdapter(FragmentManager fm, SerializablePreview previewDetail) {
        super(fm);

        mPreviewDetail = previewDetail;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = PreviewNumberFragment.newInstance(mPreviewDetail);
                break;
            case 1:
                fragment = PreviewBodyFragment.newInstance(mPreviewDetail);
                break;
            case 2:
                fragment =  PreviewGoodsFragment.newInstance(mPreviewDetail);
                break;
            default:
                fragment = PreviewNumberFragment.newInstance(mPreviewDetail);
                break;
        }
        return fragment;

    }


    @Override
    public int getCount() {
        return 3;
    }
}
