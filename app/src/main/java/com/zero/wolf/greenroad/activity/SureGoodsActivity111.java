package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.SureCarNumberAdapter;
import com.zero.wolf.greenroad.adapter.SureCarStationAdapter;
import com.zero.wolf.greenroad.adapter.SureViewPagerAdapter;
import com.zero.wolf.greenroad.bean.SerializableNumber;
import com.zero.wolf.greenroad.litepalbean.SupportGoods;
import com.zero.wolf.greenroad.smartsearch.PinyinComparator;
import com.zero.wolf.greenroad.tools.ActionBarTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SureGoodsActivity111 extends BaseActivity {






    private EditText mEt_change2;
    private List<SupportGoods> mSupportGoodsList;

    private String mUsername;
    private String mPhotoPath1;
    private String mPhotoPath2;
    private String mPhotoPath3;
    private String mLicense_plate;
    private String mCar_goods;
    private String mCar_station;
    private String mColor;
    private List<SupportGoods> mSupportGoodses;



    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;





    private SureCarStationAdapter mStationAdapter;
    private SureCarNumberAdapter mNumberAdapter;
    private ArrayList<SerializableNumber> mSerializableNumberArrayList;

    private SureViewPagerAdapter mPagerAdapter;

    @BindView(R.id.tab_sure)
    TabLayout mTabLayoutSure;
    @BindView(R.id.view_pager_sure)
    ViewPager mViewPagerSure;
    @BindView(R.id.toolbar_sure)
    Toolbar mToolbarSure;
    private AppCompatActivity mActivity;
    private Context mContext;
    private String mStationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_goods111);

        ButterKnife.bind(this);

        mActivity = this;
        mContext = this;

        initToolbar();

        getIntentData();

        initViewPagerAndTabs();

    }

    private void initViewPagerAndTabs() {
        mPagerAdapter = new SureViewPagerAdapter(getSupportFragmentManager(),
                mUsername,mStationName,mColor,mPhotoPath1,mPhotoPath2,mPhotoPath3,this);
        mViewPagerSure.setOffscreenPageLimit(3);//设置viewpager预加载页面数
        mViewPagerSure.setAdapter(mPagerAdapter);  // 给Viewpager设置适配器
//        mViewpager.setCurrentItem(1); // 设置当前显示在哪个页面
        mTabLayoutSure.setupWithViewPager(mViewPagerSure);
    }

    public static void actionStart(Context context, String stationName, String color, String username
            , String photoPath1, String photoPath2, String photoPath3) {
        Intent intent = new Intent(context, SureGoodsActivity111.class);
        intent.putExtra("stationName", stationName);
        intent.putExtra("username", username);
        intent.putExtra("photoPath1", photoPath1);
        intent.putExtra("photoPath2", photoPath2);
        intent.putExtra("photoPath3", photoPath3);
        intent.putExtra("color", color);

        context.startActivity(intent);
    }

    /**
     * 得到从上一个activity中拿到的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        mColor = intent.getStringExtra("color");
        mStationName = intent.getStringExtra("stationName");
        mPhotoPath1 = intent.getStringExtra("photoPath1");
        mPhotoPath2 = intent.getStringExtra("photoPath2");
        mPhotoPath3 = intent.getStringExtra("photoPath3");
    }





    private void initToolbar() {

        setSupportActionBar(mToolbarSure);

        TextView title_text_view = ActionBarTool.getInstance(mActivity).getTitle_text_view();
        title_text_view.setText(getString(R.string.sure_goods_type));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示返回上级的箭头
        //getSupportActionBar().setDisplayShowTitleEnabled(false);//将actionbar原有的标题去掉（这句一般是用在xml方法一实现）
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

