package com.zero.wolf.greenroad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.activity.BaseActivity;
import com.zero.wolf.greenroad.activity.MainActivity;
import com.zero.wolf.greenroad.bean.LoginName;
import com.zero.wolf.greenroad.interfacy.HttpUtilsApi;
import com.zero.wolf.greenroad.litepalbean.LoginUserInfo;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private ImageButton mPopup_button;
    private ArrayList<String> mList;
    private Button mBt_login;
    @BindView(R.id.text_user_name)
    EditText mEt_user_name;
    @BindView(R.id.text_password)
    EditText mEt_password;
    private SpinnerPopupWindow mPopupWindow;
    private boolean mIsConnected;
    private List<LoginUserInfo> mLoginUserInfos;


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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_button:
                initData();
                mPopupWindow = new SpinnerPopupWindow.Builder(LoginActivity.this)
                        .setmLayoutManager(null, 0)
                        .setmAdapter(new SpinnerAdapter(this, mLoginUserInfos, new onItemClick() {
                            @Override
                            public void itemClick(int position) {
                                updatePopup(position);
                            }
                        }))
                        .setmHeight(500).setmWidth(500)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();

                mPopupWindow.showPopWindowCenter(v);
                break;
            case R.id.bt_login:
                startMainActivity();
                break;
            default:
                break;

        }
    }

    private void updatePopup(int position) {
        mEt_user_name.setText(mLoginUserInfos.get(position).getUsername());
        mEt_password.setText(mLoginUserInfos.get(position).getPassword());
        mPopupWindow.dismissPopWindow();

    }

    private void initData() {
        Connector.getDatabase();
        mLoginUserInfos = DataSupport.findAll(LoginUserInfo.class);
    }

    private void startMainActivity() {

        String username = mEt_user_name.getText().toString();
        String password = mEt_password.getText().toString();

        mIsConnected = NetWorkManager.isnetworkConnected(this);
        if (mIsConnected) {
            loginFromNet(username, password);
        } else {
            List<LoginUserInfo> userInfos = DataSupport
                    .where("username=? and password = ?", username, password)
                    .find(LoginUserInfo.class);
            if (userInfos.size() == 0) {
                ToastUtils.singleToast("无网络连接状态下本地库未存储该账号");
            } else if (userInfos.size() == 1) {
                String operator = userInfos.get(0).getOperator();
                login2MainActivity(operator, username);
                ToastUtils.singleToast("无网络连接状态登陆成功");
            }
        }
    }

    /**
     * 有网络的状态下登录
     * @param username
     * @param password
     */
    private void loginFromNet(String username, String password) {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder.baseUrl("http://192.168.2.122/lvsetondao/index.php/Home/Login/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        HttpUtilsApi httpUtilsApi = retrofit.create(HttpUtilsApi.class);
        Call<LoginName> login = httpUtilsApi.login(username, password);
        login.enqueue(new Callback<LoginName>() {
            @Override
            public void onResponse(Call<LoginName> call, Response<LoginName> response) {
                int code = response.code();
                int code1 = response.body().getCode();
                String msg = response.body().getMsg();
                String name = response.body().getData();
                Logger.i("" + code);
                Logger.i("" + code1);
                Logger.i("" + msg);
                Logger.i("" + name);
                if (code == 200) {
                    if (code1 == 200) {

                        List<LoginUserInfo> userInfos = DataSupport
                                .where("username=? and password = ? and operator = ?", username, password,name)
                                .find(LoginUserInfo.class);
                        if (userInfos.size() == 0) {
                            LoginUserInfo userInfo = new LoginUserInfo();
                            userInfo.setUsername(username);
                            userInfo.setPassword(password);
                            userInfo.setOperator(name);
                            userInfo.save();
                        }

                        ToastUtils.singleToast(msg);
                        login2MainActivity(name,username);
                    } else if (code1 == 201) {
                        ToastUtils.singleToast(msg);

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
            public void onFailure(Call<LoginName> call, Throwable t) {
                Logger.i(t.getMessage());
            }
        });
    }

    /**
     * 登录成功进入mainActivity
     */
    private void login2MainActivity(String name, String username) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("operator", name);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.MyViewHolder> {

        private final AppCompatActivity mActivity;


        private final onItemClick mItemClick;
        private final List<LoginUserInfo> mList;

        public SpinnerAdapter(AppCompatActivity activity, List<LoginUserInfo> list, onItemClick itemClick) {
            mItemClick = itemClick;
            mActivity = activity;
            mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    LoginActivity.this).inflate(R.layout.item_test, parent,
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
                tv = (TextView) view.findViewById(R.id.test1);
                //     mPopupWindow.dismissPopWindow();

            }
        }

    }

    public interface onItemClick {
        void itemClick(int position);
    }
}
