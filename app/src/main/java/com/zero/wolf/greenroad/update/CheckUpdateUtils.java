package com.zero.wolf.greenroad.update;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.bean.UpdateAppInfo;
import com.zero.wolf.greenroad.https.HttpUtilsApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * User: Losileeya (847457332@qq.com)
 * Date: 2016-09-27
 * Time: 15:29
 * 类描述：
 *
 * @version :
 */
public class CheckUpdateUtils {
    private static final String BASEURL="http://192.168.2.122/lvsetondao/index.php/Home/Apiversion/";

    /**
     * 检查更新
     */
    @SuppressWarnings("unused")
    public static void checkUpdate(String appCode, String curVersion, final CheckCallBack updateCallback) {

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .build();
        HttpUtilsApi utilsApi = adapter.create(HttpUtilsApi.class);
        utilsApi.update("GreenRoad.apk","1.0")//测试使用
                //   .apiService.getUpdateInfo(appCode, curVersion)//开发过程中可能使用的
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateAppInfo>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("更新app完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("更新app" + e.getMessage());
                    }
                    @Override
                    public void onNext(UpdateAppInfo updateAppInfo) {
                        Logger.i("更新app" + updateAppInfo.toString());
                        if (updateAppInfo.getCode() == 201 || updateAppInfo.getData() == null ||
                                updateAppInfo.getData().getDownloadurl() == null) {
                            updateCallback.onError(); // 失败
                        } else {
                            updateCallback.onSuccess(updateAppInfo);
                        }
                    }
                });
    }


    public interface CheckCallBack{
        void onSuccess(UpdateAppInfo updateInfo);
        void onError();
    }


}
