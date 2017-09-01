package com.zero.wolf.greenroad.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.SpinnerPopupWindow;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.adapter.SpinnerAdapter;
import com.zero.wolf.greenroad.httpresultbean.HttpResult;
import com.zero.wolf.greenroad.https.RequestRegistered;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.register_road)
    ImageButton mRegisterRoad;
    @BindView(R.id.register_station)
    ImageButton mRegisterStation;
    @BindView(R.id.register_code)
    EditText mRegisterCode;
    @BindView(R.id.register_user)
    EditText mRegisterUser;
    @BindView(R.id.register_psw)
    EditText mRegisterPsw;
    @BindView(R.id.register_sure_psw)
    EditText mRegisterSurePsw;
    @BindView(R.id.btn_sure_register)
    Button mBtnSureRegister;
    @BindView(R.id.text_road_register)
    TextView mTextRoadRegister;
    @BindView(R.id.text_station_register)
    TextView mTextStationRegister;

    private SpinnerAdapter mAdapter_road;
    private SpinnerAdapter mAdapter_station;
    private SpinnerPopupWindow mPopupWindow_road;
    private SpinnerPopupWindow mPopupWindow_station;
    private String[] mRoadList;
    private String[] mStationList;
    private float mDimension;
    private int mWidth;
    private String macID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        macID = Settings.Secure
                .getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Logger.i(macID);

        initData();
    }

    @OnClick({R.id.register_road, R.id.register_station, R.id.btn_sure_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_road:
                mWidth = mTextRoadRegister.getWidth();
                mAdapter_road = new SpinnerAdapter(this, mRoadList, position -> {
                    mTextRoadRegister.setText(mRoadList[position]);
                    mPopupWindow_road.dismissPopWindow();
                });

                mPopupWindow_road = new SpinnerPopupWindow.Builder(RegisterActivity.this)
                        .setmLayoutManager(null, 0)
                        .setmAdapter(mAdapter_road)
                        .setmItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 5, Color.CYAN))
                        .setmHeight(600).setmWidth(mWidth)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();

                mPopupWindow_road.showPopWindow(view, (int) mDimension);

                break;
            case R.id.register_station:
                int width = mTextStationRegister.getWidth();

                mAdapter_station = new SpinnerAdapter(this, mStationList, position -> {
                    mTextStationRegister.setText(mStationList[position]);
                    mPopupWindow_station.dismissPopWindow();
                });

                mPopupWindow_station = new SpinnerPopupWindow.Builder(RegisterActivity.this)
                        .setmLayoutManager(null, 0)
                        .setmAdapter(mAdapter_station)
                        .setmItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 5, Color.CYAN))
                        .setmHeight(800).setmWidth(width)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();


                mPopupWindow_station.showPopWindow(view, (int) mDimension);
                break;
            case R.id.btn_sure_register:
                String roadText = mTextRoadRegister.getText().toString().trim();
                String stationText = mTextStationRegister.getText().toString().trim();

                String register_code = mRegisterCode.getText().toString().trim();
                String register_user = mRegisterUser.getText().toString().trim();
                String register_psw = mRegisterPsw.getText().toString().trim();
                String register_sure_psw = mRegisterSurePsw.getText().toString().trim();

                if (TextUtils.isEmpty(roadText)) {
                    ToastUtils.singleToast("请确定注册路段");
                    return;
                }
                if (TextUtils.isEmpty(stationText)) {
                    ToastUtils.singleToast("请确定注册收费站");
                    return;
                }


                if (TextUtils.isEmpty(register_code)) {
                    mRegisterCode.setError(getString(R.string.error_field_required));
                    mRegisterCode.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(register_user)) {
                    mRegisterUser.setError("请输入用户名");
                    mRegisterUser.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(register_psw) || !isPasswordValid(register_psw)) {
                    mRegisterPsw.setError(getString(R.string.error_invalid_password));
                    mRegisterPsw.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(register_sure_psw)) {
                    mRegisterSurePsw.setError("请再定确定密码");
                    mRegisterSurePsw.requestFocus();
                    return;
                }
                if (!register_psw.equals(register_sure_psw)) {
                    mRegisterSurePsw.setError("两次密码不相同,请重新确定密码");
                    mRegisterSurePsw.requestFocus();
                }


                Subscriber<HttpResult> subscriber = new Subscriber<HttpResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        int code = httpResult.getCode();
                        Logger.i(code + httpResult.getMsg());
                        if (code == 200) {
                            ToastUtils.singleToast("注册成功,请登录");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                            List app_config_info = new ArrayList<String>();

                            app_config_info.add(roadText);
                            app_config_info.add(stationText);
                            app_config_info.add(register_user);

                            Logger.i(app_config_info.toString());
                            SPListUtil.putStrListValue(RegisterActivity.this, SPListUtil.APPCONFIGINFO, app_config_info);
                        } else {
                            ToastUtils.singleToast(httpResult.getMsg());

                        }
                    }
                };
                RequestRegistered.getInstance().postRegistered(subscriber, roadText, stationText,
                        register_code, register_user, register_psw, macID);

                break;

            default:
                break;
        }
    }

    private void saveToSpUtil() {
        List app_config_info = new ArrayList<String>();

        app_config_info.add("163127841234");
        app_config_info.add("西部沿海");
        app_config_info.add("广海");

        Logger.i(app_config_info.toString());
        SPListUtil.putStrListValue(this, SPListUtil.APPCONFIGINFO, app_config_info);


    }

    private void initData() {
        mRoadList = getResources().getStringArray(R.array.road_name);
        mStationList = getResources().getStringArray(R.array.station_name);
        mDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120,
                getResources().getDisplayMetrics());


    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
