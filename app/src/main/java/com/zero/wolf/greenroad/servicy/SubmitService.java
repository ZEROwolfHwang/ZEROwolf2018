package com.zero.wolf.greenroad.servicy;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.activity.MainActivity;
import com.zero.wolf.greenroad.activity.ShowActivity;
import com.zero.wolf.greenroad.bean.CheckedBean;
import com.zero.wolf.greenroad.bean.DetailInfoBean;
import com.zero.wolf.greenroad.bean.PathTitleBean;
import com.zero.wolf.greenroad.bean.ScanInfoBean;
import com.zero.wolf.greenroad.fragment.CheckedFragment;
import com.zero.wolf.greenroad.fragment.DetailsFragment;
import com.zero.wolf.greenroad.fragment.PhotoFragment;
import com.zero.wolf.greenroad.fragment.ScanFragment;
import com.zero.wolf.greenroad.httpresultbean.HttpResultCode;
import com.zero.wolf.greenroad.https.PostInfo;
import com.zero.wolf.greenroad.https.RequestJson;
import com.zero.wolf.greenroad.https.RequestPicture;
import com.zero.wolf.greenroad.litepalbean.SupportChecked;
import com.zero.wolf.greenroad.litepalbean.SupportDetail;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;
import com.zero.wolf.greenroad.litepalbean.SupportMedia;
import com.zero.wolf.greenroad.litepalbean.SupportScan;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.tools.BitmapUtil;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;

import static org.litepal.LitePalApplication.getContext;


public class SubmitService extends IntentService {
    private static DetailInfoBean mDetailInfoBean_Q;
    private static ScanInfoBean mScanInfoBean_Q;
    private static CheckedBean mCheckedBean_Q;

    private static List<LocalMedia> mLocalMedias_sanzheng_Q;
    private static List<LocalMedia> mLocalMedias_cheshen_Q;
    private static List<LocalMedia> mSelectList_huowu;

    private static String mStation_Q;
    private static String mRoad_Q;
    private File mFile;
    private static String mFilePath_str;
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SUBMIT = "com.zero.wolf.greenroad.submitservice.action.FOO";
    private static final String ACTION_SAVE = "com.zero.wolf.greenroad.submitservice.action.BAZ";
    public static final String ARG_BROADCAST_DRAFT = "arg_broadcast_draft";
    public static final String ARG_BROADCAST_SUBMIT = "arg_broadcast_submit";

    public static final String ARG_TYPE_SAVE = "arg_type_save";
    public static final String ARG_TYPE_SUBMIT = "arg_type_submit";

    public static final String ARG_TYPE_SHOW_SUBMIT = "arg_type_show_submit";
    public static final String ARG_TYPE_SHOW_SAVE = "arg_type_show_save";


    private String mSubmitTime;

    private static ShowActivity sActivity;
    private static Context sContext_draft;
    private static Context sContext_submit;
    private String mShowType_submit;
    private static ArrayList<String> mNew_path_list;

    public SubmitService() {
        super("SubmitService");
    }


    public static void startActionSubmit(ShowActivity activity, Context context, String enterType, String showType) {
        sContext_submit = context;
        sActivity = activity;
        Intent intent = new Intent(sActivity, SubmitService.class);
        intent.setAction(ACTION_SUBMIT);
        intent.putExtra(ARG_TYPE_SUBMIT, enterType);
        intent.putExtra(ARG_TYPE_SHOW_SUBMIT, showType);
        sActivity.startService(intent);
    }

