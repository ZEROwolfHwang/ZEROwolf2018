package com.android.htc.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.android.htc.greenroad.R;
import com.android.htc.greenroad.adapter.ShowViewPagerAdapter;
import com.android.htc.greenroad.bean.CheckedBean;
import com.android.htc.greenroad.bean.DetailInfoBean;
import com.android.htc.greenroad.bean.ScanInfoBean;
import com.android.htc.greenroad.fragment.CarNumberFragment;
import com.android.htc.greenroad.fragment.DetailsFragment;
import com.android.htc.greenroad.fragment.GoodsFragment;
import com.android.htc.greenroad.fragment.PhotoFragment;
import com.android.htc.greenroad.litepalbean.SupportChecked;
import com.android.htc.greenroad.litepalbean.SupportDetail;
import com.android.htc.greenroad.litepalbean.SupportScan;
import com.android.htc.greenroad.servicy.SubmitService;
import com.android.htc.greenroad.tools.ActionBarTool;
import com.android.htc.greenroad.tools.PermissionUtils;
import com.android.htc.greenroad.tools.SPListUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ShowActivity extends BaseActivity {

    private static String ARG_SUPPORT_SCAN = "arg_support_scan";
    private static String ARG_SUPPORT_DETAIL = "arg_support_detail";
    private static String ARG_SUPPORT_CHECKED = "arg_support_checked";
    private static String ARG_SUPPORT_MEDIA = "arg_support_media";
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
    private static List<LocalMedia> mLocalMedias_sanzheng_Q;
    private static List<LocalMedia> mLocalMedias_cheshen_Q;
    private static List<LocalMedia> mLocalMedias_huozhao_Q;

    private File mFile;
    private static String mFilePath_str;
    public static String mShowType;
    public static int mLite_id;

    public static void actionStart(Context context, SupportDetail supportDetail, SupportScan supportScan,
                                   SupportChecked supportChecked, int lite_ID) {
        Intent intent = new Intent(context, ShowActivity.class);
        intent.setAction(ACTION_DRAFT_ENTER_SHOW);

        intent.putExtra(ARG_SUPPORT_DETAIL, supportDetail);
        intent.putExtra(ARG_SUPPORT_SCAN, supportScan);
        intent.putExtra(ARG_SUPPORT_CHECKED, supportChecked);
        intent.putExtra(ARG_SUPPORT_MEDIA, lite_ID);

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


        initView();


    }

    private void getIntentData() {
        Intent intent = getIntent();
        mShowType = intent.getType();
        if (ACTION_DRAFT_ENTER_SHOW.equals(intent.getAction())) {
            SupportDetail supportDetail = intent.getParcelableExtra(ARG_SUPPORT_DETAIL);
            SupportScan supportScan = intent.getParcelableExtra(ARG_SUPPORT_SCAN);
            SupportChecked supportChecked = intent.getParcelableExtra(ARG_SUPPORT_CHECKED);
            mLite_id = intent.getIntExtra(ARG_SUPPORT_MEDIA, 1);
            Logger.i(mLite_id + "qqq");

            mPagerAdapter = new ShowViewPagerAdapter(getSupportFragmentManager(), this,
                    supportDetail, supportScan, supportChecked, mLite_id, mShowType);

        } else if (ACTION_MAIN_ENTER_SHOW.equals(intent.getAction())) {
            mShowType = intent.getType();
            mPagerAdapter = new ShowViewPagerAdapter(getSupportFragmentManager(), this, mShowType);
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
        mRoad_Q = strListValue.get(1).toString();
        mStation_Q = strListValue.get(2).toString();
    }

    private void initData() {
        PermissionUtils.verifyStoragePermissions(mActivity);
        if (mFile == null) {
            mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "GreenShoot");
            mFile.mkdirs();
        }
        mFilePath_str = mFile.getPath();


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
                mFabDraft.hideButtonInMenu(true);
                mFabSubmit.hideButtonInMenu(true);
                mMenuFab.toggle(false);
                Logger.i(DetailsFragment.sEnterType);
                SubmitService.startActionSubmit(this,this,DetailsFragment.sEnterType,mShowType);
                break;
            case R.id.fab_draft:
                mFabDraft.hideButtonInMenu(true);
                mFabSubmit.hideButtonInMenu(true);
                mMenuFab.toggle(false);
                Logger.i(DetailsFragment.sEnterType);
                saveDraft(0);
                break;

            default:
                break;
        }
    }

    /**
     * 保存草稿
     * 1.进入草稿的activity并保存
     * 2.将数据保存到数据库
     */
    private void saveDraft(int id) {
       SubmitService.startActionSave(this,DetailsFragment.sEnterType,mShowType,id);
    }

    @Override
    public void onBackPressed() {
        if (mMenuFab.isOpened()) {
            mFabDraft.hideButtonInMenu(true);
            mFabSubmit.hideButtonInMenu(true);
            mMenuFab.toggle(false);
        } else {
            backToMain();

        }
    }

    private void backToMain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("是否保存为草稿");
        builder.setMessage("点击确定保存为草稿并退出\n点击直接退出则清空当前采集");
        builder.setNegativeButton("直接退出", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(ShowActivity.this, MainActivity.class);
            startActivity(intent);
            mActivity.finish();
        });

        builder.setPositiveButton("保存并退出", (dialog, which) -> {
            //在这里做保存草稿的操作
            saveDraft(1);
            Intent intent = new Intent(ShowActivity.this, MainActivity.class);
            startActivity(intent);
            mActivity.finish();
        });
        builder.show();
    }

    public static void notifyDataChangeAndFinish() {
        CarNumberFragment.notifyDataChange();
        GoodsFragment.notifyDataChange();
        PhotoFragment.notifyDataChange();
        ConclusionActivity.notifyDataChange();
        DetailsFragment.notifyDataChange();
    }
}
