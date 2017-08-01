package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.PreviewDetailAdapter;
import com.zero.wolf.greenroad.bean.SerializablePreview;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewDetailActivity extends BaseActivity {

    @BindView(R.id.preview_item_detail)
    ViewPager mViewPagerDetail;
    @BindView(R.id.page1)
    RadioButton mPage1;
    @BindView(R.id.page2)
    RadioButton mPage2;
    @BindView(R.id.page3)
    RadioButton mPage3;
    @BindView(R.id.page_tag)
    RadioGroup mPageTag;
    private SerializablePreview mPreviewDetail;
    private PreviewDetailAdapter mAdapter;
    private int mOpenType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_detail);
        ButterKnife.bind(this);

        getIntentData();

        mAdapter = new PreviewDetailAdapter(getSupportFragmentManager(),mPreviewDetail);
        mViewPagerDetail.setOffscreenPageLimit(3);

        if (mOpenType == 0)
            mPageTag.check(mPage1.getId());
        else if (mOpenType == 1)
            mPageTag.check(mPage2.getId());
        else if (mOpenType == 2)
            mPageTag.check(mPage3.getId());

        mViewPagerDetail.setAdapter(mAdapter);
        mViewPagerDetail.setCurrentItem(mOpenType);

        mViewPagerDetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int currentPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * @param position 根据位置调整滑动指示器
             */
            @Override
            public void onPageSelected(int position) {

                currentPosition = position;

                position = position % 3;
                if (position == 0)
                    mPageTag.check(mPage1.getId());
                else if (position == 1)
                    mPageTag.check(mPage2.getId());
                else if (position == 2)
                    mPageTag.check(mPage3.getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    private void getIntentData() {
        mPreviewDetail = (SerializablePreview) getIntent().getSerializableExtra("previewDetail");
        mOpenType = getIntent().getIntExtra("openType",0);
    }

    public static void actionStart(Context context, SerializablePreview preview, int openType) {
        Intent intent = new Intent(context, PreviewDetailActivity.class);
        intent.putExtra("previewDetail", preview);
        intent.putExtra("openType",openType);
        context.startActivity(intent);
    }


}
