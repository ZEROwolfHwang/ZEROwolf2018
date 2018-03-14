package com.android.htc.greenroad.servicy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.htc.greenroad.R;
import com.android.htc.greenroad.httpresultbean.HttpResultPolling;
import com.android.htc.greenroad.https.RequestPolling;
import com.android.htc.greenroad.litepalbean.SupportDraftOrSubmit;
import com.android.htc.greenroad.manager.GlobalManager;
import com.android.htc.greenroad.tools.SPUtils;
import com.android.htc.greenroad.tools.TimeUtil;
import com.android.htc.greenroad.tools.ToastUtils;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;

public class PollingService extends Service {
    private Timer mTimer;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    SupportDraftOrSubmit submit = (SupportDraftOrSubmit) message.obj;
                    android.support.v7.app.NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(mContext)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(submit.getSupportDetail().getDetail_carType()
                                    + submit.getSupportDetail().getNumber() + "审核通过")
                            .setContentText("当前审核通过的车辆:" + "\n"
                                    + "车  型:" + submit.getSupportDetail().getDetail_carType() + "  车牌号:" + submit.getSupportDetail().getDetail_carType() + "\n"
                                    + "上传时间:" + submit.getCurrent_time()+ "\n"
                                    + "不通过原因:" + submit.getSupportChecked().getSiteLogin()
                            )
                            .setAutoCancel(true)
                            .setOnlyAlertOnce(true)
                            // 需要VIBRATE权限
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setPriority(Notification.PRIORITY_DEFAULT);

                    NotificationManager notificationManager = (NotificationManager) mContext
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(TimeUtil.getTimeId(), builder.build());

                    break;

