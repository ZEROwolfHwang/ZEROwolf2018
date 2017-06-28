package com.zero.wolf.greenroad;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.litepalbean.GetServiceData;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.DevicesInfoUtils;
import com.zero.wolf.greenroad.tools.SDcardSpace;
import com.zero.wolf.greenroad.view.CircleImageView;

import org.litepal.LitePal;

import butterknife.ButterKnife;

import static com.zero.wolf.greenroad.R.id.tv_change;

/**
 * Created by Administrator on 2017/6/20.
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, NetWorkStateReceiver.NetworkStation {

    private static final String TAG = "MainActivity";
    private static final int REQ_0 = 001;
    private TextView mTitle_text;


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
        GetServiceData.getInstance();



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


        mMath_number_main_two = (LinearLayout) findViewById(R.id.math_number_main_two);
        mTv_number_has_send = (TextView) mMath_number_main_two.findViewById(R.id.math_number_main_has).findViewById(R.id.tv_math_number_main_has);
        mTv_number_has_not_send = (TextView) mMath_number_main_two.findViewById(R.id.math_number_main_has_not).findViewById(R.id.tv_math_number_main_has_not);

        mTv_number_has_send.setText("99");
        mTv_number_has_not_send.setText("55");

        tv_change1.setText("李树人");
        tv_change2.setText(mAvailSpace);
       // mTv_change3.setText("良好");

        //得到版本号
        TextView version_number = (TextView) findViewById(R.id.version_number);
        version_number.setText("e绿通 V" + DevicesInfoUtils.getInstance().getVersion(mActivity));

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



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 更新客户端
     */
    private void upDateApp() {
        //判断网络是否连接

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
        mNetWorkStateReceiver.setNetworkStationListener(this);
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(mNetWorkStateReceiver);
        super.onPause();
    }

    /**
     * 得到网络状态的回调
     * @param netStation
     */
    @Override
    public void onStation(String netStation) {
        Logger.i(netStation);
        mTv_change3.setText(netStation);

    }
}
