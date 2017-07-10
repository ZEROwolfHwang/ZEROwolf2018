package com.zero.wolf.greenroad.https;


import com.zero.wolf.greenroad.httpresultbean.HttpResultStation;
import com.zero.wolf.greenroad.httpresultbean.StationDataBean;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/7/9 9:30
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/7/9
 * @updataDes ${描述更新内容}
 */

public class RequestLiteStation {

    public static RequestLiteStation sRequestLiteStation;

    public RequestLiteStation() {

    }

    public static RequestLiteStation getInstance() {
        if (sRequestLiteStation == null) {
            sRequestLiteStation = new RequestLiteStation();
        }
        return sRequestLiteStation;
    }

    public void doGetStationInfo(Subscriber<List<StationDataBean>> subscriber) {
        Observable<List<StationDataBean>> observable = HttpMethods.getInstance().getApi().getStationInfo()
                .map(new HttpStationFunc<>());

        HttpMethods.getInstance().toSubscribe(observable,subscriber);

    }
/*
    *//**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpStationFunc<T> implements Func1<HttpResultStation<T>, T> {
        @Override
        public T call(HttpResultStation<T> tHttpResultStation) {
            return tHttpResultStation.getData();
        }
    }
}
