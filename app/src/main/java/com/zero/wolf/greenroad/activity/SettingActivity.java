package com.zero.wolf.greenroad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.SettingOperatorAdapter;
import com.zero.wolf.greenroad.bean.SettingOperatorInfo;
import com.zero.wolf.greenroad.interfacy.TextChangeWatcher;
import com.zero.wolf.greenroad.litepalbean.SupportOperator;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    public static final int SETTING_REQUEST = 001;
    @BindView(R.id.toolbar_setting)
    Toolbar mToolbarSetting;
    @BindView(R.id.text_setting_road)
    TextView mTextSettingRoad;
    @BindView(R.id.text_setting_station)
    TextView mTextSettingStation;
    @BindView(R.id.setting_recycler_view)
    RecyclerView mSettingRecyclerView;
    @BindView(R.id.text_setting_lane)
    EditText mTextSettingLane;
    @BindView(R.id.text_setting_add_selection)
    TextView mTextSettingAddSelection;

    private SettingActivity mActivity;
    private SettingOperatorAdapter mAdapter;
    private ArrayList<SettingOperatorInfo> mOperatorList;
    private String mJob_number_check;
    private String mJob_number_login;
    private String mOperator_check_name;
    private String mOperator_login_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mActivity = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        initData();
        initToolbar();
        initRecyclerView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
        initRecyclerView();
    }


    /**
     * 初始化工作人员信息
     */
    private void initData() {
        List<SupportOperator> operators = DataSupport.findAll(SupportOperator.class);
        Logger.i(operators.toString());

        mOperatorList = new ArrayList<>();

        for (int i = 0; i < operators.size(); i++) {
            SettingOperatorInfo info = new SettingOperatorInfo();
            info.setOperator_name(operators.get(i).getOperator_name());
            info.setJob_number(operators.get(i).getJob_number());
            info.setIsCheckSelected(operators.get(i).getCheck_select());
            info.setIsLoginSelected(operators.get(i).getLogin_select());
            mOperatorList.add(info);
            Logger.i(mOperatorList.get(i).toString());
        }
        //从app注册时的配置信息中取出数据填充线路以及收费站
        List<String> strListValue = SPListUtil.getStrListValue(mActivity, SPListUtil.APPCONFIGINFO);
        mTextSettingRoad.setText(strListValue.get(1).toString());
        mTextSettingStation.setText(strListValue.get(2).toString());

        //初始化车道以及保存改变车道的状态
        mTextSettingLane.setText((String) SPUtils.get(mActivity, SPUtils.TEXTLANE, "66"));
        mTextSettingLane.setSelection(mTextSettingLane.getText().length());
        mTextSettingLane.addTextChangedListener(new TextChangeWatcher(editable -> {
            String laneText = mTextSettingLane.getText().toString().trim();
            if (!"".equals(laneText)) {
                SPUtils.putAndApply(mActivity, SPUtils.TEXTLANE, laneText);
            }
        }));
    }


    /**
     * 初始化RecyclerView的布局
     */
    private void initRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mAdapter = new SettingOperatorAdapter(this, mOperatorList, (SettingOperatorInfo info) -> {
            mJob_number_check = info.getJob_number();
            mOperator_check_name = info.getOperator_name();
            Logger.i("check---" + mJob_number_check + "-----" + mOperator_check_name);

        }, (SettingOperatorInfo info) -> {
            mJob_number_login = info.getJob_number();
            mOperator_login_name = info.getOperator_name();
            Logger.i("login---" + mJob_number_login + "-----" + mOperator_login_name);

        });
        mSettingRecyclerView.setLayoutManager(manager);
        mSettingRecyclerView.setAdapter(mAdapter);
    }


    private void initToolbar() {

        setSupportActionBar(mToolbarSetting);


        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText(getString(R.string.setting_default));

        mToolbarSetting.setNavigationIcon(R.drawable.back_up_logo);

        mToolbarSetting.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.text_setting_add_selection,
            R.id.text_setting_lane,
            R.id.text_setting_road})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.text_setting_add_selection:
                addOperator();

                break;

            default:
                break;
        }
    }

    /**
     * 添加操作员
     */
    private void addOperator() {
        Intent intent = new Intent(this, AddOperatorActivity.class);
        startActivity(intent);
    }

    /**
     * 更新operator数据库表
     *
     * @param job_number
     * @param type
     */
    private void updateOperatorLite(String job_number, int type) {


        List<SupportOperator> operatorList = DataSupport.findAll(SupportOperator.class);
        for (SupportOperator operator : operatorList) {
            if (job_number != null && job_number.equals(operator.getJob_number())) {
                if (type == 001) {
                    operator.setCheck_select(1);
                } else {
                    operator.setLogin_select(1);
                }
            } else {
                if (type == 001) {
                    operator.setCheck_select(0);
                } else {
                    operator.setLogin_select(0);
                }
            }
            operator.save();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.i("onDestroy");
        if (mJob_number_check != null) {
            updateOperatorLite(mJob_number_check, 001);
        }
        if (mJob_number_login != null) {
            updateOperatorLite(mJob_number_login, 002);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
