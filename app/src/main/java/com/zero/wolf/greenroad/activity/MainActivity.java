package com.zero.wolf.greenroad.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.LoginActivity;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.TestActivity;
import com.zero.wolf.greenroad.bean.UpdateAppInfo;
import com.zero.wolf.greenroad.litepalbean.SupportOperator;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;
import com.zero.wolf.greenroad.servicy.PostIntentService;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.ActivityCollector;
import com.zero.wolf.greenroad.tools.DevicesInfoUtils;
import com.zero.wolf.greenroad.tools.PermissionUtils;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.ToastUtils;
import com.zero.wolf.greenroad.update.AppInnerDownLoder;
import com.zero.wolf.greenroad.update.CheckUpdateUtils;
import com.zero.wolf.greenroad.update.Subject;
import com.zero.wolf.greenroad.update.SubscriberOnNextListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/20.
 */

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        SubscriberOnNextListener<List<Subject>> {

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
    RelativeLayout mRlMainDraft;
    @BindView(R.id.tv_math_number_submit)
    TextView mTvMathNumberSubmit;
    @BindView(R.id.rl_main_submit)
    RelativeLayout mRlMainSubmit;
    @BindView(R.id.tv_math_number_draft)
    TextView mTvMathNumberDraft;
    private long firstClick;
    private static final String TAG = "MainActivity";
    private static final int REQ_0 = 001;

    @BindView(R.id.footer_item_setting)
    TextView mTv_footer_setting;
    @BindView(R.id.footer_item_theme)
    TextView mTv_footer_theme;
    @BindView(R.id.footer_item_location)
    TextView mTv_footer_location;

    private AppCompatActivity mActivity;


    private AlertDialog.Builder mDialog;


    private AlertDialog.Builder mNotPostDialog;


    private int thumb_margin_left_day = 0;
    private int thumb_margin_left_night = 0;


    private String mRoad_Q;
    private String mStation_Q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);
        mActivity = this;

        List app_config_info = new ArrayList<String>();

        app_config_info.add("163127841234");
        app_config_info.add("西部沿海");
        app_config_info.add("广海");

        Logger.i(app_config_info.toString());
        SPListUtil.putStrListValue(this, SPListUtil.APPCONFIGINFO, app_config_info);



        List<String> strListValue = SPListUtil.getStrListValue(getContext(), SPListUtil.APPCONFIGINFO);
        for (int i = 0; i < strListValue.size(); i++) {
            String string = strListValue.get(i).toString();
            Logger.i(string);
        }
        mRoad_Q = strListValue.get(1).toString();
        mStation_Q = strListValue.get(2).toString();

        mTvOperatorCheckMain = (TextView) findViewById(R.id.tv_operator_check_main);
        mTvOperatorLoginMain = (TextView) findViewById(R.id.tv_operator_login_main);

        mTvChangeLaneMain.setOnClickListener(v -> openSettingActivity());
        mTvOperatorCheckMain.setOnClickListener(v -> openSettingActivity());
        mTvOperatorLoginMain.setOnClickListener(v -> openSettingActivity());

        mTvChangeStationMain.setText(mStation_Q);

        Button button = (Button) findViewById(R.id.btn_enter_show);
        button.setOnClickListener(v -> {
            ShowActivity.actionStart(MainActivity.this);
        });

        mRlMainSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SubmitActivity.class);
            startActivity(intent);
        });
        mRlMainDraft.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DraftActivity.class);
            startActivity(intent);
        });


        initData();
        //initSp();
        initView();


    }

    private void initData() {

        PermissionUtils.verifyStoragePermissions(mActivity);
    }


    private void initView() {

        setSupportActionBar(mToolbarMain);

        //得到拍照的按钮
        //  mIvCamera = (ImageView) findViewById(R.id.iv_camera);

        //mIvCamera.setOnClickListener(this);
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


    @OnClick({R.id.footer_item_setting,
            R.id.footer_item_theme, R.id.footer_item_location})
    public void onFooterClick(View view) {
        switch (view.getId()) {
            case R.id.footer_item_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.footer_item_theme:
                changeTheme();
                break;
            case R.id.footer_item_location:
                ToastUtils.singleToast("在这里做更新位置的处理");
                Intent intent1 = new Intent(this, TestActivity.class);
                finish();
                startActivity(intent1);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_about) {
            navAbout();
        } else if (id == R.id.nav_update) {
            Logger.i("点击了更新按钮");
            updateApp();

        } else if (id == R.id.nav_cancer) {
            cancelCount();
        } else if (id == R.id.nav_backup) {
            buckUpApp();
        } else if (id == R.id.nav_post) {
            post_not_upload();
            refresh();
            Logger.i("点击了未上传按钮");
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
                finish();
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


    private void post_not_upload() {


        //initBroadCast();

        mNotPostDialog = new AlertDialog.Builder(mActivity);
        mNotPostDialog.setTitle("是否提交本地保存的车辆信息");
        mNotPostDialog.setMessage("本地保存的未上传成功的车辆个数为:" +
                SPUtils.get(mActivity, SPUtils.CAR_NOT_COUNT, 0));
        mNotPostDialog.setCancelable(false);
        mNotPostDialog.setPositiveButton("提交", ((dialog, which) ->
                PostIntentService.startActionNotPost(this))
        );
        mNotPostDialog.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        mNotPostDialog.show();
    }

    /**
     * 退出程序
     */
    private void buckUpApp() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 关于的方法
     */
    private void navAbout() {
        Intent intent = new Intent(mActivity, AboutActivity.class);
        mActivity.startActivity(intent);
    }

    /**
     * 将拍照车辆的计数以及上传车辆的计数归零
     */
    private void cancelCount() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle("将已拍摄车辆及未上传车辆清零重新计数");
        dialog.setMessage("点击“确定”将清空计数" + "\"" +
                "点击“取消”将取消该操作");
        dialog.setCancelable(false);
        dialog.setPositiveButton(getString(R.string.dialog_messge_OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface di, int which) {
                DataSupport.deleteAll(SupportPhotoLite.class);
                SPUtils.cancel_count(getApplicationContext(), SPUtils.CAR_COUNT);
                SPUtils.cancel_count(getApplicationContext(), SPUtils.CAR_NOT_COUNT);
                refresh();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /**
     * 刷新当前页面的数据
     */
    private void refresh() {
        onResume();
    }


    //在onResume()方法注册
    @Override
    protected void onResume() {
        super.onResume();
        setOperatorInfo("check_select = ?", mTvOperatorCheckMain);
        setOperatorInfo("login_select = ?", mTvOperatorLoginMain);
        mTvChangeLaneMain.setText((String) SPUtils.get(this, SPUtils.TEXTLANE, "66"));

    }
    private void setOperatorInfo(String condition, TextView textView) {
        List<SupportOperator> operatorList = DataSupport.where(condition, "1").find(SupportOperator.class);
        if (operatorList.size() != 0) {
            Logger.i(operatorList.toString());
            String job_number = operatorList.get(0).getJob_number();
            String operator_name = operatorList.get(0).getOperator_name();
            textView.setText(job_number + "(" + operator_name + ")");
        } else {
            textView.setText("500001(苏三)");
        }

    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onNext(List<Subject> subjects) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - firstClick > 2000) {
                firstClick = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCollector.finishAll();
            }
            return true;
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


    @OnClick(R.id.footer_item_setting)
    public void onSetting(View view) {
        ToastUtils.singleToast("点击了设置");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        //  mLocalBroadcastManager.unregisterReceiver(mPostReceiver);
        Logger.i("mainactivity被销毁");
        //  PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
    }
    private void openSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
