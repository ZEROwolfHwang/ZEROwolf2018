package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.SureViewPagerAdapter;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.fragment.MyBitmap;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.smartsearch.SortModel;
import com.zero.wolf.greenroad.tools.ActionBarTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SureGoodsActivity extends BaseActivity {


    private List<SortModel> mGoodsList = new ArrayList<>();
    private SureViewPagerAdapter mPagerAdapter;


    @BindView(R.id.tab_sure)
    TabLayout mTabLayoutSure;
    @BindView(R.id.view_pager_sure)
    ViewPager mViewPagerSure;
    @BindView(R.id.toolbar_sure)
    Toolbar mToolbarSure;
    private AppCompatActivity mActivity;
    private Context mContext;

    private String mType;
    private SerializableMain2Sure mMain2Sure;
    private ArrayList<MyBitmap> mMyBitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_goods);

        ButterKnife.bind(this);

        mActivity = this;
        mContext = this;

        initToolbar();

        getIntentData();


        initViewPagerAndTabs();

    }



    private void initViewPagerAndTabs() {
        mPagerAdapter = new SureViewPagerAdapter(getSupportFragmentManager(),mMain2Sure);
        mViewPagerSure.setOffscreenPageLimit(3);//设置viewpager预加载页面数

        mViewPagerSure.setAdapter(mPagerAdapter);  // 给Viewpager设置适配器
        if (GlobalManager.ENTERTYPE_NUMBER.equals(mType)) {
            mViewPagerSure.setCurrentItem(0); // 设置当前显示在哪个页面}
        }
        if (GlobalManager.ENTERTYPE_GOODS.equals(mType)) {
            mViewPagerSure.setCurrentItem(1); // 设置当前显示在哪个页面}
        }
        if (GlobalManager.ENTERTYPE_PHOTO.equals(mType)) {
            mViewPagerSure.setCurrentItem(2); // 设置当前显示在哪个页面}
        }

        mTabLayoutSure.setupWithViewPager(mViewPagerSure);
    }


    public static void actionStart(Context context, SerializableMain2Sure main2Sure, String type) {
        Intent intent = new Intent(context, SureGoodsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("main2Sure", main2Sure);
        //bundle.putParcelableArrayList("myBitmapList", (ArrayList<? extends Parcelable>) myBitmaps);
        intent.putExtras(bundle);
        intent.setType(type);
        context.startActivity(intent);
    }

    /**
     * 得到从上一个activity中拿到的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        mType = intent.getType();
        Bundle bundle = intent.getExtras();
        mMain2Sure= (SerializableMain2Sure) bundle.getSerializable("main2Sure");

    }


    private void initToolbar() {

        setSupportActionBar(mToolbarSure);

        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText(getString(R.string.sure_goods_type));

        mToolbarSure.setNavigationIcon(R.drawable.back_up_logo);
        mToolbarSure.setNavigationOnClickListener((v -> finish()));

    }

    @Override
    protected void onPause() {

        super.onPause();
        //存入缓存
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

