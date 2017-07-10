package com.zero.wolf.greenroad.https;


import com.zero.wolf.greenroad.bean.HttpResultNumber;

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

public class RequestLiteNumber {

    public static RequestLiteNumber sRequestLiteNumber;

    public RequestLiteNumber() {

    }

    public static RequestLiteNumber getInstance() {
        if (sRequestLiteNumber == null) {
            sRequestLiteNumber = new RequestLiteNumber();
        }
        return sRequestLiteNumber;
    }

    public void doGetNumberInfo(Subscriber<List<HttpResultNumber.DataBean>> subscriber) {
        Observable<List<HttpResultNumber.DataBean>> observable = HttpMethods.getInstance().getApi().getNumberInfo()
                .map(new HttpNumberFunc<>());

        HttpMethods.getInstance().toSubscribe(observable,subscriber);

    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpNumberFunc<T> implements Func1<HttpResultNumber<T>, T> {

        @Override
        public T call(HttpResultNumber<T> tHttpResultNumber) {
            return tHttpResultNumber.getData();
        }
    }
}