    public static void startActionSave(Context context, String enterType, String showType) {
        sContext_draft = context;
        Intent intent = new Intent(sContext_draft, SubmitService.class);
        intent.setAction(ACTION_SAVE);
        intent.putExtra(ARG_TYPE_SAVE, enterType);
        intent.putExtra(ARG_TYPE_SHOW_SAVE, showType);
        sContext_draft.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            List<String> strListValue = SPListUtil.getStrListValue(getContext(), SPListUtil.APPCONFIGINFO);
            if (strListValue.size() == 3) {
                mRoad_Q = strListValue.get(1).toString();
                mStation_Q = strListValue.get(2).toString();
            }

            final String action = intent.getAction();
            if (ACTION_SUBMIT.equals(action)) {

                String enterType = intent.getStringExtra(ARG_TYPE_SUBMIT);
                mShowType_submit = intent.getStringExtra(ARG_TYPE_SHOW_SUBMIT);

                if (mFile == null) {
                    mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "GreenPicture");
                    mFile.mkdirs();
                }
                mFilePath_str = mFile.getPath();


                mSubmitTime = TimeUtil.getCurrentTimeToDate();

                getListenerData(enterType);

                if (NetWorkManager.isnetworkConnected(sActivity)) {
                    postPictureAndJson(mSubmitTime);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(sActivity);
                    builder.setTitle("网络无连接");
                    builder.setMessage("是否保存为草稿");
                    builder.setPositiveButton("保存草稿", (dialog, which) -> {
                        save2Litepal(mSubmitTime, GlobalManager.TYPE_DRAFT_LITE, ACTION_SUBMIT, mShowType_submit);
                    });
                    builder.setNegativeButton("取消", (dialog, which) -> {
                        dialog.dismiss();
                    });

                    ToastUtils.singleToast("网络无连接");
                }


            } else {
                if (ACTION_SAVE.equals(action)) {
                    String enterType = intent.getStringExtra(ARG_TYPE_SAVE);
                    String showType = intent.getStringExtra(ARG_TYPE_SHOW_SAVE);

                    getListenerData(enterType);

                    String saveTime = TimeUtil.getCurrentTimeToDate();
                    save2Litepal(saveTime, GlobalManager.TYPE_DRAFT_LITE, ACTION_SAVE, showType);
                }
            }
        }
    }

    /**
     * 拿到两个fragment中被监听的数据
     * 在提交至服务器以及保存草稿中都要用到
     */
    private void getListenerData(String enterType) {
        Logger.i("---------------" + enterType);

        DetailsFragment.setDetailsConnectListener((bean) -> {
            mDetailInfoBean_Q = bean;
        });

        if (ShowActivity.TYPE_MAIN_ENTER_SHOW.equals(enterType)) {

            PhotoFragment.setSelectedListListener((medias_sanzheng, medias_cheshen, medias_huozhao) -> {
                mLocalMedias_sanzheng_Q = medias_sanzheng;
                mLocalMedias_cheshen_Q = medias_cheshen;
                mSelectList_huowu = medias_huozhao;
            });
        } else if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(enterType)) {

            DetailsFragment.setSelectedListListener((medias_sanzheng, medias_cheshen, medias_huozhao) -> {
                mLocalMedias_sanzheng_Q = medias_sanzheng;
                mLocalMedias_cheshen_Q = medias_cheshen;
                mSelectList_huowu = medias_huozhao;
            });
        }


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
     * 将保存草稿数据或者提交网络的数据保存之后本地服务器
     */
    private void save2Litepal(String current_time, String lite_type, String action, String showType) {
        int count = 0;
        SupportDraftOrSubmit support = new SupportDraftOrSubmit();
        if (ShowActivity.TYPE_MAIN_ENTER_SHOW.equals(showType)) {
            count = TimeUtil.getTimeId();
            Logger.i(count + "++++++++++++count");
            //DataSupport.count(SupportDraftOrSubmit.class);

            support.setLite_ID(count);
            support.setLite_type(lite_type);
            support.setCurrent_time(current_time);
            //保存数据到表SupportScan
            SupportScan supportScan = new SupportScan();
            supportScan.setLite_ID(count);
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
            supportDetail.setLite_ID(count);
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
            supportChecked.setLite_ID(count);
            supportChecked.setIsRoom(mCheckedBean_Q.getIsRoom());
            supportChecked.setIsFree(mCheckedBean_Q.getIsFree());
            supportChecked.setConclusion(mCheckedBean_Q.getConclusion());
            supportChecked.setDescription(mCheckedBean_Q.getDescription());
            supportChecked.setSiteChecks(mCheckedBean_Q.getSiteChecks());
            supportChecked.setSiteLogin(mCheckedBean_Q.getSiteLogin());
            supportChecked.save();

            if (GlobalManager.TYPE_DRAFT_LITE.equals(lite_type)) {
                SupportMedia supportMedia = new SupportMedia();
                ArrayList<String> photoTypes = new ArrayList<>();
                ArrayList<String> paths = new ArrayList<>();
                ArrayList<String> pictureTypes = new ArrayList<>();
                ArrayList<Long> mDurations = new ArrayList<>();
                ArrayList<Integer> nums = new ArrayList<>();
                ArrayList<Integer> mimeTypes = new ArrayList<>();
                ArrayList<Integer> widths = new ArrayList<>();
                ArrayList<Integer> heights = new ArrayList<>();
                ArrayList<Integer> positions = new ArrayList<>();
                if (mLocalMedias_sanzheng_Q != null && mLocalMedias_sanzheng_Q.size() != 0) {
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
                        photoTypes.add(GlobalManager.PHOTO_TYPE_SANZHENG);
                    }
                }
                if (mLocalMedias_cheshen_Q != null && mLocalMedias_cheshen_Q.size() != 0) {
                    for (int i = 0; i < mLocalMedias_cheshen_Q.size(); i++) {
                        LocalMedia media = mLocalMedias_cheshen_Q.get(i);
                        paths.add(media.getPath());
                        pictureTypes.add(media.getPictureType());
                        mDurations.add(media.getDuration());
                        nums.add(media.getNum());
                        heights.add(media.getHeight());
                        widths.add(media.getWidth());
                        positions.add(media.getPosition());
                        mimeTypes.add(media.getMimeType());
                        photoTypes.add(GlobalManager.PHOTO_TYPE_CHESHEN);
                    }
                }
                if (mSelectList_huowu != null && mSelectList_huowu.size() != 0) {
                    for (int i = 0; i < mSelectList_huowu.size(); i++) {
                        LocalMedia media = mSelectList_huowu.get(i);
                        paths.add(media.getPath());
                        pictureTypes.add(media.getPictureType());
                        mDurations.add(media.getDuration());
                        nums.add(media.getNum());
                        heights.add(media.getHeight());
                        widths.add(media.getWidth());
                        positions.add(media.getPosition());
                        mimeTypes.add(media.getMimeType());
                        photoTypes.add(GlobalManager.PHOTO_TYPE_HUOZHAO);
                    }
                }

                supportMedia.setPaths(paths);
                supportMedia.setPhotoTypes(photoTypes);
                supportMedia.setPictureTypes(pictureTypes);
                supportMedia.setDurations(mDurations);
                supportMedia.setNums(nums);
                supportMedia.setHeights(heights);
                supportMedia.setWidths(widths);
                supportMedia.setMimeTypes(mimeTypes);
                supportMedia.setPositions(positions);
                supportMedia.setLite_ID(count);
                supportMedia.save();

                support.setSupportMedia(supportMedia);
            }

            support.setSupportScan(supportScan);
            support.setSupportDetail(supportDetail);
            support.setSupportChecked(supportChecked);

            support.save();

            if (action.equals(ACTION_SUBMIT)) {
                if (GlobalManager.TYPE_DRAFT_LITE.equals(lite_type)) {
                    SPUtils.add_one(sActivity, SPUtils.MATH_DRAFT_LITE);
                    ToastUtils.singleToast("提交失败并保存至草稿列表");
                } else if (GlobalManager.TYPE_SUBMIT_LITE.equals(lite_type)) {
                    SPUtils.add_one(sActivity, SPUtils.MATH_SUBMIT_LITE);
                    ToastUtils.singleToast("提交成功并保存至提交列表");
                }

            } else if (action.equals(ACTION_SAVE)) {
                ToastUtils.singleToast("已保存至草稿列表");
                SPUtils.add_one(sContext_draft, SPUtils.MATH_DRAFT_LITE);
            }
        } else if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(showType)) {
            count = ShowActivity.mLite_id;
            Logger.i(count + "-------------count");
            //DataSupport.count(SupportDraftOrSubmit.class);

            //support.setLite_ID(count);
            support.setLite_type(lite_type);
            //support.setCurrent_time(current_time);
            //保存数据到表SupportScan
            SupportScan supportScan = new SupportScan();
            // supportScan.setLite_ID(count);
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
            supportScan.updateAll("lite_ID = ?", String.valueOf(count));

            //保存数据到表SupportDetail
            SupportDetail supportDetail = new SupportDetail();
            // supportDetail.setLite_ID(count);
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

            //supportDetail.save();
            supportDetail.updateAll("lite_ID = ?", String.valueOf(count));

            //保存数据到表SupportChecked
            SupportChecked supportChecked = new SupportChecked();
            // supportChecked.setLite_ID(count);
            supportChecked.setIsRoom(mCheckedBean_Q.getIsRoom());
            supportChecked.setIsFree(mCheckedBean_Q.getIsFree());
            supportChecked.setConclusion(mCheckedBean_Q.getConclusion());
            supportChecked.setDescription(mCheckedBean_Q.getDescription());
            supportChecked.setSiteChecks(mCheckedBean_Q.getSiteChecks());
            supportChecked.setSiteLogin(mCheckedBean_Q.getSiteLogin());
            //supportChecked.save();
            supportChecked.updateAll("lite_ID = ?", String.valueOf(count));

            if (GlobalManager.TYPE_DRAFT_LITE.equals(lite_type)) {
                SupportMedia supportMedia = new SupportMedia();
                ArrayList<String> photoTypes = new ArrayList<>();
                ArrayList<String> paths = new ArrayList<>();
                ArrayList<String> pictureTypes = new ArrayList<>();
                ArrayList<Long> mDurations = new ArrayList<>();
                ArrayList<Integer> nums = new ArrayList<>();
                ArrayList<Integer> mimeTypes = new ArrayList<>();
                ArrayList<Integer> widths = new ArrayList<>();
                ArrayList<Integer> heights = new ArrayList<>();
                ArrayList<Integer> positions = new ArrayList<>();
                if (mLocalMedias_sanzheng_Q != null && mLocalMedias_sanzheng_Q.size() != 0) {
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
                        photoTypes.add(GlobalManager.PHOTO_TYPE_SANZHENG);
                    }
                }
                if (mLocalMedias_cheshen_Q != null && mLocalMedias_cheshen_Q.size() != 0) {
                    for (int i = 0; i < mLocalMedias_cheshen_Q.size(); i++) {
                        LocalMedia media = mLocalMedias_cheshen_Q.get(i);
                        paths.add(media.getPath());
                        pictureTypes.add(media.getPictureType());
                        mDurations.add(media.getDuration());
                        nums.add(media.getNum());
                        heights.add(media.getHeight());
                        widths.add(media.getWidth());
                        positions.add(media.getPosition());
                        mimeTypes.add(media.getMimeType());
                        photoTypes.add(GlobalManager.PHOTO_TYPE_CHESHEN);
                    }
                }
                if (mSelectList_huowu != null && mSelectList_huowu.size() != 0) {
                    for (int i = 0; i < mSelectList_huowu.size(); i++) {
                        LocalMedia media = mSelectList_huowu.get(i);
                        paths.add(media.getPath());
                        pictureTypes.add(media.getPictureType());
                        mDurations.add(media.getDuration());
                        nums.add(media.getNum());
                        heights.add(media.getHeight());
                        widths.add(media.getWidth());
                        positions.add(media.getPosition());
                        mimeTypes.add(media.getMimeType());
                        photoTypes.add(GlobalManager.PHOTO_TYPE_HUOZHAO);
                    }
                }

                supportMedia.setPaths(paths);
                supportMedia.setPhotoTypes(photoTypes);
                supportMedia.setPictureTypes(pictureTypes);
                supportMedia.setDurations(mDurations);
                supportMedia.setNums(nums);
                supportMedia.setHeights(heights);
                supportMedia.setWidths(widths);
                supportMedia.setMimeTypes(mimeTypes);
                supportMedia.setPositions(positions);
                // supportMedia.setLite_ID(count);
                //supportMedia.save();
                supportChecked.updateAll("lite_ID=?", String.valueOf(count));

                support.setSupportMedia(supportMedia);
            }

            support.setSupportScan(supportScan);
            support.setSupportDetail(supportDetail);
            support.setSupportChecked(supportChecked);

            //  support.save();
            support.updateAll("lite_ID = ?", String.valueOf(count));

            if (action.equals(ACTION_SUBMIT)) {
                if (GlobalManager.TYPE_DRAFT_LITE.equals(lite_type)) {
                    //SPUtils.add_one(sActivity, SPUtils.MATH_DRAFT_LITE);
                    ToastUtils.singleToast("提交失败并更新至草稿列表");
                } else if (GlobalManager.TYPE_SUBMIT_LITE.equals(lite_type)) {
                    SPUtils.add_one(sActivity, SPUtils.MATH_SUBMIT_LITE);
                    SPUtils.cut_one(sActivity, SPUtils.MATH_DRAFT_LITE);
                    ToastUtils.singleToast("提交成功并更新至提交列表");
                }


            } else if (action.equals(ACTION_SAVE)) {
                ToastUtils.singleToast("已更新至草稿列表");
                //  SPUtils.add_one(sContext_draft, SPUtils.MATH_DRAFT_LITE);
            }
        }
        Intent intent = new Intent("com.example.updateUI");
        intent.putExtra(ARG_BROADCAST_DRAFT, (int) SPUtils.get(sActivity, SPUtils.MATH_DRAFT_LITE, 0));
        intent.putExtra(ARG_BROADCAST_SUBMIT, (int) SPUtils.get(sActivity, SPUtils.MATH_SUBMIT_LITE, 0));
        sendBroadcast(intent);

    }

    private void postPictureAndJson(String postTime) {

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
            List<String> siteChecks = mCheckedBean_Q.getSiteChecks();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < siteChecks.size(); i++) {
                builder.append("-" + siteChecks.get(i));
            }
            info.setIsFree(mCheckedBean_Q.getIsFree());
            info.setIsRoom(mCheckedBean_Q.getIsRoom());
            info.setConclusion(mCheckedBean_Q.getConclusion());
            info.setDescription(mCheckedBean_Q.getDescription());
            info.setSiteCheck(builder.toString());
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

        if (info.getColor() == null || "".equals(info.getColor())) {
            ToastUtils.singleToast("请确定车牌颜色");
            return;
        }
        if (info.getNumber() == null || "".equals(info.getNumber())) {
            ToastUtils.singleToast("请确定车牌号");
            return;
        }
        if (info.getGoods() == null || "".equals(info.getGoods())) {
            ToastUtils.singleToast("请确定货物名称");
            return;
        }
        if (info.getSiteCheck() == null || "".equals(info.getSiteCheck())) {
            ToastUtils.singleToast("请确定现场检查人");
            return;
        }
        if (info.getSiteLogin() == null || "".equals(info.getSiteLogin())) {
            ToastUtils.singleToast("请确定现场登记人");
            return;
        }
        if (info.getConclusion() == null || "".equals(info.getConclusion())) {
            ToastUtils.singleToast("请确定选择检查结论");
            return;
        }
        if (info.getScan_code() == null || "".equals(info.getScan_code())) {
            ToastUtils.singleToast("请扫描出口流水号二维码得到更多的信息");
            return;
        }

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

        List<MultipartBody.Part> sanzheng = null;
        List<MultipartBody.Part> cheshen = null;
        List<MultipartBody.Part> huozhao = null;

        if (pathTitle_sanzheng == null || pathTitle_sanzheng.size() < 3) {
            ToastUtils.singleToast("请拍摄或选择三张及以上的三证照片");
            return;
        }
        if (pathTitle_cheshen == null || pathTitle_cheshen.size() < 2) {
            ToastUtils.singleToast("请拍摄或选择两张及以上的车身车型照片");
            return;
        }
        if (pathTitle_huozhao == null || pathTitle_huozhao.size() < 2) {
            ToastUtils.singleToast("请拍摄或选择两张及以上的货物照片");
            return;
        }

        sActivity.notifyDataChangeAndFinish();
        Intent intent = new Intent(sActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


        sanzheng = getBodyPart1(pathTitle_sanzheng, "sanzheng");
        cheshen = getBodyPart1(pathTitle_cheshen, "cheshen");
        huozhao = getBodyPart1(pathTitle_huozhao, "huozhao");


        Gson gson = new Gson();
        String route = gson.toJson(info);

        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), route);
        Logger.i("json  string" + route);

        List<MultipartBody.Part> finalSanzheng = sanzheng;
        List<MultipartBody.Part> finalCheshen = cheshen;
        List<MultipartBody.Part> finalHuozhao = huozhao;


        Subscriber<HttpResultCode> subscriber_json = new Subscriber<HttpResultCode>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.i(e.getMessage());
                save2Litepal(mSubmitTime, GlobalManager.TYPE_DRAFT_LITE, ACTION_SUBMIT, mShowType_submit);
                Logger.i("json未上传成功走了草稿保存的方法");
                ToastUtils.singleToast("提交失败,已保存至草稿");
            }

            @Override
            public void onNext(HttpResultCode httpResultCode) {
                int code = httpResultCode.getCode();
                Logger.i(code + "----json");
                if (code == 200) {
                    Logger.i("json上传成功");

                    RequestPicture.getInstance().postPicture(new Subscriber<HttpResultCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.i(e.getMessage());
                            save2Litepal(mSubmitTime, GlobalManager.TYPE_DRAFT_LITE, ACTION_SUBMIT, mShowType_submit);
                            Logger.i("json上传成功图片未上传成功走了草稿保存的方法");
                            ToastUtils.singleToast("提交失败,已保存至草稿");
                        }

                        @Override
                        public void onNext(HttpResultCode httpResultCode) {
                            int code = httpResultCode.getCode();
                            Logger.i(code + "----photo");

                            if (code == 200) {
                                // ToastUtils.singleToast("图片上传成功");
                                Logger.i("图片上传成功");
                                save2Litepal(mSubmitTime, GlobalManager.TYPE_SUBMIT_LITE, ACTION_SUBMIT, mShowType_submit);
                                Logger.i("走了提交保存的方法");
                            } else {
                                save2Litepal(mSubmitTime, GlobalManager.TYPE_DRAFT_LITE, ACTION_SUBMIT, mShowType_submit);
                                Logger.i("图片上传返回错误走了草稿保存的方法");
                            }
                        }
                    }, postTime, finalSanzheng, finalCheshen, finalHuozhao);

                } else {
                    Logger.i("json上传返回错误走了草稿保存的方法");
                    save2Litepal(mSubmitTime, GlobalManager.TYPE_DRAFT_LITE, ACTION_SUBMIT, mShowType_submit);
                }
                //  ToastUtils.singleToast("json上传成功");
            }
        };
        RequestJson.getInstance().postJson(subscriber_json, body);


    }

    public static List<MultipartBody.Part> getBodyPart1(List<PathTitleBean> bitmapList, String type) {

        MultipartBody.Builder builder = new MultipartBody.Builder();


        for (int i = 0; i < bitmapList.size(); i++) {

            String mFilePath_str_new = null;
            try {
                Bitmap bitmap = BitmapUtil.convertToBitmap(bitmapList.get(i).getPath(), 700, 960);

                mFilePath_str_new = mFilePath_str + "/" + TimeUtil.getCurrentTime()
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
}
