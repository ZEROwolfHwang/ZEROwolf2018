package com.zero.wolf.greenroad.activity;

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
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.ShowViewPagerAdapter;
import com.zero.wolf.greenroad.bean.CheckedBean;
import com.zero.wolf.greenroad.bean.DetailInfoBean;
import com.zero.wolf.greenroad.bean.ScanInfoBean;
import com.zero.wolf.greenroad.fragment.CarNumberFragment;
import com.zero.wolf.greenroad.fragment.DetailsFragment;
import com.zero.wolf.greenroad.fragment.GoodsFragment;
import com.zero.wolf.greenroad.fragment.PhotoFragment;
import com.zero.wolf.greenroad.litepalbean.SupportChecked;
import com.zero.wolf.greenroad.litepalbean.SupportDetail;
import com.zero.wolf.greenroad.litepalbean.SupportScan;
import com.zero.wolf.greenroad.servicy.SubmitService;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.PermissionUtils;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

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

        initViewPagerAndTabs();

        initView();


    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (ACTION_DRAFT_ENTER_SHOW.equals(intent.getAction())) {
            SupportDetail supportDetail = intent.getParcelableExtra(ARG_SUPPORT_DETAIL);
            SupportScan supportScan = intent.getParcelableExtra(ARG_SUPPORT_SCAN);
            SupportChecked supportChecked = intent.getParcelableExtra(ARG_SUPPORT_CHECKED);
            int lite_ID = intent.getIntExtra(ARG_SUPPORT_MEDIA, 1);
            Logger.i(lite_ID + "qqq");

            mPagerAdapter = new ShowViewPagerAdapter(getSupportFragmentManager(), this,
                    supportDetail, supportScan, supportChecked, lite_ID, intent.getType());
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
        if (mFile == null) {
            mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "GreenShoot");
            mFile.mkdirs();
        }
        mFilePath_str = mFile.getPath();


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
                //submit2Service();
                SubmitService.startActionSubmit(this);
                Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                startActivity(intent);
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
/*
    private void submit2Service() {



        getListenerData();

*//*
        if (mScanInfoBean_Q != null) {

            save2Litepal(GlobalManager.TYPE_SUBMIT_LITE);
        } else {
            ToastUtils.singleToast("请扫描二维码");
        }*//*
        save2Litepal(GlobalManager.TYPE_SUBMIT_LITE);


        postPicture(postTime);

        postJson(postTime);


    }

    private void postJson(String postTime) {
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
            info.setCurrent_time(postTime);

        } else {
            ToastUtils.singleToast("请扫描二维码");
        }

        Gson gson = new Gson();
        String route = gson.toJson(info);


        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), route);
        Logger.i("json  string" + route);


        Subscriber<HttpResultCode> subscriber_json = new Subscriber<HttpResultCode>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.i(e.getMessage());
                ToastUtils.singleToast(e.getMessage());
            }

            @Override
            public void onNext(HttpResultCode httpResultCode) {
                int code = httpResultCode.getCode();
                Logger.i(code + "");
                ToastUtils.singleToast(code + "");
            }
        };
        RequestJson.getInstance().postJson(subscriber_json, body);

    }

    private void postPicture(String postTime) {
        ArrayList<PathTitleBean> pathTitle_sanzheng = new ArrayList<>();
        ArrayList<PathTitleBean> pathTitle_cheshen = new ArrayList<>();
        ArrayList<PathTitleBean> pathTitle_huozhao = new ArrayList<>();
        for (int i = 0; i < mDetailInfoBean_Q.getPath_and_title().size(); i++) {
            String photo_type = mDetailInfoBean_Q.getPath_and_title().get(i).getPhoto_type();
            if (GlobalManager.PHOTO_TYPE_SANZHENG.equals(photo_type)) {
                pathTitle_sanzheng.add(mDetailInfoBean_Q.getPath_and_title().get(i));
            } else if (GlobalManager.PHOTO_TYPE_CHESHEN.equals(photo_type)) {
                pathTitle_cheshen.add(mDetailInfoBean_Q.getPath_and_title().get(i));
            } else if (GlobalManager.PHOTO_TYPE_HUOZHAO.equals(photo_type)) {
                pathTitle_huozhao.add(mDetailInfoBean_Q.getPath_and_title().get(i));
            }
        }
        SubmitService.startActionSubmit(this,mRoad_Q,mStation_Q);
        List<MultipartBody.Part> sanzheng = null;
        List<MultipartBody.Part> cheshen = null;
        List<MultipartBody.Part> huozhao = null;
        if (pathTitle_sanzheng != null && pathTitle_sanzheng.size() != 0) {
            sanzheng = getBodyPart1(pathTitle_sanzheng, "sanzheng");
        } else {
            ToastUtils.singleToast("请拍摄或选择三证照片");
        }
        if (pathTitle_cheshen != null && pathTitle_cheshen.size() != 0) {
            cheshen = getBodyPart1(pathTitle_cheshen, "cheshen");
        } else {
            ToastUtils.singleToast("请拍摄或选择三证照片");
        }
        if (pathTitle_huozhao != null && pathTitle_huozhao.size() != 0) {
            huozhao = getBodyPart1(pathTitle_huozhao, "huozhao");
        } else {
            ToastUtils.singleToast("请拍摄或选择三证照片");
        }
        Subscriber<HttpResultCode> subscriber_picture = new Subscriber<HttpResultCode>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.i(e.getMessage());
                ToastUtils.singleToast(e.getMessage());
            }

            @Override
            public void onNext(HttpResultCode httpResultCode) {
                int code = httpResultCode.getCode();
                Logger.i(code + "");
                ToastUtils.singleToast(code + "");
            }
        };
        RequestPicture.getInstance().postPicture(subscriber_picture, postTime, sanzheng, cheshen, huozhao);


    }

    public static List<MultipartBody.Part> getBodyPart1(List<PathTitleBean> bitmapList, String type) {

        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (int i = 0; i < bitmapList.size(); i++) {

            String mFilePath_str_new = null;
            try {
                Bitmap bitmap = BitmapUtil.convertToBitmap(bitmapList.get(i).getPath(), 800, 1080);

                mFilePath_str_new = mFilePath_str + "/" + System.currentTimeMillis()
                        + type + (i + 1) + ".jpg";

                saveFile(bitmap, mFilePath_str_new);

            } catch (Exception e) {
                e.printStackTrace();

            }

            //String path = bitmapList.get(i).getPath();
            if (mFilePath_str_new != null) {
                Logger.i(mFilePath_str_new);
                File file = new File(mFilePath_str_new);//filePath 图片地址
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);//image/png
                //RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpg"), file);//image/png
                builder.addFormDataPart(type + (i + 1), file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }

    //存储进SD卡
    public static void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);
        //检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete();  //删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        //100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    *//**
     * 拿到两个fragment中被监听的数据
     * 在提交至服务器以及保存草稿中都要用到
     *//*
    private void getListenerData() {

        DetailsFragment.setDetailsConnectListener((bean) -> {
            mDetailInfoBean_Q = bean;

            //拿到图片的体进行网络传递
           *//* if (mBitmapList != null && mBitmapList.size() != 0) {
                List<MultipartBody.Part> parts = PathUtil
                        .getBodyPart(mBitmapList);
                Logger.i(mDetailInfoBean_Q.toString());
                Logger.i(parts.toString());
            }*//*
        });
        PhotoFragment.setSelectedListListener((medias_sanzheng, medias_cheshen, medias_huozhao) -> {
            mLocalMedias_sanzheng_Q = medias_sanzheng;
            mLocalMedias_cheshen_Q = medias_cheshen;
            mLocalMedias_huozhao_Q = medias_huozhao;
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

    }*/

    /**
     * 保存草稿
     * 1.进入草稿的activity并保存
     * 2.将数据保存到数据库
     */
    private void saveDraft() {
       SubmitService.startActionSave(this);
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
            notifyDataChangeAndFinish();
            Intent intent = new Intent(ShowActivity.this, MainActivity.class);
            startActivity(intent);
        });

        builder.setPositiveButton("保存并退出", (dialog, which) -> {
            saveDraft();
            //在这里做保存草稿的操作
            notifyDataChangeAndFinish();
            Intent intent = new Intent(ShowActivity.this, MainActivity.class);
            startActivity(intent);
        });
        builder.show();
    }

    private void notifyDataChangeAndFinish() {
        CarNumberFragment.notifyDataChange();
        GoodsFragment.notifyDataChange();
        PhotoFragment.notifyDataChange();
        ConclusionActivity.notifyDataChange();
        DetailsFragment.notifyDataChange();
        mActivity.finish();
    }
}
