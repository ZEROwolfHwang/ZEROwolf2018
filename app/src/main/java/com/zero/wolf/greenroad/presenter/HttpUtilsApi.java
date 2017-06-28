package com.zero.wolf.greenroad.presenter;

import com.zero.wolf.greenroad.bean.RequestResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/7/4 17:09
 * @updateAuthor ${Author}
 * @updataTIme 2016/7/4
 * @updataDes ${描述更新内容}
 */
public interface HttpUtilsApi {

    //更新
    //TODO:需要添加UBMVersion字段用于区分是哪个版本的更新（xiahua、ruijie、pad等oem版本和分支版本）
    @Headers("Content-BaseIWBType: application/json;charset=utf-8")
    @GET("Reg/api/appupdate/update/{cityLanguage}")
    Observable<UpdateApp> update(@Path("cityLanguage") String language);


    //激活码
    @Headers("Content-BaseIWBType: application/json;charset=utf-8")
    @POST("RegSrv/UBMRegService.svc/Android/regpost")
    Observable<RequestResult> activation(@Body ActivationRequestBody body);


    //检测机器是否激活过
    @Headers("Content-BaseIWBType: application/json;charset=utf-8")
    @POST("RegSrv/UBMRegService.svc/Android/getregkey")
    Observable<RequestResult> checkLogin(@Body GetActivationCodeBean body);


}
