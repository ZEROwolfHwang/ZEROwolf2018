package com.zero.wolf.greenroad.https;

import com.zero.wolf.greenroad.httpresultbean.HttpResultPostImg;
import com.zero.wolf.greenroad.bean.ActivationRequestBody;
import com.zero.wolf.greenroad.bean.ActivationResult;
import com.zero.wolf.greenroad.httpresultbean.HttpResultGoods;
import com.zero.wolf.greenroad.httpresultbean.HttpResultLoginName;
import com.zero.wolf.greenroad.bean.HttpResultNumber;
import com.zero.wolf.greenroad.httpresultbean.StationDataBean;
import com.zero.wolf.greenroad.httpresultbean.HttpResultStation;
import com.zero.wolf.greenroad.bean.UpdateAppInfo;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @Headers("Content-BaseIWBType: application/json;charset=utf-8")
    @POST("RegSrv/UBMRegService.svc/Android/regpost")
    Observable<ActivationResult> activation(@Body ActivationRequestBody body);

    /* //更新
     //TODO:需要添加UBMVersion字段用于区分是哪个版本的更新（xiahua、ruijie、pad等oem版本和分支版本）
     @Headers("Content-BaseIWBType: application/json;charset=utf-8")
     @GET("Reg/api/appupdate/update/{cityLanguage}")
     Observable<UpdateApp> update(@Path("cityLanguage") String language);

     //检测机器是否激活过
     @Headers("Content-BaseIWBType: application/json;charset=utf-8")
     @POST("RegSrv/UBMRegService.svc/Android/getregkey")
     Observable<RequestResult> checkLogin(@Body GetActivationCodeBean body);

     //崩溃日志上传
     @Headers("Content-BaseIWBType: application/json;charset=utf-8")
     @POST("RegSrv/UBMLogService.svc/Android/postlog")
     Observable<RequestResult> updateLog(@Body LogBean LogInfo);*/
  /*  @FormUrlEncoded
    @POST("listapi")
    Call<ResultCar> doPost(@Field("username") String username, @Field("psd") String psd);
*/
    @FormUrlEncoded
    @POST("listapi")
    Call<HttpResultLoginName> login(@Field("mScientificname") String name, @Field("password") String password);

    @GET("site")
    Observable<HttpResultStation<List<StationDataBean>>> getStationInfo();

    @GET("number")
    Observable<HttpResultNumber<List<HttpResultNumber.DataBean>>> getNumberInfo();

    @GET("goods")
    Observable<HttpResultGoods<List<HttpResultGoods.DataBean>>> getGoodsInfo();

    @GET("update")
    Observable<UpdateAppInfo> update(@Query("appname") String appname,
                                     @Query("appversion") String appversion);

    @Multipart
    @POST("accept")
    Observable<HttpResultPostImg> postAccept(@Part MultipartBody.Part file);

  /*  @Multipart
    @POST("image")
    Observable<AcceptResult> postOneImg(@Part MultipartBody.Part file);
*/
   /* @Multipart
    @POST("image")
    Observable<AcceptResult> postThreeImg(@PartMap Map<String, RequestBody> params);
*/
    @Multipart
    @POST("image")
    Observable<HttpResultPostImg> postThreeImg(@Part("shuttime") String shuttime,
                                               @Part("username") String username,
                                               @Part("station") String station,
                                               @Part("license_plate") String license_plate,
                                               @Part("goods") String goods,
                                               @Part List<MultipartBody.Part> file);



}
