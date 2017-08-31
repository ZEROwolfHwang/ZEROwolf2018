package com.zero.wolf.greenroad.servicy;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.httpresultbean.HttpResultBlack;
import com.zero.wolf.greenroad.https.RequestBlackList;
import com.zero.wolf.greenroad.litepalbean.SupportBlack;

import org.litepal.crud.DataSupport;

import java.util.List;

import rx.Subscriber;

public class BlackListService extends IntentService {

    private static final String ACTION_BLACK_LIST = "com.zero.wolf.greenroad.blacklistservice.action.FOO";
    private static final String ACTION_BAZ = "com.zero.wolf.greenroad.blacklistservice.action.BAZ";
    private static Context sContext;


    public BlackListService() {
        super("BlackListService");
    }


    public static void startActionBlack(Context context) {
        sContext = context;
        Intent intent = new Intent(sContext, BlackListService.class);
        intent.setAction(ACTION_BLACK_LIST);

        sContext.startService(intent);
    }

    public static void startActionBaz(Context context) {
        Intent intent = new Intent(context, BlackListService.class);
        intent.setAction(ACTION_BAZ);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_BLACK_LIST.equals(action)) {
                Logger.i("走了加载黑名单数据的服务");
                RequestBlackList.getInstance().getBlackList(new Subscriber<List<HttpResultBlack.DataBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                    }

                    @Override
                    public void onNext(List<HttpResultBlack.DataBean> dataBeen) {
                        List<SupportBlack> supportBlacks = DataSupport.findAll(SupportBlack.class);
                        if (supportBlacks.size() == dataBeen.size()) {
                            return;
                        } else {
                            DataSupport.deleteAll(SupportBlack.class);
                            for (int i = 0; i < dataBeen.size(); i++) {
                                Logger.i(dataBeen.get(i).getPlate_number());
                                SupportBlack supportBlack = new SupportBlack();
                                supportBlack.setLicense(dataBeen.get(i).getPlate_number());
                                supportBlack.save();
                            }
                        }
                    }
                });
            }
        }
    }

}
