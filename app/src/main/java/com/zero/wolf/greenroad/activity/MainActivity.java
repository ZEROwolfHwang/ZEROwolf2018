package com.zero.wolf.greenroad.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.NetWorkStateReceiver;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.HttpResultNumber;
import com.zero.wolf.greenroad.bean.UpdateAppInfo;
import com.zero.wolf.greenroad.httpresultbean.HttpResultGoods;
import com.zero.wolf.greenroad.httpresultbean.HttpResultPostImg;
import com.zero.wolf.greenroad.httpresultbean.StationDataBean;
import com.zero.wolf.greenroad.https.HttpMethods;
import com.zero.wolf.greenroad.https.RequestLiteGoods;
import com.zero.wolf.greenroad.https.RequestLiteNumber;
import com.zero.wolf.greenroad.https.RequestLiteStation;
import com.zero.wolf.greenroad.litepalbean.SupportCarNumber;
import com.zero.wolf.greenroad.litepalbean.SupportGoods;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;
import com.zero.wolf.greenroad.litepalbean.SupportStation;
import com.zero.wolf.greenroad.manager.CarNumberCount;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.ActivityCollector;
import com.zero.wolf.greenroad.tools.DevicesInfoUtils;
import com.zero.wolf.greenroad.tools.FileBitmapUtil;
import com.zero.wolf.greenroad.tools.PathUtil;
import com.zero.wolf.greenroad.tools.PermissionUtils;
import com.zero.wolf.greenroad.tools.RxHolder;
import com.zero.wolf.greenroad.tools.SDcardSpace;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.ToastUtils;
import com.zero.wolf.greenroad.update.AppInnerDownLoder;
import com.zero.wolf.greenroad.update.CheckUpdateUtils;
import com.zero.wolf.greenroad.update.Subject;
import com.zero.wolf.greenroad.update.SubscriberOnNextListener;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import rx.Observable;
import rx.Subscriber;

import static com.zero.wolf.greenroad.R.id.tv_change;
import static org.litepal.crud.DataSupport.findAll;

