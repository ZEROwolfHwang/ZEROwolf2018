package com.zero.wolf.greenroad.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.ShowViewPagerAdapter;
import com.zero.wolf.greenroad.bean.CheckedBean;
import com.zero.wolf.greenroad.bean.ScanInfoBean;
import com.zero.wolf.greenroad.bean.DetailInfoBean;
import com.zero.wolf.greenroad.fragment.CheckedFragment;
import com.zero.wolf.greenroad.fragment.DetailsFragment;
import com.zero.wolf.greenroad.fragment.ScanFragment;
import com.zero.wolf.greenroad.httpresultbean.HttpResultPolling;
import com.zero.wolf.greenroad.https.HttpUtilsApi;
import com.zero.wolf.greenroad.https.PostInfo;
import com.zero.wolf.greenroad.litepalbean.SupportDraft;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.PathUtil;
import com.zero.wolf.greenroad.tools.PermissionUtils;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.SnackbarUtils;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class ShowActivity extends BaseActivity{


    @BindView(R.id.tab_show)
    TabLayout mTabShow;
    @BindView(R.id.view_pager_show)
    ViewPager mViewPagerShow;
    @BindView(R.id.toolbar_show)
    Toolbar mToolbarShow;
    @BindView(R.id.show_submit)
    FloatingActionButton mShowSubmit;
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
    private DetailInfoBean mDetailInfoBean_Q;
    private static ScanInfoBean mScanInfoBean_Q;
    private ArrayList<String> mPhotoPaths;
    private List<String> mBitmapList;
    private CheckedBean mCheckedBean_Q;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        ButterKnife.bind(this);
        mActivity = this;
        PermissionUtils.verifyStoragePermissions(mActivity);

        List app_config_info = new ArrayList<String>();

        app_config_info.add("163127841234");
        app_config_info.add("西部沿海");
        app_config_info.add("广海");

        Logger.i(app_config_info.toString());
        SPListUtil.putStrListValue(this, SPListUtil.APPCONFIGINFO, app_config_info);

        initViewPagerAndTabs();
        initData();
        initView();


    }

    private void initData() {

    }
    private void initViewPagerAndTabs() {
        mPagerAdapter = new ShowViewPagerAdapter(getSupportFragmentManager(), this);
        mViewPagerShow.setOffscreenPageLimit(3);//设置viewpager预加载页面数
        mViewPagerShow.setAdapter(mPagerAdapter);  // 给Viewpager设置适配器
//        mViewpager.setCurrentItem(1); // 设置当前显示在哪个页面
        mTabShow.setupWithViewPager(mViewPagerShow);
    }



    private void initView() {

        setSupportActionBar(mToolbarShow);

        TextView title_text_view = ActionBarTool.getInstance(mActivity, 990).getTitle_text_view();
        title_text_view.setText("车辆采集");

        mToolbarShow.setNavigationIcon(R.drawable.back_up_logo);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);//将actionbar原有的标题去掉（这句一般是用在xml方法一实现）
        mToolbarShow.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.caogao:
                ToastUtils.singleToast("实现保存草稿");
                saveDraft();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.show_submit)
    public void onClick(View view) {
        SnackbarUtils.showShortSnackbar(view, "提交信息", Color.GREEN, Color.BLUE);
        submit2Service();
    }

    private void submit2Service() {
        getListenerData();


        PostInfo info = new PostInfo();
        info.setCheck("苏三");
        info.setLogin("璟四");
        Gson gson = new Gson();
        String route = gson.toJson(info);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.122/lvsetondao/index.php/Interfacy/Api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        HttpUtilsApi api = retrofit.create(HttpUtilsApi.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), route);

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
                    }

                    @Override
                    public void onNext(HttpResultPolling httpResultPolling) {
                        int code = httpResultPolling.getCode();
                        Logger.i(code + "");
                    }
                });
    }

    /**
     * 拿到两个fragment中被监听的数据
     * 在提交至服务器以及保存草稿中都要用到
     */
    private void getListenerData() {

        DetailsFragment.newInstance().setDetailsConnectListener((bean) -> {
            mDetailInfoBean_Q = bean;
            mBitmapList = mDetailInfoBean_Q.getBitmapPaths();
            //拿到图片的体进行网络传递
            if (mBitmapList != null && mBitmapList.size() != 0) {
                List<MultipartBody.Part> parts = PathUtil
                        .getBodyPart(mBitmapList);
                Logger.i(mDetailInfoBean_Q.toString());
                Logger.i(parts.toString());
            }
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
            ToastUtils.singleToast("请扫描二维码得到更多详细信息后保存111");
            return;
        }
        Logger.i(mScanInfoBean_Q.getScan_code());
        Logger.i(mScanInfoBean_Q.toString());
        List<SupportDraft> supportDrafts = DataSupport.where("scan_code = ?",
                mScanInfoBean_Q.getScan_code()).find(SupportDraft.class);

        //   DraftActivity.actionStart(this, mConfigInfoBean, mDetailInfoBean_Q, mPhotoPaths);
//        if (supportDrafts.size() == 0) {

        SupportDraft draft = new SupportDraft();

        //从采集的fragment中拿到数据
        draft.setBitmapPaths(mDetailInfoBean_Q.getBitmapPaths());
        draft.setNumber(mDetailInfoBean_Q.getNumber());
        draft.setColor(mDetailInfoBean_Q.getColor());
        draft.setGoods(mDetailInfoBean_Q.getGoods());

        // 从主界面拿到的信息
//        draft.setRoad(mScanInfoBean_Q.getRoad());
//        draft.setStation(mScanInfoBean_Q.getStation());
//        draft.setLane(mScanInfoBean_Q.getLane());

        //从扫描中拿到的数据
        draft.setScan_01Q(mScanInfoBean_Q.getScan_01Q());
        draft.setScan_02Q(mScanInfoBean_Q.getScan_02Q());
        draft.setScan_03Q(mScanInfoBean_Q.getScan_03Q());
        draft.setScan_04Q(mScanInfoBean_Q.getScan_04Q());
        draft.setScan_05Q(mScanInfoBean_Q.getScan_05Q());
        draft.setScan_06Q(mScanInfoBean_Q.getScan_06Q());
        draft.setScan_07Q(mScanInfoBean_Q.getScan_07Q());
        draft.setScan_08Q(mScanInfoBean_Q.getScan_08Q());
        draft.setScan_09Q(mScanInfoBean_Q.getScan_09Q());
        draft.setScan_10Q(mScanInfoBean_Q.getScan_10Q());
        draft.setScan_11Q(mScanInfoBean_Q.getScan_11Q());
        draft.setScan_12Q(mScanInfoBean_Q.getScan_12Q());
        draft.setScan_code(mScanInfoBean_Q.getScan_code());
        draft.setDraftTime(TimeUtil.getCurrentTimeTos());

        //从checkedFragment中拿到的数据
        draft.setIsFree(mCheckedBean_Q.getIsFree());
        draft.setIsRoom(mCheckedBean_Q.getIsRoom());
        draft.setConclusion(mCheckedBean_Q.getConclusion());
        draft.setDescription(mCheckedBean_Q.getDescription());
        draft.setSiteCheck(mCheckedBean_Q.getSiteCheck());
        draft.setSiteLogin(mCheckedBean_Q.getSiteLogin());

        draft.save();
        List<SupportDraft> draftList = DataSupport.findAll(SupportDraft.class);
        for (int i = 0; i < draftList.size(); i++) {
            Logger.i(draftList.get(i).toString());
        }
    }

}
