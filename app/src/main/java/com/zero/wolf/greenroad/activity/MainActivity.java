package com.zero.wolf.greenroad.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.NetWorkStateReceiver;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.GoodsLite;
import com.zero.wolf.greenroad.bean.NumberLite;
import com.zero.wolf.greenroad.bean.StationDataBean;
import com.zero.wolf.greenroad.bean.StationLite;
import com.zero.wolf.greenroad.interfacy.HttpMethods;
import com.zero.wolf.greenroad.interfacy.HttpUtilsApi;
import com.zero.wolf.greenroad.litepalbean.CarNumberHead;
import com.zero.wolf.greenroad.litepalbean.GoodsInfo;
import com.zero.wolf.greenroad.litepalbean.StationInfo;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.DevicesInfoUtils;
import com.zero.wolf.greenroad.tools.SDcardSpace;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.update.Subject;
import com.zero.wolf.greenroad.update.SubscriberOnNextListener;
import com.zero.wolf.greenroad.view.CircleImageView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.ButterKnife;
import ezy.boost.update.ICheckAgent;
import ezy.boost.update.IUpdateChecker;
import ezy.boost.update.IUpdateParser;
import ezy.boost.update.UpdateInfo;
import ezy.boost.update.UpdateManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zero.wolf.greenroad.R.id.tv_change;

