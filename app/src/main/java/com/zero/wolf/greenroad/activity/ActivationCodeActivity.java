package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.LoginActivity;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.ActivationRequestBody;
import com.zero.wolf.greenroad.bean.ActivationResult;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.tools.DevicesInfoUtils;
import com.zero.wolf.greenroad.tools.Md5Util;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.ToastUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

/**
 * A login screen that offers login via email/password.
 */
public class ActivationCodeActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    private String agreementUrl;
    @BindView(R.id.activation_firstCode)
    EditText mActivationFirstCode;
    @BindView(R.id.activation_secondCode)
    EditText mActivationSecondCode;
    @BindView(R.id.activation_threeCode)
    EditText mActivationThreeCode;
    @BindView(R.id.activation_fourCode)
    EditText mActivationFourCode;
    @BindView(R.id.activation_fiveCode)
    EditText mActivationFiveCode;
    @BindView(R.id.activation_sign_in_button)
    Button mSignInButton;
    @BindView(R.id.activation_sign_out_button)
    Button mActivityCodeSignOutButton;
    @BindView(R.id.activation_agree_checkbox)
    CheckBox mActivationAgreeCheckbox;
    @BindView(R.id.activation_agreement_text)
    TextView mActivationAgreementText;
    private DevicesInfoUtils mInfoUtils;
    private File mFile;
    private File mTempFile;
    private ActivationRequestBody mBody;
    private NetWorkManager mNetWorkManager;
    private boolean mIsNetConnected;
    private Observable<ActivationResult> mVerificationCode;
    private Subscriber<ActivationResult> mSubscriber;
    private String mANDROID_id;
    private String macID;
    private String mPhoneName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activation_code);
        ButterKnife.bind(this);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        mNetWorkManager = NetWorkManager.getInstane();
        mIsNetConnected = mNetWorkManager.isnetworkConnected(this);


        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        macID = tm.getDeviceId();
        mPhoneName = android.os.Build.MODEL;

        mBody = new ActivationRequestBody();

   //     SPUtils.putAndApply(getApplicationContext(), SPUtils.ISFIRSTACTIVATION, false);

    }

    @OnClick(R.id.activation_sign_in_button)
    public void sign_in(View view) {
        mSignInButton.setEnabled(false);
        final String firstCode = mActivationFirstCode.getText().toString().trim();
        final String secondCode = mActivationSecondCode.getText().toString().trim();
        final String threeCode = mActivationThreeCode.getText().toString().trim();
        final String fourCode = mActivationFourCode.getText().toString().trim();
        final String fiveCode = mActivationFiveCode.getText().toString().trim();

        if (!TextUtils.isEmpty(firstCode) && !TextUtils.isEmpty(secondCode)
                && !TextUtils.isEmpty(threeCode) && !TextUtils.isEmpty(fourCode)
                && !TextUtils.isEmpty(macID)) {
            mBody.setMacID(macID);
            mBody.setMacName(mPhoneName);
            mBody.setRegKey(firstCode + "-" + secondCode + "-" + threeCode + "-"
                    + fourCode + "-"+fiveCode);
            if (mIsNetConnected) {
                mSignInButton.setEnabled(true);
                mVerificationCode = mNetWorkManager.verificationCode(mBody.getMacID(),mBody.getMacName(),mBody.getRegKey());
                mSubscriber = new Subscriber<ActivationResult>() {
                    @Override
                    public void onCompleted() {
                        mSignInButton.setEnabled(false);
                        Logger.i("激活完成");
                        closeActivity(0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                    }

                    @Override
                    public void onNext(ActivationResult requestResult) {
                            Logger.i(requestResult.getCode());

                        if (ActivationResult.SUCCESS_CODE.equals(requestResult.getCode())) {
                            SPUtils.putAndApply(getApplicationContext(), SPUtils.CODE, firstCode  + secondCode
                                    + threeCode  + fourCode+fiveCode);
                            SPUtils.putAndApply(getApplicationContext(), SPUtils.ISACTIVATIONSUCCESS, Md5Util.md5Password(macID));

                        } else if (ActivationResult.FAILD_CODE_USED_KEY.equals(requestResult.getCode())) {

                            ToastUtils.singleToast(ActivationCodeActivity.this.getString(R.string.key_used));
                        } else if (ActivationResult.FAILD_CODE_INVALID_KEY.equals(requestResult.getCode())) {
                            ToastUtils.singleToast(ActivationCodeActivity.this.getString(R.string.Invalid_Key));
                        } else {
                            ToastUtils.singleToast(ActivationCodeActivity.this.getString(R.string.Retry));
                        }

                    }
                };
                mVerificationCode.subscribe(mSubscriber);
            } else {
                ToastUtils.singleToast(ActivationCodeActivity.this.getString(R.string.network_error));
                mSignInButton.setEnabled(false);
                return;
            }
        } else {
            mSignInButton.setEnabled(false);
            ToastUtils.singleToast( ActivationCodeActivity.this.getString(R.string.Wrong_format_key));
        }

    }
    @OnClick(R.id.activation_agree_checkbox)
    public void isChecked() {
        boolean checked = mActivationAgreeCheckbox.isChecked();
        if (checked) {
            mSignInButton.setEnabled(true);
            mSignInButton.setClickable(true);
        } else {
            mSignInButton.setEnabled(false);
            mSignInButton.setClickable(false);
        }
    }

    @OnClick(R.id.activation_sign_out_button)
    public void sign_out() {
        closeActivity(0);
    }

    /**
     * @param type 1表示跳转并关闭 其他表示仅关闭
     */
    private void closeActivity(int type) {
        this.finish();
        if (type == 1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if (mSubscriber != null)
            mSubscriber.unsubscribe();
    }

}

