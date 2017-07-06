package com.zero.wolf.greenroad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.DevicesInfoUtils;
import com.zero.wolf.greenroad.tools.SPUtils;

/**
 * Created by Administrator on 2017/7/3.
 */

public class AboutActivity extends BaseActivity {

/*    @BindView(R.id.activity_about_user_agreement)
    TextView mActivityAboutUserAgreement;*/
    private TextView mActivityAboutActivationCode;
    private TextView mActivityAboutVersion;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT*1/3;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        getWindow().setAttributes(params);

        setContentView(R.layout.activity_about);

        initView();

    }

    private void initView() {
        mActivityAboutActivationCode = (TextView) findViewById(R.id.activity_about_activation_code);
        mActivityAboutVersion = (TextView) findViewById(R.id.activity_about_version);
        mLinearLayout = (LinearLayout) findViewById(R.id.layout_activation_code);
        mActivityAboutVersion.setText(DevicesInfoUtils.getInstance().getVersion(this));
        mActivityAboutActivationCode.setText((String) SPUtils.get(getApplicationContext(), SPUtils.CODE, ""));

        mLinearLayout.setOnClickListener((view)->{
                Intent intent = new Intent(AboutActivity.this, ActivationCodeActivity.class);
                startActivityForResult(intent, GlobalManager.REQUEST_ACTIVATION);

        });

    }

 /*   private void initData() {
//        SpannableString sp = new SpannableString(getString(R.string.activation_user_agreement));
//        sp.setSpan(new URLSpan(agreementUrl), 0, getString(R.string.activation_user_agreement).length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mActivityAboutUserAgreement.setText(sp);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GlobalManager.REQUEST_ACTIVATION) {
                mActivityAboutActivationCode.setText((String) SPUtils.get(getApplicationContext(), SPUtils.CODE, ""));
            }
        }
    }
}
