package com.android.htc.greenroad.https;

import com.android.htc.greenroad.bean.ActivationResult;
import com.android.htc.greenroad.bean.UpdateAppInfo;
import com.android.htc.greenroad.httpresultbean.HttpResult;
import com.android.htc.greenroad.httpresultbean.HttpResultBlack;
import com.android.htc.greenroad.httpresultbean.HttpResultCode;
import com.android.htc.greenroad.httpresultbean.HttpResultLane;
import com.android.htc.greenroad.httpresultbean.HttpResultLineStation;
import com.android.htc.greenroad.httpresultbean.HttpResultLoginName;
import com.android.htc.greenroad.httpresultbean.HttpResultMacInfo;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/7/4 17:09
 * @updateAuthor ${Author}
 * @updataTIme 2016/7/4
 * @updataDes ${描述更新内容}
 */
public interface HttpUtilsApi {

    //激活码

    @FormUrlEncoded
    @POST("Api/activation")
    Observable<ActivationResult> activation(@Field("macID") String macId,
                                            @Field("macName") String macName,
                                            @Field("regKey") String regKey);

    @FormUrlEncoded
    @POST("Login/listapi")
    Call<HttpResultLoginName> login(@Field("name") String name, @Field("password") String password);

    @GET("Api/number")
    Observable<HttpResultBlack<List<HttpResultBlack.DataBean>>> getNumberInfo();


    @GET("Apiversion/update")
    Observable<UpdateAppInfo> update(@Query("appname") String appname,
                                     @Query("appversion") String appversion);


    @FormUrlEncoded
    @POST("Apionline/online")
    Observable<HttpResultCode> polling(@Field("polling") String polling);

    //@Headers({"Content-type:application/json;charset=UTF-8"})
    @Multipart
    @POST("task")
    Observable<HttpResultCode> task(@PartMap Map<String, RequestBody> partMap,
                                    @Part List<MultipartBody.Part> file);

    @Multipart
    @POST("Api/picture")
    Observable<HttpResultCode> postPicture(
            @Part("post_time") String postTime,
            @Part List<MultipartBody.Part> sanzheng,
            @Part List<MultipartBody.Part> cheshen,
            @Part List<MultipartBody.Part> huozhao);

    @POST("Api/json")
    Observable<HttpResultCode> postJson(@Body RequestBody info);

    @FormUrlEncoded
    @POST("Login/register")
    Observable<HttpResult> postRegistered(
            @Field("register_road") String road,
            @Field("register_station") String station,
            @Field("register_code") String code,
            @Field("register_name") String name,
            @Field("register_psw") String psw,
            @Field("register_mac") String macId
    );

    @FormUrlEncoded
    @POST("Login/login")
    Observable<HttpResult> postLogin(
            @Field("login_name") String name,
            @Field("login_psw") String psw,
            @Field("login_mac") String macId
    );


    @GET("Api/black")
    Observable<HttpResultBlack<List<HttpResultBlack.DataBean>>> getBlack();

//    @Headers({
//            "Content-Type: application/json;charset=utf-8",
//            "Accept: application/json"
//    })
    @GET("Api/linestations")
    Observable<HttpResultLineStation<List<HttpResultLineStation.DataBean>>> getLines();
//  @GET("Api/linestations")
//    Call<HttpResultLineStation> getLines();

    @FormUrlEncoded
    @POST("Api/lane")
    Observable<HttpResultLane<List<HttpResultLane.DataBean>>> getLanes(@Field("station") String station);

    @FormUrlEncoded
    @POST("Api/macInfo")
    Observable<HttpResultMacInfo> getMacInfo(@Field("macId") String macId);

}