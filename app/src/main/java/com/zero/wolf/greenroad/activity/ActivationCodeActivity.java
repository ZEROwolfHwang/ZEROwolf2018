package com.zero.wolf.greenroad.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
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
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.ActivationRequestBody;
import com.zero.wolf.greenroad.bean.ActivationResult;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.tools.DevicesInfoUtils;
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

    @BindView(R.id.activation_sign_in_button)
    Button mSignInButton;
    @BindView(R.id.activation_sign_out_button)
    Button mActivityCodeSignOutButton;
    @BindView(R.id.activation_agree_checkbox)
    CheckBox mActivationAgreeCheckbox;
    @BindView(R.id.activation_agreement_text)
    TextView mActivationAgreementText;
    private DevicesInfoUtils mInfoUtils;
    private String mMacAddress;
    private File mFile;
    private File mTempFile;
    private ActivationRequestBody mBody;
    private NetWorkManager mNetWorkManager;
    private boolean mIsNetConnected;
    private Observable<ActivationResult> mVerificationCode;
    private Subscriber<ActivationResult> mSubscriber;
    private String mANDROID_id;


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

        codeFromAndroid();

        mInfoUtils = DevicesInfoUtils.getInstance();
        mMacAddress = mInfoUtils.getMacAddress(this);

        mBody = new ActivationRequestBody();

        SPUtils.putAndApply(getApplicationContext(), SPUtils.ISFIRSTACTIVATION, false);

    }

    @OnClick(R.id.activation_sign_in_button)
    public void sign_in(View view) {
        mSignInButton.setEnabled(false);
        final String firstCode = mActivationFirstCode.getText().toString().trim();
        final String secondCode = mActivationSecondCode.getText().toString().trim();
        final String threeCode = mActivationThreeCode.getText().toString().trim();
        final String fourCode = mActivationFourCode.getText().toString().trim();

        if (!TextUtils.isEmpty(firstCode) && !TextUtils.isEmpty(secondCode)
                && !TextUtils.isEmpty(threeCode) && !TextUtils.isEmpty(fourCode)
                && !TextUtils.isEmpty(mMacAddress)) {
            mBody.setMacCode(mMacAddress);
            mBody.setMCode(mMacAddress);
            mBody.setRegKey(firstCode + "-" + secondCode + "-" + threeCode + "-"
                    + fourCode );
            if (mIsNetConnected) {
                mVerificationCode = mNetWorkManager.verificationCode(mBody);
                mSubscriber = new Subscriber<ActivationResult>() {
                    @Override
                    public void onCompleted() {
                        mSignInButton.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ActivationResult requestResult) {
                        if (ActivationResult.SUCCESS_CODE.equals(requestResult.getCode())) {
                            SPUtils.putAndApply(getApplicationContext(), SPUtils.SHAREDPRENAME, requestResult.getDetail().replaceAll(" ", ""));
                            SPUtils.putAndApply(getApplicationContext(), SPUtils.CODE, firstCode + "-" + secondCode + "-"
                                    + threeCode + "-" + fourCode);
                        } else if (ActivationResult.FAILD_CODE.equals(requestResult.getCode())) {
                            if (ActivationResult.FAILD_CODE_USED_KEY.equals(requestResult.getDetail())) {
                            } else {
                                ToastUtils.singleToast(ActivationCodeActivity.this.getString(R.string.key_used));
                            }
                        } else {
                            ToastUtils.singleToast(ActivationCodeActivity.this.getString(R.string.Invalid_Key));

                        }
                    }
                };
                mVerificationCode.subscribe(mSubscriber);
            } else {
                mSignInButton.setEnabled(true);
                return;
            }
        } else {
            mSignInButton.setEnabled(true);
        }
    }

    private void codeFromAndroid() {
        mANDROID_id = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);

        char c = mANDROID_id.charAt(0);
        Logger.i(String.valueOf(c));
        Logger.i(mANDROID_id);

        String OneText = "";
        String TwoText = "";
        String ThreeText = "";
        String FourText = "";


     for (int i =0;i<4;i++) {
         OneText =OneText+ String.valueOf(mANDROID_id.charAt(i));
     }
     for (int i =4;i<8;i++) {
         TwoText =TwoText+ String.valueOf(mANDROID_id.charAt(i));
     }
     for (int i =8;i<12;i++) {
         ThreeText = ThreeText+String.valueOf(mANDROID_id.charAt(i));
     }
     for (int i =12;i<16;i++) {
         FourText = FourText+String.valueOf(mANDROID_id.charAt(i));
     }

        mActivationFirstCode.setText(OneText);
        mActivationSecondCode.setText(TwoText);
        mActivationThreeCode.setText(ThreeText);
        mActivationFourCode.setText(FourText);
        boolean isfirstactivation = (boolean) SPUtils.get(getApplicationContext(), SPUtils.ISFIRSTACTIVATION, false);

        if (!isfirstactivation) {
            mSignInButton.setClickable(true);
            mSignInButton.setEnabled(true);

            mSignInButton.setOnClickListener((View view)->{
                // TODO: 2017/7/3  在这里实现客户端设备ID与服务端的认证
            });
        }
    }


}