/**
 * Created by Administrator on 2017/6/20.
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, SubscriberOnNextListener<List<Subject>> {

    private static final String TAG = "MainActivity";
    private static final int REQ_0 = 001;
    private TextView mTitle_text;

    String mCheckUrl = "http://client.waimai.baidu.com/message/updatetag";
    String mUpdateUrl = "http://mobile.ac.qq.com/qqcomic_android.apk";

    TextView mUnSendCarNumber;
    private String mFilePath;
    private AppCompatActivity mActivity;
    private LinearLayout mLayout_top;
    private LinearLayout mLayout_center;
    private LinearLayout mLayout_bottom;
    private TextView mTv_number_has_send;
    private TextView mTv_number_has_not_send;
    private LinearLayout mMath_number_main_two;
    private String mAvailSpace;
    private NetWorkStateReceiver mNetWorkStateReceiver;
    private TextView mTv_change3;
    private CircleImageView mIvCamera;


    private SubscriberOnNextListener getTopMovieOnNext;
    private String mOperator;
    private boolean mIsConnected;
    private String mUsername;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);
        mActivity = this;


        mFilePath = Environment.getExternalStorageDirectory().getPath();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //mTvOperator.setText("功成名就");

        initData();
        initSp();
        initLitePal();
        initView();


        mIvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                intent.putExtra("username", mUsername);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    private void initLitePal() {
        LitePal.getDatabase();

        initStation();

        initCarNumber();

        initGoodsLite();
    }

    /**
     * 更新goods数据库
     */
    private void initGoodsLite() {
        HttpUtilsApi utilsApi = HttpMethods.getInstance().getApi();
        Observable<GoodsLite<List<GoodsLite.DataBean>>> goodsInfo = utilsApi.getGoodsInfo();

        goodsInfo.subscribeOn(Schedulers.io())
                .map(new Func1<GoodsLite<List<GoodsLite.DataBean>>, List<GoodsLite.DataBean>>() {
                    @Override
                    public List<GoodsLite.DataBean> call(GoodsLite<List<GoodsLite.DataBean>> listGoodsLite) {
                        return listGoodsLite.getData();
                    }

                }).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GoodsLite.DataBean>>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());

                    }

                    @Override
                    public void onNext(List<GoodsLite.DataBean> dataBeen) {

                        List<GoodsInfo> goodsInfos = DataSupport.findAll(GoodsInfo.class);
                        boolean b = goodsInfo == null;
                        Logger.i("" + b);
                        if (goodsInfos.size() != 0) {
                            if (dataBeen.size() == goodsInfos.size()) {
                                Logger.i("该方法走了没1");
                                Logger.i("返回");
                                return;
                            } else {
                                Logger.i("该方法走了没2");
                                DataSupport.deleteAll(GoodsInfo.class);
                                goodsInfoFore(dataBeen);
                            }
                        } else {
                            goodsInfoFore(dataBeen);
                        }
                    }

                });
    }

    private void goodsInfoFore(List<GoodsLite.DataBean> dataBeen) {
        for (int i = 0; i < dataBeen.size(); i++) {
            Logger.i("该方法走了没3");
            Logger.i("数据更新");
            GoodsInfo info = new GoodsInfo();
            info.setAlias(dataBeen.get(i).getAlias());
            info.setScientificname(dataBeen.get(i).getScientificname());
            info.setCargoid(dataBeen.get(i).getCargoid());
            info.save();
        }
    }

    /**
     * 接收到的车牌牌头的数据
     */

    private void initCarNumber() {
        Observable<NumberLite<List<NumberLite.DataBean>>> numberInfo = HttpMethods.getInstance().getApi().getNumberInfo();
        numberInfo.subscribeOn(Schedulers.io())
                .map(new Func1<NumberLite<List<NumberLite.DataBean>>, List<NumberLite.DataBean>>() {
                    @Override
                    public List<NumberLite.DataBean> call(NumberLite<List<NumberLite.DataBean>> listNumberLite) {
                        return listNumberLite.getData();
                    }

                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<NumberLite.DataBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<NumberLite.DataBean> dataBeen) {
                        Logger.i(dataBeen.get(0).getPac());
                        DataSupport.deleteAll(CarNumberHead.class);
                        for (int i = 0; i < dataBeen.size(); i++) {
                            CarNumberHead numberHead = new CarNumberHead();
                            numberHead.setHeadName(dataBeen.get(i).getPac());
                            numberHead.save();
                        }

                    }
                });
    }

    /**
     * 接收到的收费站站名的数据
     */
    private void initStation() {
        HttpMethods httpMethods = HttpMethods.getInstance();
        Observable<StationLite<List<StationDataBean>>> stationInfo = httpMethods.getApi().getStationInfo();

        stationInfo.subscribeOn(Schedulers.io())
                .map(new Func1<StationLite<List<StationDataBean>>, List<StationDataBean>>() {
                    @Override
                    public List<StationDataBean> call(StationLite<List<StationDataBean>> listStationLite) {
                        return listStationLite.getData();
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<StationDataBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                    }

                    @Override
                    public void onNext(List<StationDataBean> stationDataBeen) {
                        Logger.i("" + stationDataBeen.get(0).getId());
                        Logger.i(stationDataBeen.get(0).getZhanname());
                        Logger.i("" + stationDataBeen.get(1).getId());
                        Logger.i(stationDataBeen.get(1).getZhanname());
                        Logger.i("" + stationDataBeen.get(2).getId());
                        Logger.i(stationDataBeen.get(2).getZhanname());

                        DataSupport.deleteAll(StationInfo.class);
                        for (int i = 0; i < stationDataBeen.size(); i++) {
                            StationInfo info = new StationInfo();
                            Logger.i(stationDataBeen.get(i).toString());
                            info.setStationId(stationDataBeen.get(i).getId());
                            info.setStationName(stationDataBeen.get(i).getZhanname());
                            info.save();
                        }

                    }
                });
    }

    private void initData() {
        SDcardSpace sDcardSpace = new SDcardSpace(mActivity);
        mAvailSpace = sDcardSpace.getAvailSpace();
        //Log.i(TAG, "initData: "+ mAvailSpace1);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mOperator = (String) extras.get("operator");
        mUsername = (String) extras.get("username");

    }


    private void initView() {

        //得到拍照的按钮
        mIvCamera = (CircleImageView) findViewById(R.id.iv_camera);


        //mIvCamera.setOnClickListener(this);
        TextView title_text_view = ActionBarTool.getInstance(mActivity).getTitle_text_view();
        title_text_view.setText("泰安东收费站");

        mLayout_top = (LinearLayout) findViewById(R.id.layout_top);
        mLayout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        mLayout_center = (LinearLayout) findViewById(R.id.layout_center);

        //找到固定的textview
        TextView textView1 = (TextView) mLayout_top.findViewById(R.id.layout_group_main).findViewById(R.id.tv_no_change);
        textView1.setText(getString(R.string.static_tv_operator));
        TextView textView2 = (TextView) mLayout_center.findViewById(R.id.layout_group_main).findViewById(R.id.tv_no_change);
        textView2.setText(getString(R.string.static_tv_space));
        TextView textView3 = (TextView) mLayout_bottom.findViewById(R.id.layout_group_main).findViewById(R.id.tv_no_change);
        textView3.setText(getString(R.string.static_tv_net_state));

        //找到改变的TextView
        TextView tv_change1 = (TextView) mLayout_top.findViewById(R.id.layout_group_main).findViewById(tv_change);
        TextView tv_change2 = (TextView) mLayout_center.findViewById(R.id.layout_group_main).findViewById(tv_change);
        mTv_change3 = (TextView) mLayout_bottom.findViewById(R.id.layout_group_main).findViewById(tv_change);


        tv_change1.setText(mOperator);
        tv_change2.setText(mAvailSpace);
        // mTv_change3.setText("良好");
        mIsConnected = NetWorkManager.isnetworkConnected(mActivity);
        if (mIsConnected) {
            mTv_change3.setText("良好");
            mTv_change3.setTextColor(Color.BLUE);
        } else {
            mTv_change3.setText("网络未连接");
            mTv_change3.setTextColor(Color.RED);
        }
    /*    mNetWorkStateReceiver.setNetworkStationListener(new NetWorkStateReceiver.NetworkStation() {
            @Override
            public void onStation(String netStation) {
                mTv_change3.setText(netStation);
            }
        });
*/
        //得到版本号
        TextView version_number = (TextView) findViewById(R.id.version_number);
        version_number.setText("e绿通 V" + DevicesInfoUtils.getInstance().getVersion(mActivity));


    }

    private void initSp() {
        //如果cra_count为空则创建，否则不创建
        if (SPUtils.get(getApplicationContext(), SPUtils.CAR_COUNT, 0) == null) {
            Logger.i("zoule?");
            SPUtils.putAndApply(getApplicationContext(), SPUtils.CAR_COUNT, 0);
        }
        //如果cra_not_count为空则创建，否则不创建
        if (SPUtils.get(getApplicationContext(), SPUtils.CAR_NOT_COUNT, 0) == null) {
            SPUtils.putAndApply(getApplicationContext(), SPUtils.CAR_NOT_COUNT, 0);
        }
    }

    private void initCount() {
        //找到两个计数的textview
        mMath_number_main_two = (LinearLayout) findViewById(R.id.math_number_main_two);
        mTv_number_has_send = (TextView) mMath_number_main_two.findViewById(R.id.math_number_main_has).findViewById(R.id.tv_math_number_main_has);
        mTv_number_has_not_send = (TextView) mMath_number_main_two.findViewById(R.id.math_number_main_has_not).findViewById(R.id.tv_math_number_main_has_not);


        int count_shut = (int) SPUtils.get(getApplicationContext(), SPUtils.CAR_COUNT, 0);
        int count_cut = (int) SPUtils.get(getApplicationContext(), SPUtils.CAR_NOT_COUNT, 0);

        mTv_number_has_send.setText(String.valueOf(count_shut));
        mTv_number_has_not_send.setText(String.valueOf(count_cut));
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "点击了设置", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_about) {
            navAbout();
        } else if (id == R.id.nav_update) {
            updateVersion();

        } else if (id == R.id.nav_cancer) {
            cancelCount();
        } else if (id == R.id.nav_backup) {
            buckUpApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 退出程序
     */
    private void buckUpApp() {

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
        dialog.setTitle("清楚车辆计数");
        dialog.setMessage("是否对拍摄车辆以及上传车辆进行重新计数");
        dialog.setCancelable(false);
        dialog.setPositiveButton("清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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


    /**
     * 根据版本号判断要不要更新
     */
    private void updateVersion() {

        UpdateManager.setDebuggable(true);
        UpdateManager.setWifiOnly(false);
        UpdateManager.setUrl(mCheckUrl, "yyb");
        UpdateManager.check(this);

        check(true, true, false, false, false, 998);
    }

    /**
     * 检查更新
     *
     * @param isManual
     * @param hasUpdate
     * @param isForce
     * @param isSilent
     * @param isIgnorable
     * @param notifyId
     */
    public void check(boolean isManual, final boolean hasUpdate, final boolean isForce, final boolean isSilent, final boolean isIgnorable, final int
            notifyId) {
        UpdateManager.create(this).setChecker(new IUpdateChecker() {
            @Override
            public void check(ICheckAgent agent, String url) {
                Log.e("ezy.update", "checking");
                agent.setInfo("");
            }
        }).setUrl(mCheckUrl).setManual(isManual).setNotifyId(notifyId).setParser(new IUpdateParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                UpdateInfo info = new UpdateInfo();
                info.hasUpdate = hasUpdate;
                info.updateContent = "• 支持文字、贴纸、背景音乐，尽情展现欢乐气氛；\n• 两人视频通话支持实时滤镜，丰富滤镜，多彩心情；\n• 图片编辑新增艺术滤镜，一键打造文艺画风；\n• 资料卡新增点赞排行榜，看好友里谁是魅力之王。";
                info.versionCode = 587;
                info.versionName = "v5.8.7";
                info.url = mUpdateUrl;
                info.md5 = "56cf48f10e4cf6043fbf53bbbc4009e3";
                info.size = 10149314;
                info.isForce = isForce;
                info.isIgnorable = isIgnorable;
                info.isSilent = isSilent;
                return info;
            }
        }).check();
    }

    //在onResume()方法注册
    @Override
    protected void onResume() {
        /*if (mNetWorkStateReceiver == null) {
            mNetWorkStateReceiver = new NetWorkStateReceiver(mTv_change3);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetWorkStateReceiver, filter);

*/
        initCount();
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
       /* unregisterReceiver(mNetWorkStateReceiver);
*/
        super.onPause();
    }

    @Override
    public void onNext(List<Subject> subjects) {

    }
}
