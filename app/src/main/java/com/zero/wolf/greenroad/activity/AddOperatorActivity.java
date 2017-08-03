package com.zero.wolf.greenroad.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.tools.ActionBarTool;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddOperatorActivity extends BaseActivity {

    @BindView(R.id.toolbar_setting_add)
    Toolbar mToolbarSettingAdd;
    private AddOperatorActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operator);
        ButterKnife.bind(this);
        mActivity = this;

        initToolbar();
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
