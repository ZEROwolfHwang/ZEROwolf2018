package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.PreviewDetailAdapter;
import com.zero.wolf.greenroad.bean.SerializablePreview;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewDetailActivity extends AppCompatActivity {

    @BindView(R.id.preview_item_detail)
    ViewPager mViewPagerDetail;
    private SerializablePreview mPreviewDetail;
    private PreviewDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_detail);
        ButterKnife.bind(this);

        getIntentData();

        mAdapter = new PreviewDetailAdapter(getSupportFragmentManager(), mPreviewDetail);
        mViewPagerDetail.setOffscreenPageLimit(3);
        mViewPagerDetail.setAdapter(mAdapter);
    }


    private void getIntentData() {
        mPreviewDetail = (SerializablePreview) getIntent().getSerializableExtra("previewDetail");

    }

    public static void actionStart(Context context, SerializablePreview preview) {
        Intent intent = new Intent(context, PreviewDetailActivity.class);
        intent.putExtra("previewDetail",  preview);
        context.startActivity(intent);
    }


}
