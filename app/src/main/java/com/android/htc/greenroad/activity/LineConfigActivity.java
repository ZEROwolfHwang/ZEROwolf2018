package com.android.htc.greenroad.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.android.htc.greenroad.R;
import com.android.htc.greenroad.tools.ActionBarTool;
import com.android.htc.greenroad.tools.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineConfigActivity extends BaseActivity {

    @BindView(R.id.toolbar_line_config)
    Toolbar mToolbarLineConfig;
    @BindView(R.id.text_line_config)
    EditText mTextLineConfig;
    private LineConfigActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_config);
        mActivity = this;
        ButterKnife.bind(this);

        initToolbar();
        initView();
    }

    private void initView() {
        String newUrl = "";
        String url = mTextLineConfig.getText().toString().trim();
        if (url.contains("http://")) {
            newUrl = url;
        } else {
            newUrl = "http://" + url;
        }

        SPUtils.putAndApply(this,SPUtils.LINE_CONFIG,newUrl);

    }

    private void initToolbar() {

        setSupportActionBar(mToolbarLineConfig);


        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText("配置线路");

        mToolbarLineConfig.setNavigationIcon(R.drawable.back_up_logo);

        mToolbarLineConfig.setNavigationOnClickListener(v -> finish());
    }

}
