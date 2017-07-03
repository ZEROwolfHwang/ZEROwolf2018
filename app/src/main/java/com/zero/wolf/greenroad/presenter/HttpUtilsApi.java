package com.zero.wolf.greenroad.presenter;

import com.zero.wolf.greenroad.bean.ActivationRequestBody;
import com.zero.wolf.greenroad.bean.ActivationResult;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
}
