package com.zero.wolf.greenroad;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.zero.wolf.greenroad.activity.AboutActivity;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.DevicesInfoUtils;
import com.zero.wolf.greenroad.tools.SDcardSpace;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.update.Subject;
import com.zero.wolf.greenroad.update.SubscriberOnNextListener;
import com.zero.wolf.greenroad.view.CircleImageView;

import org.litepal.LitePal;

import java.util.List;

import butterknife.ButterKnife;
import ezy.boost.update.ICheckAgent;
import ezy.boost.update.IUpdateChecker;
import ezy.boost.update.IUpdateParser;
import ezy.boost.update.UpdateInfo;
import ezy.boost.update.UpdateManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);
        mActivity = this;

        Logger.i("123324");

        mFilePath = Environment.getExternalStorageDirectory().getPath();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //mTvOperator.setText("功成名就");

        initSp();
        initLitePal();
        initData();
        initView();


        mIvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
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

    }

    private void initData() {
        SDcardSpace sDcardSpace = new SDcardSpace(mActivity);
        mAvailSpace = sDcardSpace.getAvailSpace();
        //Log.i(TAG, "initData: "+ mAvailSpace1);
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


        tv_change1.setText("李树人");
        tv_change2.setText(mAvailSpace);
        // mTv_change3.setText("良好");

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
            SPUtils.putAndApply(getApplicationContext(),SPUtils.CAR_COUNT, 0);
        }
        //如果cra_not_count为空则创建，否则不创建
        if (SPUtils.get(getApplicationContext(), SPUtils.CAR_NOT_COUNT, 0) == null) {
            Logger.i("zoule?"+"ma");
            SPUtils.putAndApply(getApplicationContext(), SPUtils.CAR_NOT_COUNT, 0);
        }
    }
    private void initCount() {
        //找到两个计数的textview
        mMath_number_main_two = (LinearLayout) findViewById(R.id.math_number_main_two);
        mTv_number_has_send = (TextView) mMath_number_main_two.findViewById(R.id.math_number_main_has).findViewById(R.id.tv_math_number_main_has);
        mTv_number_has_not_send = (TextView) mMath_number_main_two.findViewById(R.id.math_number_main_has_not).findViewById(R.id.tv_math_number_main_has_not);


        int count_shut = (int) SPUtils.get(getApplicationContext(),SPUtils.CAR_COUNT, 0);
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                SPUtils.cancel_count(getApplicationContext(),SPUtils.CAR_COUNT);
                SPUtils.cancel_count(getApplicationContext(),SPUtils.CAR_NOT_COUNT);
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
