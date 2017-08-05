package com.zero.wolf.greenroad.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.litepalbean.SupportOperator;
import com.zero.wolf.greenroad.tools.ActionBarTool;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddOperatorActivity extends BaseActivity {

    @BindView(R.id.toolbar_setting_add)
    Toolbar mToolbarSettingAdd;
    @BindView(R.id.add_operator_job_number)
    EditText mAddOperatorJobNumber;
    @BindView(R.id.add_operator_name)
    EditText mAddOperatorName;
    @BindView(R.id.add_operator_check_person)
    CheckBox mAddOperatorCheckPerson;
    @BindView(R.id.add_operator_login_person)
    CheckBox mAddOperatorLoginPerson;
    @BindView(R.id.save_operator_info_btn)
    Button mSaveOperatorInfoBtn;
    private AddOperatorActivity mActivity;
    private String mJob_number;
    private String mOperator_name;
    private boolean mIsLogined;
    private boolean mIsChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operator);
        ButterKnife.bind(this);
        mActivity = this;

        initToolbar();
        initView();
    }

    private void initView() {

        mSaveOperatorInfoBtn.setOnClickListener(v -> {
            mJob_number = mAddOperatorJobNumber.getText().toString().trim();
            Logger.i(mJob_number.length() + "");
            mOperator_name = mAddOperatorName.getText().toString().trim();
            mIsChecked = mAddOperatorCheckPerson.isChecked();
            mIsLogined = mAddOperatorLoginPerson.isChecked();

            if ("".equals(mJob_number) || mJob_number.length() < 6) {
                mAddOperatorJobNumber.setError("请输入六位数的工号");
                mAddOperatorJobNumber.requestFocus();
                return;
            }
            List<SupportOperator> jobNumberList = DataSupport.select("job_number").find(SupportOperator.class);
            for (int i = 0; i < jobNumberList.size(); i++) {
                String job_number = jobNumberList.get(i).getJob_number();
                if (mJob_number.equals(job_number)) {
                    mAddOperatorJobNumber.setError("工号已被使用");
                    mAddOperatorJobNumber.requestFocus();
                    return;
                }
            }

            if ("".equals(mOperator_name)) {
                mAddOperatorName.setError("请输入姓名");
                mAddOperatorName.requestFocus();
                return;
            }
            if (mIsChecked) {
                SupportOperator operator = new SupportOperator();
                operator.setToDefault("check_select");
                operator.updateAll();
            }
            if (mIsLogined) {
                SupportOperator operator = new SupportOperator();
                operator.setToDefault("login_select");
                operator.updateAll();
            }

            SupportOperator operator = new SupportOperator();
            operator.setJob_number(mJob_number);
            operator.setOperator_name(mOperator_name);
            if (mIsChecked) {
                operator.setCheck_select(1);
            } else {
                operator.setCheck_select(0);
            }
            if (mIsLogined) {
                operator.setLogin_select(1);
            } else {
                operator.setLogin_select(0);
            }
            operator.save();
            // TODO: 2017/8/4 需要向网络提交本次添加

            finish();
        });
    }

    private void initToolbar() {

        setSupportActionBar(mToolbarSettingAdd);


        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText(getString(R.string.add_operator));
        //title_text_view.setTextSize(18);

        mToolbarSettingAdd.setNavigationIcon(R.drawable.back_up_logo);

        mToolbarSettingAdd.setNavigationOnClickListener(v -> finish());
    }

}
