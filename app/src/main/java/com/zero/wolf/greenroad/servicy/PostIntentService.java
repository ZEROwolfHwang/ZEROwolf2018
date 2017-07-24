package com.zero.wolf.greenroad.servicy;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.activity.SaveToLocation;
import com.zero.wolf.greenroad.bean.PostContent;
import com.zero.wolf.greenroad.httpresultbean.HttpResultPostImg;
import com.zero.wolf.greenroad.https.HttpMethods;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;
import com.zero.wolf.greenroad.manager.CarNumberCount;
import com.zero.wolf.greenroad.tools.ArrayCollector;
import com.zero.wolf.greenroad.tools.PathUtil;
import com.zero.wolf.greenroad.tools.RxHolder;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.Subscriber;

import static org.litepal.LitePalApplication.getContext;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PostIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_POST_CONTENT = "com.zero.wolf.greenroad.servicy.action.POST_CONTENT";
    private static final String ACTION_NOT_POST = "com.zero.wolf.greenroad.servicy.action.NOT_POST";

    // TODO: Rename parameters
    private static final String EXTRA_POST_CONTENT = "com.zero.wolf.greenroad.servicy.extra.POST_CONTENT";
    private static final String EXTRA_NOT_POST = "com.zero.wolf.greenroad.servicy.extra.NOT_POST";


    private static String LOCAL_BROADCAST = "com.zero.wolf.greenroad.LOCAL_BROADCAST";


    private String mCar_goods;
    private String mColor;
    private String mCar_number;
    private String mCar_station;
    private String mCar_type;
    private String mCurrentTime;
    private String mUsername;
    private List<MultipartBody.Part> mParts;
    private String mPhotoPath1;
    private String mPhotoPath2;
    private String mPhotoPath3;
    private static AppCompatActivity sActivity;
    private String mStatiomName;
    private static AppCompatActivity mMainActivity;
    private Subscriber<HttpResultPostImg> mNotPostImgSubscriber;
    private Subscriber<HttpResultPostImg> mPostImgSubscriber;
    private NotPostReceiver mPostReceiver;
    private ArrayList<String> mSuccessArrayList = new ArrayList<>();

    private ArrayList<String> mFailedArrayList;


    public PostIntentService() {
        super("PostIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionNotPost(AppCompatActivity activity) {
        mMainActivity = activity;
        Intent intent = new Intent(activity, PostIntentService.class);
        intent.setAction(ACTION_NOT_POST);
        activity.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionPost(AppCompatActivity activity, PostContent content) {
        sActivity = activity;
        Intent intent = new Intent(sActivity, PostIntentService.class);
        intent.setAction(ACTION_POST_CONTENT);
        intent.putExtra(EXTRA_POST_CONTENT, content);
        sActivity.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POST_CONTENT.equals(action)) {
                PostContent post_content = intent.getParcelableExtra(EXTRA_POST_CONTENT);
                mStatiomName = post_content.getStatiomName();
                mCar_goods = post_content.getCar_goods();
                mColor = post_content.getColor();
                mCar_number = post_content.getCar_number();
                mCar_station = post_content.getCar_station();
                mCar_type = post_content.getCar_type();
                mCurrentTime = post_content.getCurrentTime();
                mUsername = post_content.getUsername();
                mPhotoPath1 = post_content.getPhotoPath1();
                mPhotoPath2 = post_content.getPhotoPath2();
                mPhotoPath3 = post_content.getPhotoPath3();
                mParts = post_content.getParts();
                Logger.i(mCar_goods + "000000" + mColor);

                Observable<HttpResultPostImg> observable = HttpMethods.getInstance().getApi()
                        .postThreeImg(mCar_type, mColor, mCurrentTime, mUsername,
                                mCar_station, mCar_number, mCar_goods, mParts);

                mPostImgSubscriber = new Subscriber<HttpResultPostImg>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("三张照片上传成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                        SaveToLocation.saveLocalLite(mCurrentTime, "卡车", mUsername, mColor,
                                mCar_number, mCar_station, mCar_goods,
                                mPhotoPath1, mPhotoPath2, mPhotoPath3);
                        ToastUtils.singleToast("上传失败,已保存至本地");
                    }

                    @Override
                    public void onNext(HttpResultPostImg httpResultPostImg) {
                        // backToPhotoActivity();
                        int code = httpResultPostImg.getCode();
                        String msg = httpResultPostImg.getMsg();
                        if (code == 200) {
                            // backToPhotoActivity();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            CarNumberCount.CarNumberCut(getContext());

                            ToastUtils.singleToast("车牌号为" + mCar_number + "上传成功");
                        } else {
                            SaveToLocation.saveLocalLite(mCurrentTime, "卡车", mUsername, mColor,
                                    mCar_number, mCar_station, mCar_goods,
                                    mPhotoPath1, mPhotoPath2, mPhotoPath3);
                            ToastUtils.singleToast("上传失败,已保存至本地");
                        }
                        Logger.i("" + code);
                        Logger.i("" + msg);
                    }
                };
                observable.compose(RxHolder.io_main()).subscribe(mPostImgSubscriber);

            } else if (ACTION_NOT_POST.equals(action)) {

                //initLocalBroadCast();
                //mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
                //  Intent intent1 = new Intent("com.zero.wolf.greenroad.servicy.action.TYPE_THREAD");

                mPostReceiver = new NotPostReceiver();

                registerReceiver(mPostReceiver, new IntentFilter("com.zero.wolf.greenroad.servicy.action.TYPE_THREAD"));

                //sendServiceStatus("服务启动");

                List<SupportPhotoLite> liteList = DataSupport
                        //.where("is_post==?", "NO").find(SupportPhotoLite.class);
                        .findAll(SupportPhotoLite.class);
                int size = liteList.size();
                Logger.i("未上传车辆" + size);

                mFailedArrayList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
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
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    List<MultipartBody.Part> parts = PathUtil
                            .getMultipartBodyPart(photoPath1, photoPath2, photoPath3);
                    Observable<HttpResultPostImg> observable = HttpMethods.getInstance()
                            .getApi().postThreeImg("大货车", license_color,
                                    shuttime, username, station, license_plate, goods, parts);


                    mNotPostImgSubscriber = new Subscriber<HttpResultPostImg>() {
                        @Override
                        public void onCompleted() {

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
                                //sendServiceStatus("success");
                                //Logger.i("第" + finalI + license_plate + "---上传成功");
                                // TODO: 2017/7/24 取消删除数据库.
                                // CarNumberCount.CarNumberCut(mMainActivity);
                                // DataSupport.deleteAll(SupportPhotoLite.class, "shuttime=?", shuttime);
                                //  ArrayCollector.addString(license_plate);
                                // mSuccessArrayList.add(license_plate);

                            } else {
                                if (code == 300) {
                                    ToastUtils.singleToast("上传失败");
                                    mFailedArrayList.add(license_plate);
                                }

                            }

                        }
                    };
                    observable.compose(RxHolder.io_main()).subscribe(mNotPostImgSubscriber);

                }

                Logger.i(ArrayCollector.getList().toString());

            }
            Logger.i(ArrayCollector.getList().toString());


        }
        Logger.i(ArrayCollector.getList().toString());

    }

    /**
     * 取消掉订阅事件
     */
    @Override
    public void onDestroy() {

        super.onDestroy();

        List<SupportPhotoLite> liteList = DataSupport
                //.where("is_post==?", "NO").find(SupportPhotoLite.class);
                .findAll(SupportPhotoLite.class);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < liteList.size(); i++) {
            strings.add(liteList.get(i).getLicense_plate());
        }

        sendServiceStatus(strings);

        if (mPostImgSubscriber != null && mPostImgSubscriber.isUnsubscribed()) {
            mPostImgSubscriber.unsubscribe();
        }
        if (mNotPostImgSubscriber != null && mNotPostImgSubscriber.isUnsubscribed()) {
            mNotPostImgSubscriber.unsubscribe();
        }

        if (mPostReceiver != null) {
            unregisterReceiver(mPostReceiver);
        }

    }

    // 发送服务状态信息
    private void sendServiceStatus(ArrayList<String> list) {
        Intent intent = new Intent("com.zero.wolf.greenroad.servicy.action.TYPE_THREAD");
        intent.putExtra("status", list);
        sendBroadcast(intent);
    }

    // 发送线程状态信息
    private void sendThreadStatus(String status, ArrayList<String> progress) {
        Intent intent = new Intent("com.zero.wolf.greenroad.servicy.action.TYPE_THREAD");
        intent.putExtra("status", status);
        intent.putExtra("progress", progress);
        sendBroadcast(intent);
    }

 /*   private void initLocalBroadCast() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent(LOCAL_BROADCAST);
        mLocalBroadcastManager.sendBroadcast(intent);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(LOCAL_BROADCAST);
        mPostReceiver = new NotPostReceiver();
        mLocalBroadcastManager.registerReceiver(mPostReceiver, mIntentFilter);
    }*/


}
