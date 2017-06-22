package com.zero.wolf.greenroad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zero.wolf.greenroad.tools.SDcardSpace;
import com.zero.wolf.greenroad.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zero.wolf.greenroad.R.id.title_text;
import static com.zero.wolf.greenroad.R.id.tv_change;

/**
 * Created by Administrator on 2017/6/20.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int REQ_0 = 001;
    private TextView mTitle_text;

    @BindView(R.id.iv_camera)
    CircleImageView mIvCamera;

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

    private void initData() {
        mAvailSpace = SDcardSpace.getInstance().getAvailSpace();
    }


    private void initView() {
        //mIvCamera.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View titleView = inflater.inflate(R.layout.action_bar_title, null);

        actionBar.setCustomView(titleView, lp);

        actionBar.setDisplayShowHomeEnabled(false);//去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        mTitle_text = (TextView) titleView.findViewById(title_text);
        mTitle_text.setText("泰安东收费站");

        mLayout_top = (LinearLayout) findViewById(R.id.layout_top);
        mLayout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        mLayout_center= (LinearLayout) findViewById(R.id.layout_center);

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
        TextView tv_change3 = (TextView) mLayout_bottom.findViewById(R.id.layout_group_main).findViewById(tv_change);


        mMath_number_main_two = (LinearLayout) findViewById(R.id.math_number_main_two);
        mTv_number_has_send = (TextView) mMath_number_main_two.findViewById(R.id.math_number_main_has).findViewById(R.id.tv_math_number_main_has);
        mTv_number_has_not_send = (TextView) mMath_number_main_two.findViewById(R.id.math_number_main_has_not).findViewById(R.id.tv_math_number_main_has_not);

        mTv_number_has_send.setText("99");
        mTv_number_has_not_send.setText("55");

        tv_change1.setText("李树人");
        tv_change2.setText("54G");
        tv_change3.setText("良好");
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "点击了设置", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
