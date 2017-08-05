package com.zero.wolf.greenroad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.activity.BaseActivity;
import com.zero.wolf.greenroad.activity.MainActivity;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.adapter.SpinnerAdapter;
import com.zero.wolf.greenroad.httpresultbean.HttpResultLoginName;
import com.zero.wolf.greenroad.https.HttpMethods;
import com.zero.wolf.greenroad.https.HttpUtilsApi;
import com.zero.wolf.greenroad.interfacy.TextChangeWatcher;
import com.zero.wolf.greenroad.litepalbean.SupportLoginUser;
import com.zero.wolf.greenroad.polling.PollingService;
import com.zero.wolf.greenroad.polling.PollingUtils;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private ImageButton mPopup_button;
    private ArrayList<String> mList;
    private Button mBt_login;
    @BindView(R.id.text_user_name)
    EditText mEt_user_name;
    @BindView(R.id.text_password)
    EditText mEt_password;
    @BindView(R.id.check_box_pwd)
    CheckBox mCheckBox;
    private SpinnerPopupWindow mPopupWindow;
    private boolean mIsConnected;
    private List<SupportLoginUser> mSupportLoginUsers;
    private static int TIMEGAP = 10;
    private SpinnerAdapter mSpinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // TODO: 2017/8/5 客户端的认证信息，移至注册账号是的返回储存

        List app_config_info = new ArrayList<String>();

        app_config_info.add("163127841234");
        app_config_info.add("西部沿海");
        app_config_info.add("广海");

        Logger.i(app_config_info.toString());
        SPListUtil.putStrListValue(this, SPListUtil.APPCONFIGINFO, app_config_info);



        Connector.getDatabase();


        mPopup_button = (ImageButton) findViewById(R.id.popup_button);
        mPopup_button.setOnClickListener(this);


        mBt_login = (Button) findViewById(R.id.bt_login);
        mBt_login.setOnClickListener(this);

        mEt_user_name.addTextChangedListener(
                new TextChangeWatcher(editable -> {
                    mEt_password.setText("");
                }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_button:
                initData();

                int width = mEt_user_name.getWidth();

                mSpinnerAdapter = new SpinnerAdapter(this, mSupportLoginUsers, position -> {
                    DataSupport.deleteAll(SupportLoginUser.class, "username=?",
                            mSupportLoginUsers.get(position).getUsername());
                    mSupportLoginUsers = DataSupport.findAll(SupportLoginUser.class);
                    mSpinnerAdapter.updateListView(mSupportLoginUsers);
                    mPopupWindow.dismissPopWindow();
                }
                        , position -> updatePopup(position));

                mPopupWindow = new SpinnerPopupWindow.Builder(LoginActivity.this)
                        .setmLayoutManager(null, 0)
                        .setmAdapter(mSpinnerAdapter)
                        .setmItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL))
                        .setmHeight(600).setmWidth(width)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();

                float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
                        getResources().getDisplayMetrics());

                mPopupWindow.showPopWindow(v, (int) dimension);
                break;
            case R.id.bt_login:
                startMainActivity();
                break;
            default:
                break;

        }
    }

    private void updatePopup(int position) {
        mEt_user_name.setText(mSupportLoginUsers.get(position).getUsername());
        mEt_password.setText(mSupportLoginUsers.get(position).getPassword());
        mCheckBox.setChecked(true);
        mPopupWindow.dismissPopWindow();
    }

    private void initData() {
        Connector.getDatabase();
        mSupportLoginUsers = DataSupport.findAll(SupportLoginUser.class);
    }

    private void startMainActivity() {

        String username = mEt_user_name.getText().toString().trim();
        String password = mEt_password.getText().toString().trim();

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mEt_user_name.setError(getString(R.string.error_field_required));
            mEt_user_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mEt_password.setError(getString(R.string.error_invalid_password));
            mEt_password.requestFocus();
            return;
        }


        mIsConnected = NetWorkManager.isnetworkConnected(this);
        if (mIsConnected) {
            loginFromNet(username, password);
        } else {
            List<SupportLoginUser> userInfos = DataSupport
                    .where("username=? and password = ?", username, password)
                    .find(SupportLoginUser.class);
            if (userInfos.size() == 0) {
                ToastUtils.singleToast("本地无账号缓存，请连接网络登录");
            } else if (userInfos.size() == 1) {
                String operator = userInfos.get(0).getOperator();
                String stationName = userInfos.get(0).getStationName();

                getTimeGap(username, password, userInfos, operator, stationName, mIsConnected);
            }
        }

    }


    /**
     * 根据有无网络连接判断时间差内登陆的情形
     *
     * @param username
     * @param password
     * @param userInfos
     * @param operator
     * @param isConnected
     */
    private void getTimeGap(String username, String password, List<SupportLoginUser> userInfos,
                            String operator, String stationName, boolean isConnected) {
        //// TODO: 2017/7/11 ceshi
        String currentTimeToDate = TimeUtil.getCurrentTimeToDate();

        String logindate = userInfos.get(0).getLogindate();
        int timeGap = TimeUtil.differentDaysByMillisecond(logindate, currentTimeToDate);

        Logger.i("timeGap" + timeGap);
        if (timeGap > TIMEGAP) {
            DataSupport.deleteAll(SupportLoginUser.class, "username=? and password = ?",
                    username, password);
            if (mIsConnected) {
                ToastUtils.singleToast("账号已过期，请重新输入密码");
                mEt_user_name.setText("");
                mEt_password.setText("");
                mCheckBox.setChecked(false);
                return;
            } else {
                ToastUtils.singleToast("账号已过期，请在有网状态下重新登录");
                mEt_user_name.setText("");
                mEt_password.setText("");
                mCheckBox.setChecked(false);
                return;
            }
        } else {
            startPollingService(username);
            login2MainActivity(username, operator, stationName);
            if (mIsConnected) {
                //ToastUtils.singleToast("登陆成功");
                //// TODO: 2017/7/28  snakebar
                Snackbar.make(mBt_login, "登陆成功", Snackbar.LENGTH_SHORT).show();
            } else {
                ToastUtils.singleToast("无网络连接状态登陆成功");
            }
        }
    }

    /**
     * 有网络的状态下登录
     *
     * @param username
     * @param password
     */
    private void loginFromNet(String username, String password) {
        HttpUtilsApi httpUtilsApi = HttpMethods.getInstance().getApi();
        Call<HttpResultLoginName> login = httpUtilsApi.login(username, password);
        login.enqueue(new Callback<HttpResultLoginName>() {
            @Override
            public void onResponse(Call<HttpResultLoginName> call, Response<HttpResultLoginName> response) {
                int code = response.code();
                int code1 = response.body().getCode();
                String msg = response.body().getMsg();
                String operator = response.body().getOperator();
                String stationName = response.body().getStationName();

                Logger.i(msg + "----" + operator + "---" + stationName);

                if (code == 200) {
                    if (code1 == 200) {
                        if (operator == null || stationName == null) {
                            ToastUtils.singleToast("服务器返回错误信息");
                            return;
                        }
                        List<SupportLoginUser> userInfos = DataSupport
                                .where("username=? and password = ?", username, password)
                                .find(SupportLoginUser.class);
                        if (mCheckBox.isChecked()) {
                            if (userInfos.size() == 0) {
                                SupportLoginUser userInfo = new SupportLoginUser();
                                userInfo.setLogindate(TimeUtil.getCurrentTimeToDate());
                                userInfo.setUsername(username);
                                userInfo.setPassword(password);
                                userInfo.setOperator(operator);
                                userInfo.setStationName(stationName);
                                userInfo.save();
                                Logger.i("登陆成功");
                                // TODO: 2017/8/1
                                startPollingService(username);
                                login2MainActivity(username, operator, stationName);
                            } else {
                                Logger.i("走了gettimegap方法");
                                getTimeGap(username, password, userInfos, operator, stationName, mIsConnected);
                            }
                        } else {
                            if (userInfos.size() != 0) {
                                userInfos.get(0).delete();
                                login2MainActivity(username, operator, stationName);
                            }
                        }
                    } else if (code1 == 201) {
                        mEt_user_name.setError("账号不存在");
                        mEt_user_name.requestFocus();
                    } else if (code1 == 202) {
                        mEt_password.setError("密码错误");
                        mEt_password.requestFocus();
                    } else if (code1 == 203) {
                        mEt_user_name.setError("此账号被禁用");
                        mEt_user_name.requestFocus();
                    } else if (code1 == 204) {
                        // TODO: 2017/7/27
                        mEt_user_name.setError("此账号已登录,请检查账号安全");
                        mEt_user_name.requestFocus();
                    }
                }
            }

            @Override
            public void onFailure(Call<HttpResultLoginName> call, Throwable t) {
                Logger.i(t.getMessage());
                ToastUtils.singleToast("网络异常,请重新登录");
            }
        });
    }

   /* *//**
     * 登陆成功开启服务
     * @param username
     */
    private void startPollingService(String username) {
        Logger.i("loginActivity界面登陆成功启动服务");
        //LoopService.startLoopService(this,username);
        PollingUtils.startPollingService(this, 5, PollingService.class, PollingService.ACTION);

    }
    /**
     * 登录成功进入mainActivity
     */
    private void login2MainActivity(String username, String operator, String stationName) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("operator", operator);
        intent.putExtra("stationName", stationName);

        startActivity(intent);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
