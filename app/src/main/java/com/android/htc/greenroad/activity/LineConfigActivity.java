package com.android.htc.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.htc.greenroad.R;
import com.android.htc.greenroad.httpresultbean.HttpResultLineStation;
import com.android.htc.greenroad.https.RequestLineStation;
import com.android.htc.greenroad.litepalbean.SupportLine;
import com.android.htc.greenroad.manager.GlobalManager;
import com.android.htc.greenroad.tools.SPUtils;
import com.android.htc.greenroad.tools.ToastUtils;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class LineConfigActivity extends BaseActivity {

    @BindView(R.id.text_line_config)
    EditText mTextLineConfig;
    @BindView(R.id.btn_sure_line)
    Button mBtnSureLine;
    @BindView(R.id.rl_progress_port)
    RelativeLayout mRlProgressPort;
    private String mConfigPort;
    private String mType;

    public static void actionStart(Context context,String type){
        Intent intent = new Intent(context, LineConfigActivity.class);
        intent.setType(type);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_config);
        ButterKnife.bind(this);

        //initToolbar();

        getIntentData();
        initView();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mType = intent.getType();

    }

    private void initView() {
        mConfigPort = mTextLineConfig.getText().toString();
        mBtnSureLine.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mConfigPort)) {
                SPUtils.putAndApply(this, SPUtils.CONFIG_PORT, mConfigPort);
                if (GlobalManager.LOGIN2PORT.equals(mType)) {
                    mRlProgressPort.setVisibility(View.VISIBLE);
                    initLine();
                } else {
                    finish();
                }
            }
        });
    }

    /**
     * 拿到线路以及收费站的信息保存到本地数据库
     */
    private void initLine() {
        RequestLineStation.getInstance().getLineStation(new Subscriber<List<HttpResultLineStation.DataBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.i(e.getMessage());
                mRlProgressPort.setVisibility(View.GONE);
                ToastUtils.singleToast("连接服务器失败,请检查端口配置是否正确");
            }

            @Override
            public void onNext(List<HttpResultLineStation.DataBean> dataBeen) {
                DataSupport.deleteAll(SupportLine.class);
                for (int i = 0; i < dataBeen.size(); i++) {
                    Logger.i(dataBeen.get(i).getLine() + "");
                    Logger.i(dataBeen.get(i).getStations() + "");
                    String[] stations = dataBeen.get(i).getStations().split(",");
                    ArrayList<String> stationList = new ArrayList<>();
                    for (int j = 0; j < stations.length; j++) {
                        stationList.add(stations[j]);
                    }
                    SupportLine supportLine = new SupportLine();
                    supportLine.setLine(dataBeen.get(i).getLine());
                    supportLine.setStations(stationList);
                    supportLine.save();
                }
                mRlProgressPort.setVisibility(View.GONE);
                Intent intent = new Intent(LineConfigActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /*private void initToolbar() {

        setSupportActionBar(mToolbarLineConfig);


        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText("配置线路");

        mToolbarLineConfig.setNavigationIcon(R.drawable.back_up_logo);

        mToolbarLineConfig.setNavigationOnClickListener(v -> finish());
    }*/

}