/**
 * Created by Administrator on 2017/6/20.
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, SubscriberOnNextListener<List<Subject>> {

    private long firstClick;
    private static final String TAG = "MainActivity";
    private static final int REQ_0 = 001;

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
    private ImageView mIvCamera;


    private String mOperator;
    private boolean mIsConnected;
    private String mUsername;
    private boolean isNumberCompleted;
    private boolean isStationCompleted;
    private boolean isGoodsCompleted;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            // pd.dismiss();// 关闭ProgressDialog
            String s = (String) msg.obj;
            if (s == "123") {
                isNumberCompleted = true;
            } else if (s == "456") {
                isStationCompleted = true;
            } else if (s == "789") {
                isGoodsCompleted = true;
            } else if (s.equals("111")||s.equals("222")||s.equals("333")) {
                mProgressDialog.dismiss();
                ToastUtils.singleToast("数据加载失败,请检查网络");
            }

            if (isNumberCompleted && isStationCompleted && isGoodsCompleted) {
                mProgressDialog.dismiss();
            }

        }
    };
    private AlertDialog.Builder mDialog;
    private ProgressDialog mProgressDialog;
    private File mGoodsFile;
    private String mGoodsFilePath;

    private String mStationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);
        mActivity = this;


        mFilePath = Environment.getExternalStorageDirectory().getPath();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initData();
        initSp();
        initLitePal();
        initView();


        mIvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                String android_ID = Settings.Secure
                        .getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                String macID = (String) SPUtils.get(mActivity, SPUtils.ISACTIVATIONSUCCESS, "");

                Logger.i(macID);
                if (macID == null) {
                    Intent intent = new Intent(MainActivity.this, ActivationCodeActivity.class);
                    startActivity(intent);
                } else {
                    if (macID.equals(android_ID)) {
                        Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                        intent.putExtra("username", mUsername);
                        intent.putExtra("stationName", mStationName);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, ActivationCodeActivity.class);
                        startActivity(intent);
                    }
                }*/
                Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                intent.putExtra("username", mUsername);
                intent.putExtra("stationName", mStationName);
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
        if (NetWorkManager.isnetworkConnected(mActivity)) {

            isNumberCompleted = false;
            isStationCompleted = false;
            isGoodsCompleted = false;
            LitePal.getDatabase();

            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setTitle("正在更新数据");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    initStation();
                    initCarNumber();
                    initGoodsLite();
                }
            }).start();
        } else {
            return;
        }

    }

    /**
     * 更新goods数据库
     */
    private void initGoodsLite() {
        RequestLiteGoods.getInstance()
                .doGetGoodsInfo(new Subscriber<List<HttpResultGoods.DataBean>>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("货物加载完成");
                        Message message = Message.obtain();
                        message.obj = "789";
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                        Message message = Message.obtain();
                        message.obj = "333";
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onNext(List<HttpResultGoods.DataBean> dataBeen) {
                        List<SupportGoods> supportGoods = findAll(SupportGoods.class);
                        if (supportGoods.size() != 0) {
                            if (dataBeen.size() == supportGoods.size()) {
                                Logger.i("该方法走了没1");
                                Logger.i("返回");
                                return;
                            } else {
                                Logger.i("该方法走了没2");
                                DataSupport.deleteAll(SupportGoods.class);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        goodsInfoFore(dataBeen);
                                    }
                                }).start();
                            }
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoFore(dataBeen);
                                }
                            }).start();
                        }
                    }
                });
    }

    /**
     * 循环遍历网络数据填充本地数据库
     *
     * @param dataBeen
     */
    private void goodsInfoFore(List<HttpResultGoods.DataBean> dataBeen) {
        Logger.i("该方法走了没goods");
        for (int i = 0; i < dataBeen.size(); i++) {

            String imgurl = dataBeen.get(i).getImgurl();
            Bitmap bitmap = FileBitmapUtil.getBitmap(imgurl);
            String fileName = FileBitmapUtil.getFileName(imgurl);

            Logger.i(imgurl);
            Logger.i(fileName);

            try {
                FileBitmapUtil.saveJPGFile(bitmap, fileName, mGoodsFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            SupportGoods info = new SupportGoods();

            info.setAlias(dataBeen.get(i).getAlias().trim());
            info.setScientificname(dataBeen.get(i).getScientificname());
            info.setCargoid(dataBeen.get(i).getCargoid());
            info.setKind(dataBeen.get(i).getKind());
            //   info.setBitmap(bitmap);
            info.setImgurl(mGoodsFilePath + "/" + fileName);//本地数据库保存的图片路径
            info.save();

        }
    }

    /**
     * 循环遍历网络数据填充本地数据库
     *
     * @param dataBeen
     */
    private void numbersInfoFore(List<HttpResultNumber.DataBean> dataBeen) {
        Logger.i("该方法走了没carnumber");
        for (int i = 0; i < dataBeen.size(); i++) {
            SupportCarNumber carNumber = new SupportCarNumber();
            carNumber.setHeadName(dataBeen.get(i).getPac());
            carNumber.save();
        }
    }

    /**
     * 循环遍历网络数据填充本地数据库
     *
     * @param dataBeen
     */

    private void stationsInfoFore(List<StationDataBean> dataBeen) {
        Logger.i("该方法走了没station");
        for (int i = 0; i < dataBeen.size(); i++) {
            SupportStation station = new SupportStation();
            station.setStationId(dataBeen.get(i).getId());
            station.setStationName(dataBeen.get(i).getZhanname());
            station.save();
        }
    }

    /**
     * 接收到的车牌牌头的数据
     */

    private void initCarNumber() {
        RequestLiteNumber.getInstance().doGetNumberInfo(new Subscriber<List<HttpResultNumber.DataBean>>() {
            @Override
            public void onCompleted() {
                //   mNumberListener.onCompleted(true);
                Message message = Message.obtain();
                message.obj = "123";
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.obj = "111";
                handler.sendMessage(message);
                Logger.i(e.getMessage());
            }

            @Override
            public void onNext(List<HttpResultNumber.DataBean> dataBeen) {
              /*  Logger.i(dataBeen.get(0).getPac());
                DataSupport.deleteAll(SupportCarNumber.class);
                for (int i = 0; i < dataBeen.size(); i++) {
                    SupportCarNumber numberHead = new SupportCarNumber();
                    numberHead.setHeadName(dataBeen.get(i).getPac());
                    numberHead.save();
                }*/
                //isNumberCompleted = true;

                List<SupportCarNumber> supportCarNumbers = findAll(SupportCarNumber.class);
                if (supportCarNumbers.size() != 0) {
                    if (dataBeen.size() == supportCarNumbers.size()) {
                        Logger.i("该方法走了没1");
                        Logger.i("返回");
                        return;
                    } else {
                        Logger.i("该方法走了没2");
                        DataSupport.deleteAll(SupportCarNumber.class);
                        numbersInfoFore(dataBeen);
                        List<SupportCarNumber> supportCarNumbers1 = findAll(SupportCarNumber.class);
                        Logger.i(supportCarNumbers1.size() + "");
                    }
                } else {
                    numbersInfoFore(dataBeen);
                    List<SupportCarNumber> supportCarNumbers1 = findAll(SupportCarNumber.class);
                    Logger.i(supportCarNumbers1.size() + "");
                }
            }
        });
    }

    /**
     * 接收到的收费站站名的数据
     */
    private void initStation() {
        RequestLiteStation.getInstance().doGetStationInfo(new Subscriber<List<StationDataBean>>() {
            @Override
            public void onCompleted() {
                Message message = Message.obtain();
                message.obj = "456";
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable e) {
                Logger.i(e.getMessage());
                Message message = Message.obtain();
                message.obj = "222";
                handler.sendMessage(message);
            }

            @Override
            public void onNext(List<StationDataBean> dataBeen) {
                List<SupportStation> supportStations = findAll(SupportStation.class);
                if (supportStations.size() != 0) {
                    if (dataBeen.size() == supportStations.size()) {
                        Logger.i("该方法走了没1");
                        Logger.i("返回");
                        return;
                    } else {
                        Logger.i("该方法走了没2");
                        DataSupport.deleteAll(SupportStation.class);
                        stationsInfoFore(dataBeen);
                        List<SupportStation> supportStations1 = findAll(SupportStation.class);
                        Logger.i(supportStations1.size() + "");
                    }
                } else {
                    stationsInfoFore(dataBeen);
                    List<SupportStation> supportStations1 = findAll(SupportStation.class);
                    Logger.i(supportStations1.size() + "");
                }
            }
        });
    }

    private void initData() {
        SDcardSpace sDcardSpace = new SDcardSpace(mActivity);
        mAvailSpace = sDcardSpace.getAvailSpace();
        //Log.i(TAG, "initData: "+ mAvailSpace1);

        PermissionUtils.verifyStoragePermissions(mActivity);
        if (mGoodsFile == null) {
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mGoodsFile = new File(mFilePath, "goodsImg");
            mGoodsFile.mkdirs();
        }
        mGoodsFilePath = mGoodsFile.getPath();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mOperator = (String) extras.get("operator");
        mUsername = (String) extras.get("username");
        mStationName = (String) extras.get("stationName");

    }


    private void initView() {

        //得到拍照的按钮
        mIvCamera = (ImageView) findViewById(R.id.iv_camera);


        //mIvCamera.setOnClickListener(this);
        TextView title_text_view = ActionBarTool.getInstance(mActivity).getTitle_text_view();
        title_text_view.setText(mStationName);

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
            Logger.i("点击了未上传按钮");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void post_not_upload() {
        List<SupportPhotoLite> liteList = DataSupport
                //.where("is_post==?", "NO").find(SupportPhotoLite.class);
                .findAll(SupportPhotoLite.class);
        Logger.i("未上传车辆"+liteList.size());
        for (int i = 0; i < liteList.size(); i++) {
            String goods = liteList.get(i).getGoods();
            String license_color = liteList.get(i).getLicense_color();
            String license_plate = liteList.get(i).getLicense_plate();
            String photoPath1 = liteList.get(i).getPhotoPath1();
            String photoPath2 = liteList.get(i).getPhotoPath2();
            String photoPath3 = liteList.get(i).getPhotoPath3();
            String shuttime = liteList.get(i).getShuttime();
            String station = liteList.get(i).getStation();
            String username = liteList.get(i).getUsername();
            String car_type = liteList.get(i).getCar_type();

            if (photoPath1 == null) {
                return;
            }

            List<MultipartBody.Part> parts = PathUtil
                    .getMultipartBodyPart(photoPath1, photoPath2, photoPath3);
            Observable<HttpResultPostImg> observable = HttpMethods.getInstance().getApi().postThreeImg("大货车", license_color,
                    shuttime, username, station, license_plate, goods, parts);
            observable.compose(RxHolder.io_main()).subscribe(new Subscriber<HttpResultPostImg>() {
                @Override
                public void onCompleted() {
                    Logger.i("三张照片上传成功");
                }

                @Override
                public void onError(Throwable e) {
                    Logger.i(e.getMessage());
                }

                @Override
                public void onNext(HttpResultPostImg httpResultPostImg) {
                    int code = httpResultPostImg.getCode();
                    String msg = httpResultPostImg.getMsg();
                    if (code == 200) {
                        CarNumberCount.CarNumberCut(mActivity);
                        DataSupport.deleteAll(SupportPhotoLite.class, "shuttime=?", shuttime);
                        Logger.i("shangchuan成功");
                    } else if (code == 300) {
                        ToastUtils.singleToast("上传失败");
                    }
                }
            });
        }

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


    //在onResume()方法注册
    @Override
    protected void onResume() {
        if (mNetWorkStateReceiver == null) {
            mNetWorkStateReceiver = new NetWorkStateReceiver(mTv_change3);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetWorkStateReceiver, filter);

        initCount();
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(mNetWorkStateReceiver);
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
                AppInnerDownLoder.downLoadApk(MainActivity.this, downUrl, appName);
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
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
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
}
