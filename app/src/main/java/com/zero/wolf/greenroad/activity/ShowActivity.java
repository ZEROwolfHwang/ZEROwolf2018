package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.ShowViewPagerAdapter;
import com.zero.wolf.greenroad.bean.CheckedBean;
import com.zero.wolf.greenroad.bean.DetailInfoBean;
import com.zero.wolf.greenroad.bean.ScanInfoBean;
import com.zero.wolf.greenroad.fragment.CarNumberFragment;
import com.zero.wolf.greenroad.fragment.CheckedFragment;
import com.zero.wolf.greenroad.fragment.DetailsFragment;
import com.zero.wolf.greenroad.fragment.GoodsFragment;
import com.zero.wolf.greenroad.fragment.PhotoFragment;
import com.zero.wolf.greenroad.fragment.ScanFragment;
import com.zero.wolf.greenroad.httpresultbean.HttpResultPolling;
import com.zero.wolf.greenroad.https.HttpUtilsApi;
import com.zero.wolf.greenroad.https.PostInfo;
import com.zero.wolf.greenroad.litepalbean.SupportDraft;
import com.zero.wolf.greenroad.litepalbean.SupportSubmit;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.PermissionUtils;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ShowActivity extends BaseActivity {

    private static int SAVE_TO_DRAFT = 998;
    private static int SAVE_TO_SUBMIT = 999;

    private static String ARG_SCANINFOBEAN = "arg_scaninfobean";
    private static String ARG_STATION = "arg_station";

    private static String ACTION_MAIN_ENTER_SHOW = "action_main_enter_show";
    public static String TYPE_MAIN_ENTER_SHOW = "type_main_enter_show";

    private static String ACTION_DRAFT_ENTER_SHOW = "action_draft_enter_show";
    public static String TYPE_DRAFT_ENTER_SHOW = "type_draft_enter_show";
    @BindView(R.id.tab_show)
    TabLayout mTabShow;
    @BindView(R.id.view_pager_show)
    ViewPager mViewPagerShow;
    @BindView(R.id.toolbar_show)
    Toolbar mToolbarShow;
    @BindView(R.id.fab_draft)
    FloatingActionButton mFabDraft;
    @BindView(R.id.fab_submit)
    FloatingActionButton mFabSubmit;
    @BindView(R.id.menu_fab)
    FloatingActionMenu mMenuFab;


    private long firstClick;
    private static final String TAG = "MainActivity";
    private static final int REQ_0 = 001;

    private AppCompatActivity mActivity;

    private AlertDialog.Builder mDialog;

    private AlertDialog.Builder mNotPostDialog;


    private int thumb_margin_left_day = 0;
    private int thumb_margin_left_night = 0;

    private ShowViewPagerAdapter mPagerAdapter;
    private String mLogin_operator_P;
    private String mCheck_operator_P;
    private static DetailInfoBean mDetailInfoBean_Q;
    private static ScanInfoBean mScanInfoBean_Q;
    private ArrayList<String> mPhotoPaths;

    private static CheckedBean mCheckedBean_Q;
    private static String mStation_Q;
    private static String mRoad_Q;
    private Handler mUiHandler = new Handler();
    private Intent mData;


    public static void actionStart(Context context, ScanInfoBean scanInfoBean) {
        Intent intent = new Intent(context, ShowActivity.class);
        intent.setAction(ACTION_DRAFT_ENTER_SHOW);
        intent.putExtra(ARG_SCANINFOBEAN, scanInfoBean);
        intent.setType(TYPE_DRAFT_ENTER_SHOW);
        context.startActivity(intent);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShowActivity.class);
        intent.setAction(ACTION_MAIN_ENTER_SHOW);
        intent.setType(TYPE_MAIN_ENTER_SHOW);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        ButterKnife.bind(this);
        mActivity = this;
        initFab();
        initData();

        getIntentData();

        initViewPagerAndTabs();

        initView();


    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (ACTION_DRAFT_ENTER_SHOW.equals(intent.getAction())) {
            ScanInfoBean scanInfoBean = (ScanInfoBean) intent.getSerializableExtra(ARG_SCANINFOBEAN);
            mPagerAdapter = new ShowViewPagerAdapter(getSupportFragmentManager(), this, scanInfoBean, intent.getType());
            Logger.i(scanInfoBean.toString());
        } else if (ACTION_MAIN_ENTER_SHOW.equals(intent.getAction())) {
            mPagerAdapter = new ShowViewPagerAdapter(getSupportFragmentManager(), this, intent.getType());
        }
        mViewPagerShow.setOffscreenPageLimit(3);//设置viewpager预加载页面数
        mViewPagerShow.setAdapter(mPagerAdapter);  // 给Viewpager设置适配器
//        mViewpager.setCurrentItem(1); // 设置当前显示在哪个页面
        mTabShow.setupWithViewPager(mViewPagerShow);
    }

    private void initFab() {
        mMenuFab.setClosedOnTouchOutside(true);

        mMenuFab.hideMenuButton(false);
        int delay = 400;
        mUiHandler.postDelayed(() -> {
                    mMenuFab.showMenuButton(true);
                }
                , delay);
        mMenuFab.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mMenuFab.isOpened()) {
                    mFabDraft.showButtonInMenu(true);
                    mFabSubmit.showButtonInMenu(true);
                }

                mMenuFab.toggle(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> strListValue = SPListUtil.getStrListValue(getContext(), SPListUtil.APPCONFIGINFO);
        for (int i = 0; i < strListValue.size(); i++) {
            String string = strListValue.get(i).toString();
            Logger.i(string);
        }
        mRoad_Q = strListValue.get(1).toString();
        mStation_Q = strListValue.get(2).toString();
    }

    private void initData() {
        PermissionUtils.verifyStoragePermissions(mActivity);

    }

    private void initViewPagerAndTabs() {


    }


    private void initView() {

        setSupportActionBar(mToolbarShow);

        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText("车辆采集");

        mToolbarShow.setNavigationIcon(R.drawable.back_up_logo);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);//将actionbar原有的标题去掉（这句一般是用在xml方法一实现）
        mToolbarShow.setNavigationOnClickListener(v -> backToMain());

    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_draft:
                ToastUtils.singleToast("实现保存草稿");
                saveDraft();

                break;
            case R.id.menu_submit:
                ToastUtils.singleToast("向服务端提交采集的数据");
                submit2Service();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @OnClick({R.id.fab_submit, R.id.fab_draft})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fab_submit:
                ToastUtils.singleToast("向服务端提交采集的数据");
                mFabDraft.hideButtonInMenu(true);
                mFabSubmit.hideButtonInMenu(true);
                mMenuFab.toggle(false);
                submit2Service();
                break;
            case R.id.fab_draft:
                ToastUtils.singleToast("实现保存草稿");
                mFabDraft.hideButtonInMenu(true);
                mFabSubmit.hideButtonInMenu(true);
                mMenuFab.toggle(false);
                saveDraft();
                break;

            default:
                break;
        }
    }

    private void submit2Service() {
        getListenerData();

        save2Litepal(SAVE_TO_SUBMIT);

        PostInfo info = new PostInfo();
        //从扫描中拿到的数据
        if (mScanInfoBean_Q != null) {

            info.setScan_code(mScanInfoBean_Q.getScan_code());
            info.setScan_01Q(mScanInfoBean_Q.getScan_01Q());
            info.setScan_02Q(mScanInfoBean_Q.getScan_02Q());
            info.setScan_03Q(mScanInfoBean_Q.getScan_03Q());
            info.setScan_04Q(mScanInfoBean_Q.getScan_04Q());
            info.setScan_05Q(mScanInfoBean_Q.getScan_05Q());
            info.setScan_06Q(mScanInfoBean_Q.getScan_06Q());
            info.setScan_07Q(mScanInfoBean_Q.getScan_07Q());
            info.setScan_08Q(mScanInfoBean_Q.getScan_08Q());
            info.setScan_09Q(mScanInfoBean_Q.getScan_09Q());
            info.setScan_10Q(mScanInfoBean_Q.getScan_10Q());
            info.setScan_11Q(mScanInfoBean_Q.getScan_11Q());
            info.setScan_12Q(mScanInfoBean_Q.getScan_12Q());
            info.setScan_code(mScanInfoBean_Q.getScan_code());
            info.setCurrent_time(TimeUtil.getCurrentTimeTos());

        } else {
            ToastUtils.singleToast("请扫描二维码");
        }

        Gson gson = new Gson();
        String route = gson.toJson(info);


        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://192.168.2.122/lvsetondao/index.php/Interfacy/Api/")
                .baseUrl("http://greenft.githubshop.com/index.php/Interfacy/Api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        HttpUtilsApi api = retrofit.create(HttpUtilsApi.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), route);

        Logger.i("json  string" + route);


        Observable<HttpResultPolling> observable = api.task(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultPolling>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                        ToastUtils.singleToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultPolling httpResultPolling) {
                        int code = httpResultPolling.getCode();
                        Logger.i(code + "");
                        ToastUtils.singleToast(code + "");
                    }
                });
    }

    /**
     * 拿到两个fragment中被监听的数据
     * 在提交至服务器以及保存草稿中都要用到
     */
    private void getListenerData() {

        DetailsFragment.setDetailsConnectListener((bean) -> {
            mDetailInfoBean_Q = bean;


            //拿到图片的体进行网络传递
           /* if (mBitmapList != null && mBitmapList.size() != 0) {
                List<MultipartBody.Part> parts = PathUtil
                        .getBodyPart(mBitmapList);
                Logger.i(mDetailInfoBean_Q.toString());
                Logger.i(parts.toString());
            }*/
        });
        //拿到checkFragment的数据
        CheckedFragment.setCheckedBeanConnectListener(bean -> {
            mCheckedBean_Q = bean;
            Logger.i(mCheckedBean_Q.toString());
        });

        ScanFragment.setScanConnectListener(bean -> {
            mScanInfoBean_Q = bean;
            Logger.i(mScanInfoBean_Q.toString());
        });

    }

    /**
     * 保存草稿
     * 1.进入草稿的activity并保存
     * 2.将数据保存到数据库
     */
    private void saveDraft() {
        getListenerData();

        if (mScanInfoBean_Q == null || "".equals(mScanInfoBean_Q.getScan_code())) {
            ToastUtils.singleToast("请扫描二维码得到更多详细信息后保存");
            return;
        }
        Logger.i(mScanInfoBean_Q.getScan_code());
        Logger.i(mScanInfoBean_Q.toString());

        //如果数据库中的数据已经存在了一条与当前流水号相同的则视为已经保存过的

        List<SupportDraft> supportDrafts = DataSupport.where("scan_code = ?",
                mScanInfoBean_Q.getScan_code()).find(SupportDraft.class);

        //   DraftActivity.actionStart(this, mConfigInfoBean, mDetailInfoBean_Q, mPhotoPaths);
//        if (supportDrafts.size() == 0) {

        //保存至本地数据库
        save2Litepal(SAVE_TO_DRAFT);
        List<SupportDraft> draftList = DataSupport.findAll(SupportDraft.class);
        for (int i = 0; i < draftList.size(); i++) {
            Logger.i(draftList.get(i).toString());
        }
        Logger.i(draftList.get(0).toString());
    }

    /**
     * 将保存草稿数据或者提交网络的数据保存之后本地服务器
     */
    private void save2Litepal(int type) {
        if (type == SAVE_TO_DRAFT) {

           /* PhotoFragment.setSelectedListListener(data -> {
                mData = data;
                Logger.i("data-------" + data);
                List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(mData);
                SupportMedia supportMedia = new SupportMedia();
                supportMedia.setChecked(localMedias.get(0).isChecked());
                supportMedia.setCompressed(localMedias.get(0).isCompressed());
                supportMedia.setCompressPath(localMedias.get(0).getCompressPath());
                supportMedia.setPath(localMedias.get(0).getPath());
                supportMedia.save();
                Logger.i(supportMedia.toString());

            });*/

            SupportDraft support = new SupportDraft();

            //从采集的fragment中拿到数据

            support.setPath_sanzheng(mDetailInfoBean_Q.getPath_sanzheng());
            support.setPath_cheshen(mDetailInfoBean_Q.getPath_cheshen());
            support.setPath_huowu(mDetailInfoBean_Q.getPath_huowu());
            support.setNumber(mDetailInfoBean_Q.getNumber());
            support.setColor(mDetailInfoBean_Q.getColor());
            support.setGoods(mDetailInfoBean_Q.getGoods());

            // 从主界面拿到的信息
            support.setRoad(mRoad_Q);
            support.setStation(mStation_Q);
            support.setLane((String) SPUtils.get(this, SPUtils.TEXTLANE, "66"));

            //从扫描中拿到的数据
            support.setScan_01Q(mScanInfoBean_Q.getScan_01Q());
            support.setScan_02Q(mScanInfoBean_Q.getScan_02Q());
            support.setScan_03Q(mScanInfoBean_Q.getScan_03Q());
            support.setScan_04Q(mScanInfoBean_Q.getScan_04Q());
            support.setScan_05Q(mScanInfoBean_Q.getScan_05Q());
            support.setScan_06Q(mScanInfoBean_Q.getScan_06Q());
            support.setScan_07Q(mScanInfoBean_Q.getScan_07Q());
            support.setScan_08Q(mScanInfoBean_Q.getScan_08Q());
            support.setScan_09Q(mScanInfoBean_Q.getScan_09Q());
            support.setScan_10Q(mScanInfoBean_Q.getScan_10Q());
            support.setScan_11Q(mScanInfoBean_Q.getScan_11Q());
            support.setScan_12Q(mScanInfoBean_Q.getScan_12Q());
            support.setScan_code(mScanInfoBean_Q.getScan_code());
            support.setCurrent_time(TimeUtil.getCurrentTimeTos());

            //从checkedFragment中拿到的数据
            support.setIsFree(mCheckedBean_Q.getIsFree());
            support.setIsRoom(mCheckedBean_Q.getIsRoom());
            support.setConclusion(mCheckedBean_Q.getConclusion());
            support.setDescription(mCheckedBean_Q.getDescription());
            support.setSiteCheck(mCheckedBean_Q.getSiteCheck());
            support.setSiteLogin(mCheckedBean_Q.getSiteLogin());

            support.save();
        } else if (type == SAVE_TO_SUBMIT) {
            SupportSubmit support = new SupportSubmit();

            //从采集的fragment中拿到数据
            support.setPath_sanzheng(mDetailInfoBean_Q.getPath_sanzheng());
            support.setPath_cheshen(mDetailInfoBean_Q.getPath_cheshen());
            support.setPath_huowu(mDetailInfoBean_Q.getPath_huowu());
            support.setNumber(mDetailInfoBean_Q.getNumber());
            support.setColor(mDetailInfoBean_Q.getColor());
            support.setGoods(mDetailInfoBean_Q.getGoods());

            // 从主界面拿到的信息
            support.setRoad(mRoad_Q);
            support.setStation(mStation_Q);
            support.setLane((String) SPUtils.get(this, SPUtils.TEXTLANE, "66"));

            //从扫描中拿到的数据

            support.setScanbean(mScanInfoBean_Q);
            support.setCurrent_time(TimeUtil.getCurrentTimeTos());

            //从checkedFragment中拿到的数据
            support.setIsFree(mCheckedBean_Q.getIsFree());
            support.setIsRoom(mCheckedBean_Q.getIsRoom());
            support.setConclusion(mCheckedBean_Q.getConclusion());
            support.setDescription(mCheckedBean_Q.getDescription());
            support.setSiteCheck(mCheckedBean_Q.getSiteCheck());
            support.setSiteLogin(mCheckedBean_Q.getSiteLogin());

            support.save();

        }
        List<SupportSubmit> submits = DataSupport.findAll(SupportSubmit.class);
        for (int i = 0; i < submits.size(); i++) {
            Logger.i(submits.get(i).getScanbean().toString());
        }
    }

    @Override
    public void onBackPressed() {
        backToMain();
    }

    private void backToMain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("是否保存为草稿");
        builder.setMessage("点击确定保存为草稿并退出\n点击直接退出则清空当前采集");
        builder.setNegativeButton("直接退出", (dialog, which) -> {
            dialog.dismiss();
            notifyDataChangeAndFinish();

        });

        builder.setPositiveButton("保存并退出", (dialog, which) -> {
            saveDraft();
            //在这里做保存草稿的操作
            notifyDataChangeAndFinish();
        });
        builder.show();
    }

    private void notifyDataChangeAndFinish() {
        CarNumberFragment.notifyDataChange();
        GoodsFragment.notifyDataChange();
        PhotoFragment.newInstance().notifyDataChange();
        ConclusionActivity.notifyDataChange();

        mActivity.finish();
    }
}
