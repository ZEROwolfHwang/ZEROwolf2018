package com.android.htc.greenroad.https;


import com.android.htc.greenroad.httpresultbean.HttpResultGoods;

import rx.Observable;
import rx.Subscriber;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/7/9 9:30
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/7/9
 * @updataDes ${描述更新内容}
 */

public class RequestGoods {

    public static RequestGoods sRequestGoods;

    public RequestGoods() {

    }

    public static RequestGoods getInstance() {
        if (sRequestGoods == null) {
            sRequestGoods = new RequestGoods();
        }
        return sRequestGoods;
    }

    public void postGoods(Subscriber<HttpResultGoods> subscriber,String markTime) {
        Observable<HttpResultGoods> observable = HttpMethods.getInstance().
                getApi().postGoods(markTime);

        HttpMethods.getInstance().toSubscribe(observable, subscriber);

    }
}
