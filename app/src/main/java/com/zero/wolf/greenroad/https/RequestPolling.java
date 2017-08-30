package com.zero.wolf.greenroad.https;


import com.zero.wolf.greenroad.httpresultbean.HttpResultCode;

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

public class RequestPolling {

    public static RequestPolling sPolling;

    public RequestPolling() {

    }

    public static RequestPolling getInstance() {
        if (sPolling == null) {
            sPolling = new RequestPolling();
        }
        return sPolling;
    }

    public void postPolling(Subscriber<HttpResultCode> subscriber, String polling) {
        Observable<HttpResultCode> observable = HttpMethods.getInstance().getApi().polling(polling);

        HttpMethods.getInstance().toSubscribe(observable,subscriber);

    }

}
