package com.zero.wolf.greenroad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.activity.BaseActivity;
import com.zero.wolf.greenroad.activity.MainActivity;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.httpresultbean.HttpResultLoginName;
import com.zero.wolf.greenroad.https.HttpMethods;
import com.zero.wolf.greenroad.https.HttpUtilsApi;
import com.zero.wolf.greenroad.litepalbean.SupportLoginUser;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Date;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Connector.getDatabase();


        mPopup_button = (ImageButton) findViewById(R.id.popup_button);
        mPopup_button.setOnClickListener(this);


        mBt_login = (Button) findViewById(R.id.bt_login);
        mBt_login.setOnClickListener(this);

        userTextChange();
    }

    private void userTextChange() {
        mEt_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEt_password.setText("");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_button:
                initData();

                int width = mEt_user_name.getWidth();

                mPopupWindow = new SpinnerPopupWindow.Builder(LoginActivity.this)
                        .setmLayoutManager(null, 0)
                        .setmAdapter(new SpinnerAdapter(this, mSupportLoginUsers, new onItemClick() {
                            @Override
                            public void itemClick(int position) {
                                updatePopup(position);
                            }
                        }))
                        .setmItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL))
                        .setmHeight(600).setmWidth(width)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();

                int left = mEt_user_name.getLeft();
                int paddingLeft = mEt_user_name.getPaddingLeft();
                Logger.i("登录账号框" + left + "-----" + paddingLeft);


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
        mCheckBox.setChecked(mSupportLoginUsers.get(position).isCheck());
        mPopupWindow.dismissPopWindow();
    }

    private void initData() {
        Connector.getDatabase();
        mSupportLoginUsers = DataSupport.findAll(SupportLoginUser.class);
    }

    private void startMainActivity() {

        String username = mEt_user_name.getText().toString().trim();
        String password = mEt_password.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mEt_password.setError(getString(R.string.error_invalid_password));
            focusView = mEt_password;
            cancel = true;
            return;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mEt_user_name.setError(getString(R.string.error_field_required));
            focusView = mEt_user_name;
            cancel = true;
            return;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

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
                    boolean isCheck = userInfos.get(0).isCheck();

                    getTimeGap(username, password, userInfos, operator, stationName, mIsConnected);
                }
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
    private void getTimeGap(String username, String password, List<SupportLoginUser> userInfos, String operator, String stationName, boolean isConnected) {
        //// TODO: 2017/7/11 ceshi
        Date currentDate = TimeUtil.getCurrentTimeToDate();
        Date logindate = userInfos.get(0).getLogindate();
        int timeGap = TimeUtil.differentDaysByMillisecond(currentDate, logindate);
        if (timeGap > 1) {
            DataSupport.deleteAll(SupportLoginUser.class, "username=? and password = ?",
                    username, password);
            if (mIsConnected) {
                ToastUtils.singleToast("账号已过期，请重新输入密码");
            } else {
                ToastUtils.singleToast("账号已过期，请在有网状态下重新登录");
            }
        } else {
            login2MainActivity(username, operator, stationName);
            if (mIsConnected) {
                ToastUtils.singleToast("登陆成功");
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
                Logger.i("" + code);
                Logger.i("" + code1);
                Logger.i("" + msg);
                Logger.i("" + operator);
                Logger.i("" + stationName);
                if (code == 200) {
                    if (code1 == 200) {
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
                                userInfo.setCheck(true);
                                userInfo.save();
                            } else {
                                getTimeGap(username, password, userInfos, operator, stationName, mIsConnected);
                            }
                        } else {
                            if (userInfos.size() != 0) {
                                userInfos.get(0).delete();
                            }
                        }
                        login2MainActivity(username, operator, stationName);
                    } else if (code1 == 201) {
                        ToastUtils.singleToast(msg);
                        mEt_user_name.setError(msg);

                    } else if (code1 == 202) {


                        ToastUtils.singleToast(msg);
                    } else if (code1 == 203) {


                        ToastUtils.singleToast(msg);
                    } else if (code1 == 204) {

                        ToastUtils.singleToast(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<HttpResultLoginName> call, Throwable t) {
                Logger.i(t.getMessage());
            }
        });
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

    class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.MyViewHolder> {

        private final AppCompatActivity mActivity;
        private final onItemClick mItemClick;
        private final List<SupportLoginUser> mList;

        public SpinnerAdapter(AppCompatActivity activity, List<SupportLoginUser> list, onItemClick itemClick) {
            mItemClick = itemClick;
            mActivity = activity;
            mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    LoginActivity.this).inflate(R.layout.login_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv.setText(mList.get(position).getUsername());
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  int layoutPosition = holder.getLayoutPosition();
                    notifyDataSetChanged();
                    mItemClick.itemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.text_login);
                //     mPopupWindow.dismissPopWindow();

            }
        }

    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public interface onItemClick {
        void itemClick(int position);
    }
}
