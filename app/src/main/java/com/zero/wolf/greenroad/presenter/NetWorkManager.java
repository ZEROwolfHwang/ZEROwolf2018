package com.zero.wolf.greenroad.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zero.wolf.greenroad.bean.RequestResult;
import com.zero.wolf.greenroad.manager.URLManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/7/5 14:09
 * @updateAuthor ${Author}
 * @updataTIme 2016/7/5
 * @updataDes ${描述更新内容}
 */
public class NetWorkManager {

    private static NetWorkManager mNetWorkManager;

    private final HttpUtilsApi mHttpUtilsApi;
    private static final int DEFAUTL_TIMEOUT = 5;

    private NetWorkManager() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.connectTimeout(DEFAUTL_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAUTL_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAUTL_TIMEOUT, TimeUnit.SECONDS);
        mHttpUtilsApi = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(URLManager.BASEURL)
                .build()
                .create(HttpUtilsApi.class);

    }

    public static NetWorkManager getInstane() {

        if (mNetWorkManager == null) {
            synchronized (NetWorkManager.class) {
                mNetWorkManager = new NetWorkManager();
            }
        }
        return mNetWorkManager;
    }

    /**
     * 激活
     *
     * @param body 激活码以及机器数据
     * @return
     */
    public Observable<RequestResult> verificationCode(ActivationRequestBody body) {
        return mHttpUtilsApi.activation(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 更新app
     *
     * @param language 根据语言获取更新日志
     * @return
     */
    public Observable<UpdateApp> updateApp(String language) {
//        return mHttpUtilsApi.update(language)
//                .compose(RxHolder.<UpdateApp>io_main());
        return mHttpUtilsApi.update(language);
    }


  /*  public Observable<RequestResult> updateLogInfo(LogBean logBean) {
        return mHttpUtilsApi.updateLog(logBean)
                .subscribeOn(Schedulers.newThread());

    }*/


    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public boolean isnetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager maneger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = maneger.getActiveNetworkInfo();
            if (activeNetworkInfo != null)
                return activeNetworkInfo.isAvailable();
        }
        return false;

    }

    /**
     * 检测机器是否登录过
     *
     * @param bean
     * @return
     */
    public Observable<RequestResult> checkLogin(GetActivationCodeBean bean) {
        return mHttpUtilsApi.checkLogin(bean);
//                .compose(RxHolder.io_main());
    }

}
