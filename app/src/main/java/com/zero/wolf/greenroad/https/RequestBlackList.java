package com.zero.wolf.greenroad.https;


import com.zero.wolf.greenroad.httpresultbean.HttpResultBlack;

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

public class RequestBlackList {

    public static RequestBlackList sRequestBlackList;

    public RequestBlackList() {

    }

    public static RequestBlackList getInstance() {
        if (sRequestBlackList == null) {
            sRequestBlackList = new RequestBlackList();
        }
        return sRequestBlackList;
    }

    public void getBlackList(Subscriber<List<HttpResultBlack.DataBean>> subscriber) {
        Observable<List<HttpResultBlack.DataBean>> observable = HttpMethods.getInstance().getApi().getBlack()
//                .map(new HttpNumberFunc<>());
                .map(new Func1<HttpResultBlack<List<HttpResultBlack.DataBean>>, List<HttpResultBlack.DataBean>>() {
                    @Override
                    public List<HttpResultBlack.DataBean> call(HttpResultBlack<List<HttpResultBlack.DataBean>> listHttpResultBlack) {
                        return listHttpResultBlack.getData();
                    }
                });

        HttpMethods.getInstance().toSubscribe(observable,subscriber);

    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpNumberFunc<T> implements Func1<HttpResultBlack<T>, T> {

        @Override
        public T call(HttpResultBlack<T> tHttpResultBlack) {
            return tHttpResultBlack.getData();
        }
    }
}
