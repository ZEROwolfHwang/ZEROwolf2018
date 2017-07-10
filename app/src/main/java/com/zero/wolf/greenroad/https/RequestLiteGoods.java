package com.zero.wolf.greenroad.https;


import com.zero.wolf.greenroad.httpresultbean.HttpResultGoods;

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

public class RequestLiteGoods {

    public static RequestLiteGoods mLiteGoods;

    public RequestLiteGoods() {

    }

    public static RequestLiteGoods getInstance() {
        if (mLiteGoods == null) {
            mLiteGoods = new RequestLiteGoods();
        }
        return mLiteGoods;
    }

    public void doGetGoodsInfo(Subscriber<List<HttpResultGoods.DataBean>> subscriber) {
        Observable<List<HttpResultGoods.DataBean>> observable = HttpMethods.getInstance().getApi().getGoodsInfo()
                .map(new HttpGoodsFunc<>());

        HttpMethods.getInstance().toSubscribe(observable,subscriber);

    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpGoodsFunc<T> implements Func1<HttpResultGoods<T>, T> {

        @Override
        public T call(HttpResultGoods<T> tHttpResultGoods) {
            return tHttpResultGoods.getData();
        }
    }
}
