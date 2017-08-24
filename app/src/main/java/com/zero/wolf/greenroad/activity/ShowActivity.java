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
import com.zero.wolf.greenroad.bean.PathTitleBean;
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
import com.zero.wolf.greenroad.litepalbean.SupportChecked;
import com.zero.wolf.greenroad.litepalbean.SupportDetail;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;
import com.zero.wolf.greenroad.litepalbean.SupportScan;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.PathUtil;
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
import okhttp3.MultipartBody;
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

    private static String ARG_SUPPORT_SCAN = "arg_support_scan";
    private static String ARG_SUPPORT_DETAIL = "arg_support_detail";
    private static String ARG_SUPPORT_CHECKED = "arg_support_checked";
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

    private AppCompatActivity mActivity;

    private ShowViewPagerAdapter mPagerAdapter;

    private static DetailInfoBean mDetailInfoBean_Q;
    private static ScanInfoBean mScanInfoBean_Q;


    private static CheckedBean mCheckedBean_Q;
    private static String mStation_Q;
    private static String mRoad_Q;
    private Handler mUiHandler = new Handler();


    public static void actionStart(Context context, SupportDetail supportDetail,SupportScan supportScan,SupportChecked supportChecked) {
        Intent intent = new Intent(context, ShowActivity.class);
        intent.setAction(ACTION_DRAFT_ENTER_SHOW);

        intent.putExtra(ARG_SUPPORT_DETAIL, supportDetail);
        intent.putExtra(ARG_SUPPORT_SCAN, supportScan);
        intent.putExtra(ARG_SUPPORT_CHECKED, supportChecked);

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
            SupportDetail supportDetail = intent.getParcelableExtra(ARG_SUPPORT_DETAIL);
            SupportScan supportScan = intent.getParcelableExtra(ARG_SUPPORT_SCAN);
            SupportChecked supportChecked = intent.getParcelableExtra(ARG_SUPPORT_CHECKED);

            mPagerAdapter = new ShowViewPagerAdapter(getSupportFragmentManager(), this,
                    supportDetail, supportScan,supportChecked, intent.getType());
            Logger.i(supportScan.toString());
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

        if (mScanInfoBean_Q != null) {

            save2Litepal(GlobalManager.TYPE_SUBMIT_LITE);
        } else {
            ToastUtils.singleToast("请扫描二维码");
        }

        List<PathTitleBean> path_sanzheng = mDetailInfoBean_Q.getPath_sanzheng();

        PostInfo info = new PostInfo();
        //从扫描中拿到的数据
        if (mScanInfoBean_Q != null) {
            info.setNumber(mDetailInfoBean_Q.getNumber());
            info.setColor(mDetailInfoBean_Q.getColor());
            info.setGoods(mDetailInfoBean_Q.getGoods());

            // 从主界面拿到的信息
            info.setRoad(mRoad_Q);
            info.setStation(mStation_Q);
            info.setLane((String) SPUtils.get(this, SPUtils.TEXTLANE, "66"));

            //从checkedFragment中拿到的数据
            info.setIsFree(mCheckedBean_Q.getIsFree());
            info.setIsRoom(mCheckedBean_Q.getIsRoom());
            info.setConclusion(mCheckedBean_Q.getConclusion());
            info.setDescription(mCheckedBean_Q.getDescription());
            info.setSiteCheck(mCheckedBean_Q.getSiteCheck());
            info.setSiteLogin(mCheckedBean_Q.getSiteLogin());


            //扫描的到的信息
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

        ArrayList<String> pathList = new ArrayList();
        for (int i = 0; i < mDetailInfoBean_Q.getPath_sanzheng().size(); i++) {
            pathList.add(mDetailInfoBean_Q.getPath_sanzheng().get(i).getPath());
        }

        List<MultipartBody.Part> parts = PathUtil
                .getBodyPart(pathList);

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://192.168.2.122/lvsetondao/index.php/Interfacy/Api/")
                .baseUrl("http://greenft.githubshop.com/index.php/Interfacy/Api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        HttpUtilsApi api = retrofit.create(HttpUtilsApi.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), route);

        Logger.i("json  string" + route);


        Observable<HttpResultPolling> observable = api.task(body,parts);
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
        //如果数据库中的数据已经存在了一条与当前流水号相同的则视为已经保存过的


        //   DraftActivity.actionStart(this, mConfigInfoBean, mDetailInfoBean_Q, mPhotoPaths);
//        if (supportDrafts.size() == 0) {

        //保存至本地数据库
        save2Litepal(GlobalManager.TYPE_DRAFT_LITE);
        List<SupportDraftOrSubmit> draftList = DataSupport.
                where(GlobalManager.LITE_CONDITION,GlobalManager.TYPE_DRAFT_LITE).
                find(SupportDraftOrSubmit.class);
        for (int i = 0; i < draftList.size(); i++) {
            Logger.i(draftList.get(i).toString());
        }
        Logger.i(draftList.get(0).toString());
    }

    /**
     * 将保存草稿数据或者提交网络的数据保存之后本地服务器
     */
    private void save2Litepal(String lite_type) {

        SupportDraftOrSubmit support = new SupportDraftOrSubmit();
        int count = DataSupport.count(SupportDraftOrSubmit.class);
        support.setLite_ID(count+1);
        support.setLite_type(lite_type);
        support.setCurrent_time(TimeUtil.getCurrentTimeTos());
        //保存数据到表SupportScan
        SupportScan supportScan = new SupportScan();
        supportScan.setLite_ID(count + 1);
        supportScan.setScan_code(mScanInfoBean_Q.getScan_code());
        supportScan.setScan_01Q(mScanInfoBean_Q.getScan_01Q());
        supportScan.setScan_02Q(mScanInfoBean_Q.getScan_02Q());
        supportScan.setScan_03Q(mScanInfoBean_Q.getScan_03Q());
        supportScan.setScan_04Q(mScanInfoBean_Q.getScan_04Q());
        supportScan.setScan_05Q(mScanInfoBean_Q.getScan_05Q());
        supportScan.setScan_06Q(mScanInfoBean_Q.getScan_06Q());
        supportScan.setScan_07Q(mScanInfoBean_Q.getScan_07Q());
        supportScan.setScan_08Q(mScanInfoBean_Q.getScan_08Q());
        supportScan.setScan_09Q(mScanInfoBean_Q.getScan_09Q());
        supportScan.setScan_10Q(mScanInfoBean_Q.getScan_10Q());
        supportScan.setScan_11Q(mScanInfoBean_Q.getScan_11Q());
        supportScan.setScan_12Q(mScanInfoBean_Q.getScan_12Q());
        supportScan.setScan_code(mScanInfoBean_Q.getScan_code());
        supportScan.save();

        //保存数据到表SupportDetail
        SupportDetail supportDetail = new SupportDetail();
        supportDetail.setLite_ID(count + 1);
        supportDetail.setNumber(mDetailInfoBean_Q.getNumber());
        supportDetail.setColor(mDetailInfoBean_Q.getColor());
        supportDetail.setGoods(mDetailInfoBean_Q.getGoods());
        supportDetail.setStation(mStation_Q);
        supportDetail.setRoad(mRoad_Q);
        supportDetail.setLane((String) SPUtils.get(this, SPUtils.TEXTLANE, "66"));
        supportDetail.save();

        //保存数据到表SupportChecked
        SupportChecked supportChecked = new SupportChecked();
        supportChecked.setLite_ID(count + 1);
        supportChecked.setIsFree(mCheckedBean_Q.getIsFree());
        supportChecked.setIsFree(mCheckedBean_Q.getIsRoom());
        supportChecked.setConclusion(mCheckedBean_Q.getConclusion());
        supportChecked.setDescription(mCheckedBean_Q.getDescription());
        supportChecked.setSiteCheck(mCheckedBean_Q.getSiteCheck());
        supportChecked.setSiteLogin(mCheckedBean_Q.getSiteLogin());
        supportChecked.save();

        support.setSupportScan(supportScan);
        support.setSupportDetail(supportDetail);
        support.setSupportChecked(supportChecked);

        support.save();
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
