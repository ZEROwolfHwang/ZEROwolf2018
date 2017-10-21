package com.android.htc.greenroad.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.htc.greenroad.R;
import com.android.htc.greenroad.httpresultbean.HttpResult;
import com.android.htc.greenroad.https.RequestLogin;
import com.android.htc.greenroad.manager.GlobalManager;
import com.android.htc.greenroad.polling.PollingService;
import com.android.htc.greenroad.polling.PollingUtils;
import com.android.htc.greenroad.presenter.NetWorkManager;
import com.android.htc.greenroad.tools.DevicesInfoUtils;
import com.android.htc.greenroad.tools.SPListUtil;
import com.android.htc.greenroad.tools.SPUtils;
import com.android.htc.greenroad.tools.TimeUtil;
import com.android.htc.greenroad.tools.ToastUtils;
import com.orhanobut.logger.Logger;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.login_version)
    TextView mLoginVersion;
    @BindView(R.id.login_register)
    TextView mLoginRegister;
    @BindView(R.id.rl_progress_login)
    RelativeLayout mRlProgressLogin;
    private Button mBt_login;
    @BindView(R.id.text_user_name)
    EditText mEt_user_name;
    @BindView(R.id.text_password)
    EditText mEt_password;
    @BindView(R.id.check_box_pwd)
    CheckBox mCheckBox;
    private boolean mIsConnected;
    private static int TIMEGAP = 3;
    private String macID;
    private LoginActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;
        ButterKnife.bind(mActivity);
        initData();
        initView();

    }

    private void initData() {
        Connector.getDatabase();

        // TODO: 2017/8/5 客户端的认证信息，移至注册账号是的返回储存

        macID = Settings.Secure
                .getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        mLoginVersion.setText("e绿通 V" + DevicesInfoUtils.getInstance().getVersion(mActivity));


    }

    private void initView() {

        mBt_login = (Button) findViewById(R.id.bt_login);

        mBt_login.setOnClickListener(mActivity);
        mLoginRegister.setOnClickListener(mActivity);


    }

    @Override
    protected void onResume() {
        super.onResume();
        String user = (String) SPUtils.get(this, SPUtils.lOGIN_USERNAME, "");
        mEt_user_name.setText(user);
        if (user != null && !"".equals(user)) {
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                startMainActivity(v);
                break;
            case R.id.login_register:
                LineConfigActivity.actionStart(LoginActivity.this, GlobalManager.LOGIN2PORT);
                break;

            default:
                break;

        }
    }

    private void startMainActivity(View view) {

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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

        // InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mRlProgressLogin.setVisibility(View.VISIBLE);

        mIsConnected = NetWorkManager.isnetworkConnected(this);
        if (mIsConnected) {
            loginFromNet(username, password);
        } else {
            List<String> loginList = SPListUtil.getStrListValue(mActivity, SPListUtil.LOGINNFO);
            if (loginList == null || loginList.size() == 0) {
                ToastUtils.singleToast("本地无账号缓存，请连接网络登录");
            } else if (loginList.size() == 3) {
                getTimeGap(loginList, username, password);
            }
            mRlProgressLogin.setVisibility(View.GONE);
        }

    }

    /**
     * 根据有无网络连接判断时间差内登陆的情形
     *
     * @param username
     * @param password
     */
    private void getTimeGap(List<String> loginList, String username, String password) {
        String userName = loginList.get(0);
        String psw = loginList.get(1);
        String save_time = loginList.get(2);

        String currentTimeToDate = TimeUtil.getCurrentTimeToDate();
        int timeGap = TimeUtil.differentDaysByMillisecond(save_time, currentTimeToDate);

        Logger.i("timeGap" + timeGap);
        if (timeGap > TIMEGAP) {
            SPListUtil.remove(mActivity, SPListUtil.LOGINNFO);
            ToastUtils.singleToast("账号已过期，请在有网状态下重新登录");
            mEt_user_name.setText("");
            mEt_password.setText("");
            mCheckBox.setChecked(false);
            return;
        } else {
            if (username.equals(userName) && password.equals(psw)) {
                login2MainActivity();
            }
            ToastUtils.singleToast("无网络连接状态登陆成功");
        }
    }

    /**
     * 有网络的状态下登录
     *
     * @param username
     * @param password
     */
    private void loginFromNet(String username, String password) {

        Subscriber<HttpResult> subscriber = new Subscriber<HttpResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.i(e.getMessage());
                mRlProgressLogin.setVisibility(View.GONE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                dialog.setTitle("登录失败,网络存在异常");
                dialog.setIcon(getResources().getDrawable(R.drawable.alert_faild_icon));
                dialog.setMessage("点击确定重新配置网络端口\n点击取消请尝试再次登录");
                dialog.setNegativeButton("取消",(dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                dialog.setPositiveButton("确定",(dialogInterface, i) -> {
                    LineConfigActivity.actionStart(LoginActivity.this, GlobalManager.OTHER2PORT);
                    dialogInterface.dismiss();
                });
                dialog.show();
                ToastUtils.singleToast("网络异常,请重新登录");

            }

            @Override
            public void onNext(HttpResult httpResult) {
                int code = httpResult.getCode();
                String msg = httpResult.getMsg();
                Logger.i(code + msg);
                if (code == 200) {
                    if (mCheckBox.isChecked()) {
                       /* List<String> loginList = SPListUtil.getStrListValue(mActivity, SPListUtil.LOGINNFO);
                        if (loginList != null && loginList.size() == 3) {*/
                        ArrayList<String> list = new ArrayList<>();
                        list.add(username);
                        list.add(password);
                        list.add(TimeUtil.getCurrentTimeToDate());
                        SPListUtil.putStrListValue(LoginActivity.this, SPListUtil.LOGINNFO, list);

                        SPUtils.putAndApply(LoginActivity.this, SPUtils.lOGIN_USERNAME, username);
                        // startPollingService(username);
                    } else {
                        SPListUtil.remove(LoginActivity.this, SPListUtil.LOGINNFO);
                        SPUtils.remove(LoginActivity.this, SPUtils.lOGIN_USERNAME);
                    }
                    login2MainActivity();
                } else if (code == 201) {
                    mEt_user_name.setError("账号不存在");
                    mEt_user_name.requestFocus();
                    mRlProgressLogin.setVisibility(View.GONE);
                } else if (code == 202) {
                    mEt_password.setError("密码错误");
                    mEt_password.requestFocus();
                    mRlProgressLogin.setVisibility(View.GONE);
                } else if (code == 203) {
                    mEt_user_name.setError("此账号已登录,请检查账号安全");
                    mEt_user_name.requestFocus();
                    mRlProgressLogin.setVisibility(View.GONE);
                } else if (code == 204) {
                    mEt_user_name.setError("此账号被禁用");
                    mEt_user_name.requestFocus();
                    mRlProgressLogin.setVisibility(View.GONE);
                } else if (code == 205) {
                    mEt_user_name.setError("该终端尚未被注册");
                    mEt_user_name.requestFocus();
                    mRlProgressLogin.setVisibility(View.GONE);
                } else {
                    mRlProgressLogin.setVisibility(View.GONE);
                    ToastUtils.singleToast("网络异常,请重新登录");
                }
                //  mRlProgressLogin.setVisibility(View.GONE);
            }
        };
        RequestLogin.getInstance().
                postLogin(subscriber, username, password, macID);

    }

   /* */

    /**
     * 登陆成功开启服务
     *
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
    private void login2MainActivity() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        //  mRlProgressLogin.setVisibility(View.GONE);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
