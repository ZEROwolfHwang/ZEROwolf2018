package com.zero.wolf.greenroad.servicy;

import android.app.IntentService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.activity.SaveToLocation;
import com.zero.wolf.greenroad.bean.PostContent;
import com.zero.wolf.greenroad.httpresultbean.HttpResultPostImg;
import com.zero.wolf.greenroad.https.HttpMethods;
import com.zero.wolf.greenroad.manager.CarNumberCount;
import com.zero.wolf.greenroad.tools.RxHolder;
import com.zero.wolf.greenroad.tools.ToastUtils;

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
    //private static final String ACTION_BAZ = "com.zero.wolf.greenroad.servicy.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_POST_CONTENT = "com.zero.wolf.greenroad.servicy.extra.POST_CONTENT";
   // private static final String EXTRA_PARAM2 = "com.zero.wolf.greenroad.servicy.extra.PARAM2";
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
    /*public static void startActionFoo(Context context, PostContent content) {
        Intent intent = new Intent(context, PostIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(POST_CONTENT, content);
        context.startService(intent);
    }*/

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

                observable.compose(RxHolder.io_main()).subscribe(new Subscriber<HttpResultPostImg>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("三张照片上传成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                        SaveToLocation.saveLocalLite(mCurrentTime,"卡车", mUsername, mColor,
                                mCar_number,mCar_station,mCar_goods,
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
                            SaveToLocation.saveLocalLite(mCurrentTime,"卡车", mUsername, mColor,
                                    mCar_number,mCar_station,mCar_goods,
                                    mPhotoPath1, mPhotoPath2, mPhotoPath3);
                            ToastUtils.singleToast("上传失败,已保存至本地");
                        }
                        Logger.i("" + code);
                        Logger.i("" + msg);
                    }
                });

            }
        }
    }

}
