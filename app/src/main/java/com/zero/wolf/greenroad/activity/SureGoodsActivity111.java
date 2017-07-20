package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.SureViewPagerAdapter;
import com.zero.wolf.greenroad.litepalbean.SupportGoods;
import com.zero.wolf.greenroad.smartsearch.SortModel;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.PingYinUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SureGoodsActivity111 extends BaseActivity {

    private String mUsername;
    private String mPhotoPath1;
    private String mPhotoPath2;
    private String mPhotoPath3;
    private String mColor;

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
    private String mStationName;
    private ArrayList<SortModel> mAcacheGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_goods111);

        ButterKnife.bind(this);

        mActivity = this;
        mContext = this;

        initToolbar();

        getIntentData();

        initGoodsData();

        initViewPagerAndTabs();

    }

    private void initGoodsData() {
        mAcacheGoods = (ArrayList<SortModel>) ACache
                .get(mActivity).getAsObject("goods");

        List<SupportGoods> supportGoodses = DataSupport.findAll(SupportGoods.class);

        if (mAcacheGoods != null) {
            if (mAcacheGoods.size() == supportGoodses.size()) {
                if (mAcacheGoods.size() > 0) {
                    int size = 0;
                    for (int i = 0; i < mAcacheGoods.size(); i++) {
                        if (mAcacheGoods.get(i).getBitmap() != null) {
                            size += size;
                        }
                        if (size != supportGoodses.size()) {
                            mAcacheGoods.clear();
                            addGoodsData(supportGoodses);
                        }
                    }
                }
                mGoodsList.addAll(mAcacheGoods);
            } else {
                mAcacheGoods.clear();
                addGoodsData(supportGoodses);
            }
        } else {
            addGoodsData(supportGoodses);
        }
    }

    private void addGoodsData(List<SupportGoods> supportGoodses) {
        for (int i = 0; i < supportGoodses.size(); i++) {

            String scientificname = supportGoodses.get(i).getScientificname();
            String alias = supportGoodses.get(i).getAlias();
            String imgurl = supportGoodses.get(i).getImgurl();

            SortModel sortModel = new SortModel();
            sortModel.setScientificname(scientificname);
            sortModel.setAlias(alias);

            Bitmap bitmap = BitmapFactory.decodeFile(imgurl);

            sortModel.setBitmap(bitmap);

            String sortLetters = PingYinUtil.getInstance().getSortLetterBySortKey(scientificname);
            if (sortLetters == null) {
                sortLetters = PingYinUtil.getInstance().getSortLetter(alias);
            }
            sortModel.setSortLetters(sortLetters);

            String sortKey = PingYinUtil.format(scientificname + alias);
            sortModel.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));
            sortModel.setWholeSpell(PingYinUtil.getInstance().parseSortKeyWholeSpell(sortKey));


            mGoodsList.add(sortModel);
        }
           /* Logger.i(mGoodsList.get(0).toString());
            Logger.i(mGoodsList.get(0).getBitmap().toString());
            Logger.i(mGoodsList.get(1).getBitmap().toString());
            Logger.i(mGoodsList.get(2).getBitmap().toString());
        Logger.i("" + mGoodsList.size());*/
    }


    private void initViewPagerAndTabs() {
        mPagerAdapter = new SureViewPagerAdapter(getSupportFragmentManager(),
                mUsername,mStationName,mColor,mPhotoPath1,mPhotoPath2,mPhotoPath3,mGoodsList,this);
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

        TextView title_text_view = ActionBarTool.getInstance(mActivity,991).getTitle_text_view();
        title_text_view.setText(getString(R.string.sure_goods_type));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示返回上级的箭头
        //getSupportActionBar().setDisplayShowTitleEnabled(false);//将actionbar原有的标题去掉（这句一般是用在xml方法一实现）
        mToolbarSure.setNavigationOnClickListener((v -> finish()));

    }


    @Override
    protected void onPause() {
        super.onPause();
        //存入缓存
        ACache.get(mActivity).put("goods", (ArrayList<SortModel>) mGoodsList);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