                case 2:
                    break;
            }
            return false;
        }
    });
    private Context mContext;
    private String mUsername;

    @Override
    public void onCreate() {
        Logger.i("TestService -> onCreate, Thread ID: " + Thread.currentThread().getId());
        super.onCreate();



        mUsername = (String) SPUtils.get(this, SPUtils.lOGIN_USERNAME, "qqqq");

        mContext = this;
        mTimer = new Timer();
        Task task = new Task();
        //schedule 计划安排，时间表 period
        mTimer.schedule(task, 1 * 1000, 20 * 1000);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i("TestService -> onStartCommand, startId: " + startId + ", Thread ID: " + Thread.currentThread().getId());
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.i("TestService -> onBind, Thread ID: " + Thread.currentThread().getId());
        return null;
    }

    @Override
    public void onDestroy() {
        Logger.i("TestService -> onDestroy, Thread ID: " + Thread.currentThread().getId());
        super.onDestroy();

        Logger.i("PollingService onDestroy");
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    public class Task extends TimerTask {
        @Override
        public void run() {
//            String currentTime = TimeUtil.getCurrentTimeToDate();
//
//            int lastTime = TimeUtil.getLastTime(currentTime);
//            Logger.i("lastTime=" + lastTime + "-----" + "currentTime=" + currentTime);
//
//            String current_time = DataSupport.findFirst(SupportDraftOrSubmit.class).getCurrent_time();
//            Logger.i(current_time);

            List<SupportDraftOrSubmit> submitList = DataSupport.where("username = ? and current_date = ? and lite_type = ? and isPass = ?",
                    mUsername, TimeUtil.getStringDateShort(), GlobalManager.TYPE_SUBMIT_LITE, "0").find(SupportDraftOrSubmit.class);

            if (submitList.size() != 0) {
//                ArrayList<Integer> pollingList = new ArrayList<>();

                Logger.i("submitList.size()----" + submitList.size() + "");
                int[] pollingList = new int[submitList.size()];

                int[] a = new int[]{1293812, 2103123};

//                String pollingStr = "";
                StringBuffer buffer = new StringBuffer();

//                pollingList[1] = 0;
                for (int i = 0; i < submitList.size(); i++) {
                    if (i < submitList.size() - 1) {

                        buffer.append(submitList.get(i).getLite_ID() + ",");
                    } else {
                        buffer.append(submitList.get(i).getLite_ID() + "");

                    }
//                    Logger.i(submitList.get(i).getLite_ID() + "");
//                    pollingList[i] = submitList.get(i).getLite_ID();
                }
                String pollingStr = buffer.toString();
                Logger.i(pollingStr);
//                for (int i = 0; i < pollingList.length; i++) {
//                    Logger.i("发送过去的liteId字段数组"+pollingList[i]+"");
//                }

                Subscriber<HttpResultPolling> subscriber = new Subscriber<HttpResultPolling>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("错误" + e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultPolling httpResultPolling) {
                        Logger.i(httpResultPolling.toString());

                        int code = httpResultPolling.getCode();
                        List<Integer> liteIdList = httpResultPolling.getData();
                        if (code == 200) {
                            //当天提交的未审核通过的车辆
                            List<SupportDraftOrSubmit> submitList = DataSupport.where("username = ? and current_date = ? and lite_type = ? and isPass = ?",
                                    mUsername, TimeUtil.getStringDateShort(), GlobalManager.TYPE_SUBMIT_LITE, "0").find(SupportDraftOrSubmit.class);

                            Logger.i("submitList.size()" + submitList.size() + "");

                            for (int i = 0; i < submitList.size(); i++) {
                                Logger.i(submitList.get(i).toString());
                            }

                            for (int i = 0; i < submitList.size(); i++) {

                                for (int j = 0; j < liteIdList.size(); j++) {
                                    Logger.i("liteIdList.size()" + liteIdList.size() + "11111");

                                    if (submitList.get(i).getLite_ID() == liteIdList.get(j)) {
                                        Logger.i("走了多少遍");
                                        SupportDraftOrSubmit supportSubmit = new SupportDraftOrSubmit();
                                        supportSubmit.setCurrent_date(submitList.get(i).getCurrent_date());
                                        supportSubmit.setCurrent_time(submitList.get(i).getCurrent_time());
                                        supportSubmit.setUsername(submitList.get(i).getUsername());
                                        supportSubmit.setLite_type(submitList.get(i).getLite_type());
                                        supportSubmit.setSupportChecked(submitList.get(i).getSupportChecked());
                                        supportSubmit.setSupportDetail(submitList.get(i).getSupportDetail());
                                        supportSubmit.setSupportMedia(submitList.get(i).getSupportMedia());
                                        supportSubmit.setSupportScan(submitList.get(i).getSupportScan());
                                        supportSubmit.setIsPass(1);
                                        supportSubmit.setLite_ID(submitList.get(i).getLite_ID());
                                        supportSubmit.updateAll("current_date = ? and lite_ID = ?", TimeUtil.getStringDateShort(), liteIdList.get(j) + "");

                                    }
                                }

                            }


                            Logger.i("liteIdList.size()" + liteIdList.size() + "22222");
                            for (int i = 0; i < liteIdList.size(); i++) {
                                List<SupportDraftOrSubmit> submits = DataSupport.where("lite_ID = ? and lite_type = ?", liteIdList.get(i) + "", GlobalManager.TYPE_SUBMIT_LITE).find(SupportDraftOrSubmit.class);

                                Logger.i(submits.get(0).toString());

                                Message message = Message.obtain();
                                message.what = 1;
                                message.obj = submits.get(0);
                                mHandler.sendMessage(message);
                                Log.e("AAA", "开始执行执行timer定时任务...");

//                                SupportDraftOrSubmit supportDraftOrSubmit = new SupportDraftOrSubmit();
//                                supportDraftOrSubmit.setLite_ID(0);
//                                supportDraftOrSubmit.updateAll("lite_ID = ? and lite_type = ?", liteIdList.get(i) + "", GlobalManager.TYPE_SUBMIT_LITE);

                            }
                        }
                    }
                };

                //请求返回的是当天才审核通过的车辆唯一标识
                RequestPolling.getInstance().postPolling(subscriber, pollingStr);

            } else {

                Message message = Message.obtain();
                message.what = 2;
                message.obj = "你好";
                mHandler.sendMessage(message);
                Log.e("BBB", "不发送通知");
                Logger.i("当天没有未审核通过的车辆");
                ToastUtils.singleToast("当天没有未审核通过的车辆");
            }
        }
    }
}
