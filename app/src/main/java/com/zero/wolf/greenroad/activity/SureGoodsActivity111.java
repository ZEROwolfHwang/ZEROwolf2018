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
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.SureCarNumberAdapter;
import com.zero.wolf.greenroad.adapter.SureCarStationAdapter;
import com.zero.wolf.greenroad.adapter.SureViewPagerAdapter;
import com.zero.wolf.greenroad.bean.SerializableNumber;
import com.zero.wolf.greenroad.bean.SerializableStation;
import com.zero.wolf.greenroad.litepalbean.SupportCarNumber;
import com.zero.wolf.greenroad.litepalbean.SupportGoods;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;
import com.zero.wolf.greenroad.litepalbean.SupportStation;
import com.zero.wolf.greenroad.smartsearch.PinyinComparator;
import com.zero.wolf.greenroad.smartsearch.SortModel;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.PingYinUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SureGoodsActivity111 extends BaseActivity {


    private List<SerializableNumber> mNumberList = new ArrayList<>();
    private List<SerializableStation> mStationList = new ArrayList<>();
    private List<SortModel> mGoodsList = new ArrayList<>();

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


    private ArrayList<SerializableNumber> mAcacheNumbers;
    private ArrayList<SerializableStation> mAcacheStations;
    private ArrayList<SortModel> mAcacheGoods;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_goods111);

        ButterKnife.bind(this);

        mActivity = this;
        mContext = this;

        initToolbar();

        initViewPagerAndTabs();

        initData();


    }

    private void initViewPagerAndTabs() {
        mPagerAdapter = new SureViewPagerAdapter(getSupportFragmentManager(),mNumberList,mStationList,mGoodsList ,this);
        mViewPagerSure.setOffscreenPageLimit(3);//设置viewpager预加载页面数
        mViewPagerSure.setAdapter(mPagerAdapter);  // 给Viewpager设置适配器
//        mViewpager.setCurrentItem(1); // 设置当前显示在哪个页面
        mTabLayoutSure.setupWithViewPager(mViewPagerSure);
    }

    public static void actionStart(Context context, String color, String username
            , String photoPath1, String photoPath2, String photoPath3) {
        Intent intent = new Intent(context, SureGoodsActivity111.class);
        intent.putExtra("username", username);
        intent.putExtra("photoPath1", photoPath1);
        intent.putExtra("photoPath2", photoPath2);
        intent.putExtra("photoPath3", photoPath3);
        intent.putExtra("color", color);

        context.startActivity(intent);
    }


    private void initData() {

        getIntentData();

        mAcacheNumbers = (ArrayList<SerializableNumber>) ACache
                .get(mActivity).getAsObject("sessions");

        mAcacheStations = (ArrayList<SerializableStation>) ACache
                .get(mActivity).getAsObject("stations");

        mAcacheGoods = (ArrayList<SortModel>) ACache
                .get(mActivity).getAsObject("goods");

        initGoodsData();
        iniNumberData();
        initStationData();


    }

    /**
     * 加载并缓存车牌号头的数据
     */
    private void iniNumberData() {
        List<SupportCarNumber> headList = DataSupport.findAll(SupportCarNumber.class);

        //如果跟数据库长度相同则不作更改，不然则更新
        if (mAcacheNumbers != null) {
            if (mAcacheNumbers.size() == headList.size()) {
                mNumberList.addAll(mAcacheNumbers);
            } else {
                //更新数据需要删除缓存
                mAcacheNumbers.clear();
                Logger.i("" + headList.size());
                addNumberData(headList);
            }
        } else {
            addNumberData(headList);
        }
    }

    /**
     * 加载收费站名的数据
     */
    private void initStationData() {
        List<SupportStation> supportStations = DataSupport.findAll(SupportStation.class);

        if (mAcacheStations != null) {
            if (mAcacheStations.size() == supportStations.size()) {
                mStationList.addAll(mAcacheStations);
            } else {
                mAcacheStations.clear();
                addStationData(supportStations);
            }
        } else {
            addStationData(supportStations);
        }
    }

    private void addNumberData(List<SupportCarNumber> headList) {
        for (int i = 0; i < headList.size(); i++) {
            String headName = headList.get(i).getHeadName();
            SerializableNumber serializableNumber = new SerializableNumber();

            serializableNumber.setName(headName);
            String sortKey = PingYinUtil.format(headName);
            serializableNumber.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));
            serializableNumber.setWholeSpell(PingYinUtil.getInstance().parseSortKeyWholeSpell(sortKey));

            mNumberList.add(serializableNumber);
        }
    }

    /**
     * 加载货物的数据及缓存
     */
    private void initGoodsData() {
        List<SupportGoods> supportGoodses = DataSupport.findAll(SupportGoods.class);

        if (mAcacheGoods != null) {
            if (mAcacheGoods.size() == supportGoodses.size()) {
                mGoodsList.addAll(mAcacheGoods);
            } else {
                mAcacheGoods.clear();
                addGoodsData(supportGoodses);
            }
        } else {
            addGoodsData(supportGoodses);
        }
    }


    /**
     * 得到从上一个activity中拿到的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        mColor = intent.getStringExtra("color");
        mPhotoPath1 = intent.getStringExtra("photoPath1");
        mPhotoPath2 = intent.getStringExtra("photoPath3");
        mPhotoPath3 = intent.getStringExtra("photoPath3");
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
    }


    /**
     * 填充station的数据
     *
     * @param supportStations
     */
    private void addStationData(List<SupportStation> supportStations) {
        for (int i = 0; i < supportStations.size(); i++) {

            String stationName = supportStations.get(i).getStationName();

            SerializableStation serializableStation = new SerializableStation();

            serializableStation.setStationName(stationName);

            String sortKey = PingYinUtil.format(stationName);
            serializableStation.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));
            serializableStation.setWholeSpell(PingYinUtil.getInstance().parseSortKeyWholeSpell(sortKey));

            mStationList.add(serializableStation);
        }
    }



    private void initListener() {
        editFocusListener();
        editAddTextListener();

    }

    private void editAddTextListener() {

    }

    private void editFocusListener() {

    }

    /**
     * 保存到本地数据库
     *
     * @param currentTime
     */
    private void saveLocalLite(String currentTime) {
        SupportPhotoLite supportPhotoLite = new SupportPhotoLite();
        supportPhotoLite.setShuttime(currentTime);
        supportPhotoLite.setUsername(mUsername);
        supportPhotoLite.setGoods(mCar_goods);
        supportPhotoLite.setLicense_plate(mLicense_plate);
        supportPhotoLite.setStation(mCar_station);
        supportPhotoLite.setPhotoPath1(mPhotoPath1);
        supportPhotoLite.setPhotoPath2(mPhotoPath2);
        supportPhotoLite.setPhotoPath3(mPhotoPath3);
        supportPhotoLite.setLicense_color(mColor);
        supportPhotoLite.setCar_type("绿皮车");
        supportPhotoLite.save();
    }


    private void refreshView(int stype) {
        /*if (stype == 001) {
            Collections.sort(mNumberList);
            mNumberAdapter.notifyDataSetChanged();
        }
        */
        if (stype == 001) {
            Collections.sort(mNumberList);
            mNumberAdapter.updateListView(mNumberList);
        }

        if (stype == 002) {
            Collections.sort(mStationList);
            mStationAdapter.updateListView(mStationList);
        }

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
        ACache.get(mActivity).put("sessions", (ArrayList<SerializableNumber>) mNumberList);
        ACache.get(mActivity).put("stations", (ArrayList<SerializableStation>) mStationList);
        ACache.get(mActivity).put("goods", (ArrayList<SortModel>) mGoodsList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

