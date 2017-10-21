package com.android.htc.greenroad.https;


import com.android.htc.greenroad.httpresultbean.HttpResultLineStation;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.android.htc.greenroad.https.RequestBlackList.sRequestBlackList;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/7/9 9:30
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/7/9
 * @updataDes ${描述更新内容}
 */

public class RequestLineStation {

    public static RequestLineStation sRequestLineStation;

    public RequestLineStation() {

    }

    public static RequestLineStation getInstance() {
        if (sRequestBlackList == null) {
            sRequestLineStation = new RequestLineStation();
        }
        return sRequestLineStation;
    }

    public void getLineStation(Subscriber<List<HttpResultLineStation.DataBean>> subscriber) {
        Observable<List<HttpResultLineStation.DataBean>> observable = HttpMethods.getInstance().getApi().getLines()
                .map(new Func1<HttpResultLineStation<List<HttpResultLineStation.DataBean>>, List<HttpResultLineStation.DataBean>>() {
                    @Override
                    public List<HttpResultLineStation.DataBean> call(HttpResultLineStation<List<HttpResultLineStation.DataBean>> listHttpResultLineStation) {
                        return listHttpResultLineStation.getData();
                    }
                })
                ;
//                .map(new HttpNumberFunc<>());
                /*.map(new Func1<HttpResultLineStation<List<HttpResultLineStation.DataBean>>, List<HttpResultBlack.DataBean>>() {
                    @Override
                    public List<HttpResultBlack.DataBean> call(HttpResultBlack<List<HttpResultBlack.DataBean>> listHttpResultBlack) {
                        return listHttpResultBlack.getData();
                    }
                });*/

        HttpMethods.getInstance().toSubscribe(observable,subscriber);

    }

}
