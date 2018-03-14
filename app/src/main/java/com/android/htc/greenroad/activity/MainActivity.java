package com.android.htc.greenroad.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.htc.greenroad.R;
import com.android.htc.greenroad.bean.UpdateAppInfo;
import com.android.htc.greenroad.httpresultbean.HttpResultGoods;
import com.android.htc.greenroad.https.RequestGoods;
import com.android.htc.greenroad.litepalbean.SupportDraftOrSubmit;
import com.android.htc.greenroad.litepalbean.SupportGoods;
import com.android.htc.greenroad.litepalbean.SupportLocalGoods;
import com.android.htc.greenroad.litepalbean.SupportOperator;
import com.android.htc.greenroad.litepalbean.TeamItem;
import com.android.htc.greenroad.manager.GlobalManager;
import com.android.htc.greenroad.servicy.BlackListService;
import com.android.htc.greenroad.servicy.PollingService;
import com.android.htc.greenroad.servicy.SubmitIntentService;
import com.android.htc.greenroad.tools.ActionBarTool;
import com.android.htc.greenroad.tools.DevicesInfoUtils;
import com.android.htc.greenroad.tools.ImageUtils;
import com.android.htc.greenroad.tools.SDcardSpace;
import com.android.htc.greenroad.tools.SPUtils;
import com.android.htc.greenroad.tools.ToastUtils;
import com.android.htc.greenroad.update.AppInnerDownLoder;
import com.android.htc.greenroad.update.CheckUpdateUtils;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/6/20.
 */

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    File mFile = null;
    @BindView(R.id.rl_progress_login)
    RelativeLayout mRlProgressLogin;
    @BindView(R.id.ll_main_contain)
    LinearLayout mLlMainContain;
    private String mFilePath_str;
    private static final String USERNAME = "username";
    @BindView(R.id.toolbar_main)
    Toolbar mToolbarMain;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.tv_change_station_main)
    TextView mTvChangeStationMain;
    @BindView(R.id.tv_change_lane_main)
    TextView mTvChangeLaneMain;
    @BindView(R.id.tv_operator_check_main)
    TextView mTvOperatorCheckMain;
    @BindView(R.id.tv_operator_login_main)
    TextView mTvOperatorLoginMain;
    @BindView(R.id.rl_main_draft)
    LinearLayout mRlMainDraft;
    @BindView(R.id.rl_main_submit)
    LinearLayout mRlMainSubmit;
    @BindView(R.id.tv_math_number_draft)
    TextView mTvMathNumberDraft;

    private static TextView mTvMathNumberSubmit;
    @BindView(R.id.tv_avail_space)
    TextView mTvAvailSpace;
    @BindView(R.id.tv_all_space)
    TextView mTvAllSpace;
    @BindView(R.id.tv_math_number_blacklist)
    TextView mTvMathNumberBlacklist;
    @BindView(R.id.rl_main_blacklist)
    LinearLayout mRlMainBlacklist;

    private static final int REQ_0 = 001;


    private AppCompatActivity mActivity;


    private AlertDialog.Builder mDialog;


    private AlertDialog.Builder mNotPostDialog;


    private int thumb_margin_left_day = 0;
    private int thumb_margin_left_night = 0;


    private String mAvailSpace;
    private String mAllSpace;
    private MyBroadCaseReceiver mBroadCaseReceiver;
    private ArrayList<String> mLaneList;
    private String mStation;
    private String mUsername;
    private long firstClick;
    private Handler mHandler = new Handler(msg -> {
        switch (msg.what) {
            case 1:
//                ToastUtils.singleToast("完成图片下载");
                Logger.i("完成图片下载");
                mRlProgressLogin.setVisibility(View.GONE);
                mLlMainContain.setVisibility(View.VISIBLE);
                ToastUtils.singleToast("货物信息更新完成");
                break;
            case 2:
//                ToastUtils.singleToast("未加载图片数据直接进入主界面");
                Logger.i("未加载图片数据直接进入主界面");
                mRlProgressLogin.setVisibility(View.GONE);
                mLlMainContain.setVisibility(View.VISIBLE);
                break;

        }
        return false;
    });

    public static void actionStart(Context context, String enterMainType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setType(enterMainType);
        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mActivity = this;

        startPollingService();

        mUsername = (String) SPUtils.get(this, GlobalManager.USERNAME, "qqqq");
        //为了拿到NavigationView中的子View,好做修改
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView usernameText = (TextView) headView.findViewById(R.id.text_username);
        usernameText.setText(mUsername);

//        TextView textView = (TextView) findViewById(R.id.text_username);
//        textView.setText(mUsername);

        mTvOperatorCheckMain = (TextView) findViewById(R.id.tv_operator_check_main);
        mTvOperatorLoginMain = (TextView) findViewById(R.id.tv_operator_login_main);
        mTvMathNumberSubmit = (TextView) findViewById(R.id.tv_math_number_submit);

        mTvChangeLaneMain.setOnClickListener(v -> openSettingActivity());
        mTvOperatorCheckMain.setOnClickListener(v -> openSettingActivity());
        mTvOperatorLoginMain.setOnClickListener(v -> openSettingActivity());

        initSpace();

        initData();
        //initSp();
        initView();
    }

    @OnClick({R.id.rl_main_draft, R.id.rl_main_submit, R.id.rl_main_blacklist, R.id.btn_enter_show})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_main_draft:
                Intent intent = new Intent(MainActivity.this, DraftActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_main_submit:
                Intent intent1 = new Intent(MainActivity.this, SubmitActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_main_blacklist:
                Intent intent2 = new Intent(MainActivity.this, BlackListActivity.class);
                startActivity(intent2);

                break;

            case R.id.btn_enter_show:
                List<SupportOperator> check = DataSupport.where("check_select=? and username = ?", "1", mUsername).
                        find(SupportOperator.class);

                if (check.size() >= 1) {
                    ShowActivity.actionStart(MainActivity.this);
                    MainActivity.this.finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("确定选择默认检查人");
                    builder.setMessage("点击确定进入设置界面添加默认操作员;\n" +
                            "点击取消则回到界面,不可以采集车辆数据");
                    builder.setPositiveButton("确定", (dialog, which) -> {
                        openSettingActivity();
                    });
                    builder.setNegativeButton("取消", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    builder.show();
//                    ToastUtils.singleToast("目前请添加操作员");
                    break;
                }
                break;

            default:
                break;
        }
    }

    private void initSpace() {

        SDcardSpace sDcardSpace = new SDcardSpace(mActivity);
        mAvailSpace = sDcardSpace.getAvailSpace();
        // mAllSpace = sDcardSpace.getAllSpace();
        mAllSpace = sDcardSpace.getSDTotalSize(mActivity);


        mTvAllSpace.setText(" / " + mAllSpace);
        mTvAvailSpace.setText(mAvailSpace + "");

    }

    private void initData() {
        // TODO: 2018/2/4 拿到登录的班次的信息
        initGoodData(mUsername);
        initConfigView();



//        List<String> strListValue = SPListUtil.getStrListValue(MainActivity.this, SPListUtil.APPCONFIGINFO);
//        if (strListValue == null || strListValue.size() != 3) {
//            RequestMacInfo.getInstance().getMacInfo(new Subscriber<HttpResultMacInfo>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Logger.i(e.getMessage());
//                    SPListUtil.putStrListValue(MainActivity.this, SPListUtil.APPCONFIGINFO, null);
//                }
//
//                @Override
//                public void onNext(HttpResultMacInfo httpResultMacInfo) {
//                    int code = httpResultMacInfo.getCode();
//                    Logger.i(code + "");
//                    HttpResultMacInfo.DataBean dataBean = httpResultMacInfo.getData();
//                    Logger.i(dataBean.toString());
//                    if (code == 200) {
//                        List app_config_info = new ArrayList<String>();
//
//                        app_config_info.add(dataBean.getName());
//                        app_config_info.add(dataBean.getTerminal_road());
//                        app_config_info.add(dataBean.getTerminal_site());
//
//                        Logger.i(app_config_info.toString());
//                        mTvChangeStationMain.setText(dataBean.getTerminal_road());
//                        SPListUtil.putStrListValue(MainActivity.this, SPListUtil.APPCONFIGINFO, app_config_info);
//
//                        List<SupportLane> laneList = DataSupport.findAll(SupportLane.class);
//                        if (laneList.size() == 0) {
//                            RequestLane.getInstance().getLanes(new Subscriber<List<HttpResultLane.DataBean>>() {
//                                @Override
//                                public void onCompleted() {
//
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Logger.i(e.getMessage());
//                                }
//
//                                @Override
//                                public void onNext(List<HttpResultLane.DataBean> dataBeen) {
//
//                                    Logger.i(dataBeen.size() + "");
//                                    if (dataBeen != null && dataBeen.size() != 0) {
//                                        DataSupport.deleteAll(SupportLane.class);
//                                        for (int i = 0; i < dataBeen.size(); i++) {
//                                        SupportLane supportLane = new SupportLane();
//                                        supportLane.setLane(dataBeen.get(i).getLane());
//                                        supportLane.save();
//                                            Logger.i(dataBeen.get(i).getLane());
//                                        }
//                                        SPUtils.putAndApply(mActivity, SPUtils.TEXTLANE, dataBeen.get(0).getLane());
//                                        mTvChangeLaneMain.setText(dataBeen.get(0).getLane());
//                                    }
//                                }
//                            }, dataBean.getTerminal_site());
//                        }
//                    } else {
//                        SPListUtil.putStrListValue(MainActivity.this, SPListUtil.APPCONFIGINFO, null);
//                    }
//                }
//            }, mUserName);
//        }
//        List<String> new_ListValue = SPListUtil.getStrListValue(MainActivity.this, SPListUtil.APPCONFIGINFO);
//        for (int i = 0; i < new_ListValue.size(); i++) {
//            String string = new_ListValue.get(i).toString();
//            Logger.i(string);
//        }


    }

    private void initConfigView() {
        if (GlobalManager.LOGINTOMAIN.equals(getIntent().getType())) {
            Logger.i("加载了黑名单列表");
            BlackListService.startActionBlack(this, mTvMathNumberBlacklist);
        }
        Logger.i(SPUtils.get(this, SPUtils.COUNT_BLACKLIST, 0) + "");
        mTvMathNumberBlacklist.setText(SPUtils.get(this, SPUtils.COUNT_BLACKLIST, 0) + "");


//
//        mTvChangeLaneMain.setText(teamItems.get(0).getLanes().get(0));

    }

    private void initView() {
        setSupportActionBar(mToolbarMain);
        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText("绿通车登记");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_about) {
            navAbout();
        } else if (id == R.id.nav_update) {
            Logger.i("点击了更新按钮");
            updateApp();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_config) {
            Intent intent = new Intent(this, ConfigActivity.class);
            startActivity(intent);
        }

//        else if (id == R.id.nav_theme) {
//            changeTheme();
//        }
        else if (id == R.id.nav_backup) {
            buckUpApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 点击主题菜单按钮改变App的主题
     * /**
     * 切换主题时展示动画
     */

    private void changeTheme() {

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(240);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = new Intent(getContext(), AnimatorActivity.class);
                MainActivity.this.finish();
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        animatorSet.play(obtainCheckboxAnimator());

        animatorSet.start();
    }


    private Animator obtainCheckboxAnimator() {
        int start = getThemeTag() == -1 ? thumb_margin_left_night : thumb_margin_left_day;
        int end = getThemeTag() == -1 ? thumb_margin_left_day : thumb_margin_left_night;
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        return animator;
    }

    /**
     * 退出程序
     */
    private void buckUpApp() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        Intent intent1 = new Intent(this, PollingService.class);
        stopService(intent1);
        MainActivity.this.finish();
    }

    /**
     * 关于的方法
     */
    private void navAbout() {
        Intent intent = new Intent(mActivity, AboutActivity.class);
        mActivity.startActivity(intent);
    }


    //在onResume()方法注册
    @Override
    protected void onResume() {
        super.onResume();

        setOperatorInfo("check_select = ? and username = ?", mTvOperatorCheckMain);
        setOperatorInfo("login_select = ? and username = ?", mTvOperatorLoginMain);
//        mTvChangeLaneMain.setText((String) SPUtils.get(this, SPUtils.TEXTLANE, "X08"));

        List<SupportDraftOrSubmit> drafts = DataSupport.where("username = ? and lite_type = ?", mUsername, GlobalManager.TYPE_DRAFT_LITE).find(SupportDraftOrSubmit.class);
        List<SupportDraftOrSubmit> submits = DataSupport.where("username = ? and lite_type = ?", mUsername, GlobalManager.TYPE_SUBMIT_LITE).find(SupportDraftOrSubmit.class);
        mTvMathNumberDraft.setText(drafts.size() + "");
        mTvMathNumberSubmit.setText(submits.size() + "");

        List<TeamItem> teamItems = DataSupport.where("username = ?", mUsername).find(TeamItem.class);
        if (teamItems.size() != 0) {
            mTvChangeStationMain.setText(teamItems.get(0).getStation());
            mTvChangeLaneMain.setText(teamItems.get(0).getDefaultLane());

        } else {
            mTvChangeLaneMain.setText("A01");
        }

        //注册广播，接收service中启动的线程发送过来的信息，同时更新UI
        IntentFilter filter = new IntentFilter("com.example.updateUI");
        mBroadCaseReceiver = new MyBroadCaseReceiver();
        registerReceiver(mBroadCaseReceiver, filter);
    }

    private void setOperatorInfo(String condition, TextView textView) {
        List<SupportOperator> operatorList = DataSupport.where(condition, "1", mUsername).find(SupportOperator.class);
        //ArrayList<String> checkOperatorList = new ArrayList<>();
        String operator = "";
        if (operatorList.size() != 0) {
            for (int i = 0; i < operatorList.size(); i++) {
                String job_number = operatorList.get(i).getJob_number();
                String operator_name = operatorList.get(i).getOperator_name();
                if (i == 0) {
                    operator = job_number + "/" + operator_name;
                } else {
                    operator = operator + "\n" + job_number + "/" + operator_name;
                }
            }
        } else {
            operator = "无/无";
        }
        Logger.i(operator);
        if (operatorList.size() == 3) {
//            textView.setTextSize(CommonUtils.sp2px(this, 7));
            textView.setTextSize(15);
        } else {
//            textView.setTextSize(CommonUtils.sp2px(this, 9));
            textView.setTextSize(18);
        }
        textView.setText(operator);
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
           /* if (System.currentTimeMillis() - firstClick > 2000) {
                firstClick = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCollector.finishAll();
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.addCategory(Intent.CATEGORY_HOME);
                    startActivity(i);
                }
//                return super.onKeyDown(keyCode, event);
            }*/
            return false;
        }
        return false;
    }

    /**
     * 根据版本号判断要不要更新
     */
    private void updateApp() {
        String version = DevicesInfoUtils.getInstance().getVersion(mActivity);
        CheckUpdateUtils.checkUpdate("GreenRoad.apk", version,
                new CheckUpdateUtils.CheckCallBack() {
                    @Override
                    public void onSuccess(UpdateAppInfo updateInfo) {
                        String isForce = updateInfo.getData().getLastfalse();//是否需要强制更新
                        String downUrl = updateInfo.getData().getDownloadurl();//apk下载地址
                        String updateinfo = updateInfo.getData().getUpdateinfo();//apk更新详情
                        String appName = updateInfo.getData().getAppname();

                        Logger.i(isForce + "------" + downUrl + " -----"
                                + updateinfo + " -----" + appName);

                        if (("1".equals(isForce)) && !TextUtils.isEmpty(updateinfo)) {//强制更新
                            Logger.i("强制更新");
                            forceUpdate(MainActivity.this, appName, downUrl, updateinfo);
                        } else {//非强制更新
                            //正常升级
                            Logger.i("正常升级");
                            normalUpdate(MainActivity.this, appName, downUrl, updateinfo);
                        }
                    }

                    @Override
                    public void onError() {
                        noneUpdate(MainActivity.this);
                        Logger.i("返回信息为空,更新错误!");

                    }
                });
    }

    /**
     * 强制更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    Logger.i("立即更新,,,,当前手机状态是否为可下载状态");
                    showDownloadSetting();
                    return;
                }
                //   DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
                AppInnerDownLoder.downLoadApk(MainActivity.this, downUrl, appName);
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 正常更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void normalUpdate(Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                AppInnerDownLoder.downLoadApk(context, downUrl, appName);
                //  DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
            }
        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 无需跟新
     *
     * @param context
     */
    private void noneUpdate(Context context) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新")
                .setMessage("当前已是最新版本无需更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false).create().show();
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        //  mLocalBroadcastManager.unregisterReceiver(mPostReceiver);
        //  PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
        unregisterReceiver(mBroadCaseReceiver);
    }

    private void openSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    class MyBroadCaseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            int draft = intent.getIntExtra(SubmitIntentService.ARG_BROADCAST_DRAFT, 0);
            int submit = intent.getIntExtra(SubmitIntentService.ARG_BROADCAST_SUBMIT, 0);
            mTvMathNumberDraft.setText(draft + "");
            mTvMathNumberSubmit.setText(submit + "");
        }
    }

    /**
     * 从服务器端加载货物信息数据到本地数据库
     *
     * @param username
     */
    private void initGoodData(String username) {
////        String BASE_url = "http://10.0.2.2:3000/data/";
//        String BASE_url = "http://192.168.43.108:3000/data/";
////        String BASE_url = "https://api.douban.com/v2/movie/";
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(5, TimeUnit.SECONDS);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                // .client(genericClient())
//                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl(BASE_url)
//                .build();
//
//        HttpUtilsApi httpUtilsApi = retrofit.create(HttpUtilsApi.class);
//
//        Observable<HttpResultGoods> goods = httpUtilsApi.getGoods("goods");


        Subscriber<HttpResultGoods> subscriber = new Subscriber<HttpResultGoods>() {
            @Override
            public void onCompleted() {
                Logger.i("完成");

            }

            @Override
            public void onError(Throwable e) {

                Logger.i("错误" + e.getMessage());
                mRlProgressLogin.setVisibility(View.GONE);
                mLlMainContain.setVisibility(View.VISIBLE);
                ToastUtils.singleToast("加载货物信息失败,请检查网络是否连接连接正常");
            }

            @Override
            public void onNext(HttpResultGoods goodsBean) {
                int code = goodsBean.getCode();
                Logger.i(code + "");
                if (code == 200) {
                    mRlProgressLogin.setVisibility(View.VISIBLE);
                    mLlMainContain.setVisibility(View.GONE);

                    Logger.i(goodsBean.toString());

                    List<HttpResultGoods.DataBean.SubjectsBean> subjectsBeanList = goodsBean.getData().getSubjects();
                    List<String> goodsTypeList = goodsBean.getData().getGoodsTypeList();
                    List<String> carTypeList = goodsBean.getData().getCarTypeList();
                    for (int i = 0; i < goodsTypeList.size(); i++) {
                        Logger.i(goodsTypeList.get(i));
                    }
                    String markTime = goodsBean.getData().getMarkTime();
                    Logger.i(markTime + "");
                    DataSupport.deleteAll(SupportGoods.class);
                    for (int i = 0; i < subjectsBeanList.size(); i++) {
                        SupportGoods supportGoods = new SupportGoods();
                        supportGoods.setName(subjectsBeanList.get(i).getName());
                        supportGoods.setPinyin(subjectsBeanList.get(i).getPinyin());
                        supportGoods.setType(subjectsBeanList.get(i).getType());
                        supportGoods.setImageUrl(subjectsBeanList.get(i).getImageUrl());
                        supportGoods.setSortId(subjectsBeanList.get(i).getSortId());
                        supportGoods.save();
                    }
//                    List<SupportGoods> supportGoodsList = DataSupport.findAll(SupportGoods.class);

                    SupportGoods supportGoods = new SupportGoods();
                    supportGoods.setMarkTime(markTime);
                    supportGoods.setGoodsTypeList(goodsTypeList);
                    supportGoods.setCarTypeList(carTypeList);
                    supportGoods.updateAll();
                    savePicture(username);
                } else if (code == 300) {
                    Logger.i("货物数据没有改变");
                    Message msg = Message.obtain();
                    msg.what = 2;
//                    msg.obj = username;
                    mHandler.sendMessage(msg);
                }
            }
        };
        SupportGoods firstGoods = DataSupport.findFirst(SupportGoods.class);
        List<SupportGoods> all = DataSupport.findAll(SupportGoods.class);
        Logger.i(all.size()+"数据库中货物的条目111111");

        if (firstGoods != null) {
            String markTime = firstGoods.getMarkTime();
            Logger.i(markTime + "数据库中货物的条目2222222");
            RequestGoods.getInstance().postGoods(subscriber, markTime);
        } else {
            RequestGoods.getInstance().postGoods(subscriber, "");
        }

    }

    /**
     * 将拿到的数据解析保存图片到本地并重新生成数据库
     *
     * @param username
     */
    private void savePicture(String username) {

        if (mFile == null) {
            mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "GreenGoods");
            mFile.mkdirs();
        }
        mFilePath_str = mFile.getPath();
        List<String> typeList = DataSupport.findFirst(SupportGoods.class).getGoodsTypeList();

        //根据type名来新建文件夹存放图片
        for (int i = 0; i < typeList.size(); i++) {
            File typeFile = new File(mFilePath_str, typeList.get(i));
            typeFile.mkdirs();
        }

        new Thread(() -> {
            DataSupport.deleteAll(SupportLocalGoods.class);
            for (int i = 0; i < typeList.size(); i++) {
                List<SupportGoods> supportGoods = DataSupport.where("type = ?", typeList.get(i)).find(SupportGoods.class);
                for (int j = 0; j < supportGoods.size(); j++) {
                    String mFilePath_str_new = mFilePath_str + "/" + typeList.get(i);
//                    Bitmap bitmap = ImageUtils.getBitmapByUrl("http://csdnimg.cn/cdn/content-toolbar/yearlogo.png");
                    Bitmap bitmap = ImageUtils.getBitmapByUrl(supportGoods.get(j).getImageUrl());
                    ImageUtils.saveImage(bitmap, mFilePath_str_new, supportGoods.get(j).getName() + ".png");
                    SupportLocalGoods localGoods = new SupportLocalGoods();
                    localGoods.setImageUrl(mFilePath_str_new + "/" + supportGoods.get(j).getName() + ".png");
                    localGoods.setName(supportGoods.get(j).getName());
                    localGoods.setPinyin(supportGoods.get(j).getPinyin());
                    localGoods.setSortId(supportGoods.get(j).getSortId());
                    localGoods.setType(supportGoods.get(j).getType());
                    localGoods.save();
                }
            }
            Message msg = Message.obtain();
            msg.what = 1;
//            msg.obj = username;
            mHandler.sendMessage(msg);
        }).start();
    }

    /**
     * 登陆成功开启服务
     */
    private void startPollingService() {
        Intent intent = new Intent(this, PollingService.class);
        startService(intent);
    }
}
