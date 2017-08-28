package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.ShowViewPagerAdapter;
import com.zero.wolf.greenroad.bean.CheckedBean;
import com.zero.wolf.greenroad.bean.DetailInfoBean;
import com.zero.wolf.greenroad.bean.PathTitleBean;
import com.zero.wolf.greenroad.bean.ScanInfoBean;
import com.zero.wolf.greenroad.fragment.CarNumberFragment;
import com.zero.wolf.greenroad.fragment.CheckedFragment;
import com.zero.wolf.greenroad.fragment.DetailsFragment;
import com.zero.wolf.greenroad.fragment.GoodsFragment;
import com.zero.wolf.greenroad.fragment.PhotoFragment;
import com.zero.wolf.greenroad.fragment.ScanFragment;
import com.zero.wolf.greenroad.httpresultbean.HttpResultPolling;
import com.zero.wolf.greenroad.https.HttpUtilsApi;
import com.zero.wolf.greenroad.https.PostInfo;
import com.zero.wolf.greenroad.litepalbean.SupportChecked;
import com.zero.wolf.greenroad.litepalbean.SupportDetail;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;
import com.zero.wolf.greenroad.litepalbean.SupportMedia;
import com.zero.wolf.greenroad.litepalbean.SupportScan;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.PermissionUtils;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
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
    private static List<LocalMedia> mMediasHuozhao_huowu_Q;


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
            int lite_ID = intent.getIntExtra(ARG_SUPPORT_MEDIA,1);
            Logger.i(lite_ID+"qqq");
            List<SupportDraftOrSubmit> supportDraftOrSubmits = DataSupport.where("lite_ID = ?", String.valueOf(lite_ID)).find(SupportDraftOrSubmit.class);
            SupportMedia supportMedia = supportDraftOrSubmits.get(0).getSupportMedia();
            for (int i = 0; i < supportMedia.getPaths().size(); i++) {
                Logger.i(supportMedia.getPaths().get(i));
                Logger.i(supportMedia.getHeights().get(i)+"-----");
            }
            mPagerAdapter = new ShowViewPagerAdapter(getSupportFragmentManager(), this,
                    supportDetail, supportScan, supportChecked, supportMedia, intent.getType());
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
                submit2Service();
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

    private void submit2Service() {
        getListenerData();
/*
        if (mScanInfoBean_Q != null) {

            save2Litepal(GlobalManager.TYPE_SUBMIT_LITE);
        } else {
            ToastUtils.singleToast("请扫描二维码");
        }*/
        save2Litepal(GlobalManager.TYPE_SUBMIT_LITE);

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
            info.setCurrent_time(TimeUtil.getCurrentTimeTos());


        } else {
            ToastUtils.singleToast("请扫描二维码");
        }

        Gson gson = new Gson();
        String route = gson.toJson(info);

        Map<String, String> partMap = new HashMap<>();

        ArrayList<String> pathList = new ArrayList();
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

        List<MultipartBody.Part> sanzheng = getBodyPart1(pathTitle_sanzheng, "sanzheng");
        List<MultipartBody.Part> cheshen = getBodyPart1(pathTitle_cheshen, "cheshen");
        List<MultipartBody.Part> huozhao = getBodyPart1(pathTitle_huozhao, "huozhao");

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://192.168.2.122/lvsetondao/index.php/Interfacy/Api/")
                .baseUrl("http://greenft.githubshop.com/index.php/Interfacy/Api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        HttpUtilsApi api = retrofit.create(HttpUtilsApi.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), route);
        Logger.i("json  string" + route);

        Observable<HttpResultPolling> observable = api.postPicture(mScanInfoBean_Q.getScan_code(), sanzheng, cheshen, huozhao);
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
                        ToastUtils.singleToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultPolling httpResultPolling) {
                        int code = httpResultPolling.getCode();
                        Logger.i(code + "");
                        ToastUtils.singleToast(code + "");
                    }
                });
        Observable<HttpResultPolling> observable1 = api.postJson(body);
        observable1.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultPolling>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                        ToastUtils.singleToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultPolling httpResultPolling) {
                        int code = httpResultPolling.getCode();
                        Logger.i(code + "");
                        ToastUtils.singleToast(code + "");
                    }
                });
    }

    public static List<MultipartBody.Part> getBodyPart1(List<PathTitleBean> bitmapList, String type) {
/*

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < bitmapList.size(); i++) {
            list.add(bitmapList.get(i).getPath());
        }
*/
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (int i = 0; i < bitmapList.size(); i++) {
            String path = bitmapList.get(i).getPath();
            if (path != null) {
                Logger.i(bitmapList.get(i).toString());
                File file = new File(path);//filePath 图片地址
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);//image/png
                //RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpg"), file);//image/png
                builder.addFormDataPart(type + (i + 1), file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }

    /**
     * 拿到两个fragment中被监听的数据
     * 在提交至服务器以及保存草稿中都要用到
     */
    private void getListenerData() {

        DetailsFragment.setDetailsConnectListener((bean) -> {
            mDetailInfoBean_Q = bean;

            //拿到图片的体进行网络传递
           /* if (mBitmapList != null && mBitmapList.size() != 0) {
                List<MultipartBody.Part> parts = PathUtil
                        .getBodyPart(mBitmapList);
                Logger.i(mDetailInfoBean_Q.toString());
                Logger.i(parts.toString());
            }*/
        });
        PhotoFragment.setSelectedListListener((medias_sanzheng, medias_cheshen, medias_huozhao) -> {
            mLocalMedias_sanzheng_Q = medias_sanzheng;
            if (mLocalMedias_sanzheng_Q != null && mLocalMedias_sanzheng_Q.size() != 0) {
                for (int i = 0; i < mLocalMedias_sanzheng_Q.size(); i++) {
                    Logger.i(mLocalMedias_sanzheng_Q.get(i).getPath().toString());
                }
            }
            mLocalMedias_cheshen_Q = medias_cheshen;
            mMediasHuozhao_huowu_Q = medias_huozhao;
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

        //如果数据库中的数据已经存在了一条与当前流水号相同的则视为已经保存过的


        //   DraftActivity.actionStart(this, mConfigInfoBean, mDetailInfoBean_Q, mPhotoPaths);
//        if (supportDrafts.size() == 0) {

        //保存至本地数据库
        save2Litepal(GlobalManager.TYPE_DRAFT_LITE);
      /*  List<SupportDraftOrSubmit> draftList = DataSupport.
                where(GlobalManager.LITE_CONDITION, GlobalManager.TYPE_DRAFT_LITE).
                find(SupportDraftOrSubmit.class);
        for (int i = 0; i < draftList.size(); i++) {
            for (int j = 0; j < draftList.get(i).getSupportMedia().getPaths().size(); j++) {
                Logger.i(draftList.get(i).getSupportMedia().getPaths().get(j).toString());
            }
        }
        Logger.i(draftList.get(0).toString());*/
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        List<SupportDraftOrSubmit> all = DataSupport.findAll(SupportDraftOrSubmit.class);
        SupportMedia supportMedia = all.get(0).getSupportMedia();
        for (int i = 0; i < supportMedia.getPaths().size(); i++) {
        Logger.i(supportMedia.getPaths().get(i));
        Logger.i(supportMedia.getPictureTypes().get(i));
        Logger.i(supportMedia.getWidths().get(i)+"yyyyyyy");
        Logger.i(supportMedia.getHeights().get(i)+"yyyyyyy");
        Logger.i(supportMedia.getNums().get(i)+"yyyyyyy");
        Logger.i(supportMedia.getPositions().get(i)+"yyyyyyy");
        Logger.i(supportMedia.getDurations().get(i)+"yyyyyyy");
        Logger.i(supportMedia.getMimeTypes().get(i)+"yyyyyyy");

        }

    }

    /**
     * 将保存草稿数据或者提交网络的数据保存之后本地服务器
     */
    private void save2Litepal(String lite_type) {

        SupportDraftOrSubmit support = new SupportDraftOrSubmit();
        int count = DataSupport.count(SupportDraftOrSubmit.class);
        support.setLite_ID(count + 1);
        support.setLite_type(lite_type);
        support.setCurrent_time(TimeUtil.getCurrentTimeTos());
        //保存数据到表SupportScan
        SupportScan supportScan = new SupportScan();
        supportScan.setLite_ID(count + 1);
        supportScan.setScan_code(mScanInfoBean_Q.getScan_code());
        supportScan.setScan_01Q(mScanInfoBean_Q.getScan_01Q());
        supportScan.setScan_02Q(mScanInfoBean_Q.getScan_02Q());
        supportScan.setScan_03Q(mScanInfoBean_Q.getScan_03Q());
        supportScan.setScan_04Q(mScanInfoBean_Q.getScan_04Q());
        supportScan.setScan_05Q(mScanInfoBean_Q.getScan_05Q());
        supportScan.setScan_06Q(mScanInfoBean_Q.getScan_06Q());
        supportScan.setScan_07Q(mScanInfoBean_Q.getScan_07Q());
        supportScan.setScan_08Q(mScanInfoBean_Q.getScan_08Q());
        supportScan.setScan_09Q(mScanInfoBean_Q.getScan_09Q());
        supportScan.setScan_10Q(mScanInfoBean_Q.getScan_10Q());
        supportScan.setScan_11Q(mScanInfoBean_Q.getScan_11Q());
        supportScan.setScan_12Q(mScanInfoBean_Q.getScan_12Q());
        supportScan.setScan_code(mScanInfoBean_Q.getScan_code());
        supportScan.save();

        //保存数据到表SupportDetail
        SupportDetail supportDetail = new SupportDetail();
        supportDetail.setLite_ID(count + 1);
        supportDetail.setNumber(mDetailInfoBean_Q.getNumber());
        supportDetail.setColor(mDetailInfoBean_Q.getColor());
        supportDetail.setGoods(mDetailInfoBean_Q.getGoods());
        supportDetail.setStation(mStation_Q);
        supportDetail.setRoad(mRoad_Q);
        supportDetail.setLane((String) SPUtils.get(this, SPUtils.TEXTLANE, "66"));

        ArrayList<String> picturePath = new ArrayList<>();
        ArrayList<String> pictureTitle = new ArrayList<>();
        List<PathTitleBean> path_all = mDetailInfoBean_Q.getPath_and_title();

        if (path_all != null) {
            for (int i = 0; i < path_all.size(); i++) {
                picturePath.add(path_all.get(i).getPath());
                pictureTitle.add(path_all.get(i).getTitle());
            }
        }

        supportDetail.setPicturePath(picturePath);
        supportDetail.setPictureTitle(pictureTitle);

        supportDetail.save();

        //保存数据到表SupportChecked
        SupportChecked supportChecked = new SupportChecked();
        supportChecked.setLite_ID(count + 1);
        supportChecked.setIsRoom(mCheckedBean_Q.getIsRoom());
        supportChecked.setIsFree(mCheckedBean_Q.getIsFree());
        supportChecked.setConclusion(mCheckedBean_Q.getConclusion());
        supportChecked.setDescription(mCheckedBean_Q.getDescription());
        supportChecked.setSiteCheck(mCheckedBean_Q.getSiteCheck());
        supportChecked.setSiteLogin(mCheckedBean_Q.getSiteLogin());
        supportChecked.save();

        SupportMedia supportMedia = new SupportMedia();
        if (mLocalMedias_sanzheng_Q != null && mLocalMedias_sanzheng_Q.size() != 0) {
            ArrayList<String> paths = new ArrayList<>();
            ArrayList<String> pictureTypes = new ArrayList<>();
            ArrayList<Long> mDurations = new ArrayList<>();
            ArrayList<Integer> nums = new ArrayList<>();
            ArrayList<Integer> mimeTypes = new ArrayList<>();
            ArrayList<Integer> widths = new ArrayList<>();
            ArrayList<Integer> heights = new ArrayList<>();
            ArrayList<Integer> positions = new ArrayList<>();
            for (int i = 0; i < mLocalMedias_sanzheng_Q.size(); i++) {
                LocalMedia media = mLocalMedias_sanzheng_Q.get(i);
                paths.add(media.getPath());
                pictureTypes.add(media.getPictureType());
                mDurations.add(media.getDuration());
                nums.add(media.getNum());
                heights.add(media.getHeight());
                widths.add(media.getWidth());
                positions.add(media.getPosition());
                mimeTypes.add(media.getMimeType());
                Logger.i(media.getPath() + "---" +
                        media.getCompressPath() + "---" +
                        media.getCutPath() + "---" +
                        media.getDuration() + "---" +
                        media.getHeight() + "---" +
                        media.getMimeType() + "---" +
                        media.getNum() + "---" +
                        media.getPictureType() + "---" +
                        media.getPosition() + "---" +
                        media.isChecked() + "---" +
                        media.isCompressed() + "---" +
                        media.isCut() + "---" +
                        media.getWidth());
            }
            supportMedia.setPaths(paths);
            supportMedia.setPictureTypes(pictureTypes);
            supportMedia.setDurations(mDurations);
            supportMedia.setNums(nums);
            supportMedia.setHeights(heights);
            supportMedia.setWidths(widths);
            supportMedia.setMimeTypes(mimeTypes);
            supportMedia.setPositions(positions);
            supportMedia.setLite_ID(count + 1);
            supportMedia.setPhotoType(GlobalManager.PHOTO_TYPE_SANZHENG);
            supportMedia.save();

        }

      /*  saveSupportMedia(supportMedias,mLocalMedias_sanzheng_Q,count,GlobalManager.PHOTO_TYPE_SANZHENG);
        saveSupportMedia(supportMedias,mLocalMedias_cheshen_Q,count,GlobalManager.PHOTO_TYPE_CHESHEN);
        saveSupportMedia(supportMedias,mMediasHuozhao_huowu_Q,count,GlobalManager.PHOTO_TYPE_HUOZHAO);
*/

        support.setSupportScan(supportScan);
        support.setSupportDetail(supportDetail);
        support.setSupportChecked(supportChecked);
        support.setSupportMedia(supportMedia);

        support.save();
    }

   /* private void saveSupportMedia(ArrayList<SupportMedia> supportMedias,List<LocalMedia> medias,int count,String photo_type) {
        for (int i = 0; i < medias.size(); i++) {
            LocalMedia localMedia = medias.get(i);
            SupportMedia supportMedia = new SupportMedia();
            supportMedia.setChecked(localMedia.isChecked());
            supportMedia.setCompressed(localMedia.isCompressed());
            supportMedia.setCut(localMedia.isCut());
            supportMedia.setCompressPath(localMedia.getCompressPath());
            supportMedia.setPath(localMedia.getPath());
            supportMedia.setCutPath(localMedia.getCutPath());
            supportMedia.setDuration(localMedia.getDuration());
            supportMedia.setHeight(localMedia.getHeight());
            supportMedia.setPosition(localMedia.getPosition());
            supportMedia.setMimeType(localMedia.getMimeType());
            supportMedia.setNum(localMedia.getNum());
            supportMedia.setPictureType(localMedia.getPictureType());
            supportMedia.setWidth(localMedia.getWidth());
            supportMedia.setLite_ID(count+1);
            supportMedia.setPhoto_type(photo_type);
            supportMedia.save();
            supportMedias.add(supportMedia);
        }
    }*/

    @Override
    public void onBackPressed() {
        backToMain();
    }

    private void backToMain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("是否保存为草稿");
        builder.setMessage("点击确定保存为草稿并退出\n点击直接退出则清空当前采集");
        builder.setNegativeButton("直接退出", (dialog, which) -> {
            dialog.dismiss();
            notifyDataChangeAndFinish();

        });

        builder.setPositiveButton("保存并退出", (dialog, which) -> {
            saveDraft();
            //在这里做保存草稿的操作
            notifyDataChangeAndFinish();
        });
        builder.show();
    }

    private void notifyDataChangeAndFinish() {
        CarNumberFragment.notifyDataChange();
        GoodsFragment.notifyDataChange();
        PhotoFragment.newInstance().notifyDataChange();
        ConclusionActivity.notifyDataChange();

        mActivity.finish();
    }
}
