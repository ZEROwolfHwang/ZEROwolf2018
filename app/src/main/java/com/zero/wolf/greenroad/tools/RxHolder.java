package com.zero.wolf.greenroad.tools;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/1/5 11:04
 * @des  从异步线程切换到主线程
 * @updateAuthor ${Author}
 * @updataTIme 2017/1/5
 * @updataDes ${描述更新内容}
 */

public class RxHolder<T> {
    public static <T> Observable.Transformer<T, T> io_main() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
