package com.zero.wolf.greenroad.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.ConclusionActivity;
import com.zero.wolf.greenroad.activity.SettingActivity;
import com.zero.wolf.greenroad.bean.CheckedBean;
import com.zero.wolf.greenroad.litepalbean.SupportOperator;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CheckedFragment extends Fragment implements View.OnClickListener {


    Unbinder unbinder;

    private static ToggleButton mToggleIsFree;

    private static ToggleButton mToggleIsRoom;
    private static final String ARG_CONCLUSION = "conclusion";
    private static final String ARG_DESCRIPTION = "description";


    private static TextView mTextConclusionView;
    private String mConclusion_I;
    private String mDescription_I;
    private StringBuilder mBuilder;
    private static EditText mEditDescriptionView;
    private String mConclusionText;
    private String mDescriptionEditText;
    private static CheckedFragment sFragment;

    private static String sDescriptionQ;
    private static String sConclusionQ;
    private static boolean sIsFree;
    private static boolean sIsRoom;
    private static TextView mSiteCheck;
    private static TextView mSiteLogin;
    private static String sSiteLogin_Q;
    private String sSiteCheck_q;
    private static String sSiteCheck_Q;
    private String mConclusions;


    public CheckedFragment() {

    }


    public static CheckedFragment newInstance() {
        if (sFragment == null) {
            sFragment = new CheckedFragment();
        }
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checked, container, false);
        unbinder = ButterKnife.bind(this, view);

        mTextConclusionView = (TextView) view.findViewById(R.id.checked_conclusion_text);
        mEditDescriptionView = (EditText) view.findViewById(R.id.checked_description_text);
        mToggleIsRoom = (ToggleButton) view.findViewById(R.id.toggle_is_room);
        mToggleIsFree = (ToggleButton) view.findViewById(R.id.toggle_is_free);
        mSiteCheck = (TextView) view.findViewById(R.id.site_check_operator);
        mSiteLogin = (TextView) view.findViewById(R.id.site_login_operator);


        mTextConclusionView.setOnClickListener(this);
        mSiteCheck.setOnClickListener(this);
        mSiteLogin.setOnClickListener(this);


        mTextConclusionView.setText(mConclusion_I);
        //初始化各个checkbox的状态


        return view;
    }


    private void setOperatorInfo(String condition, TextView textView) {
        List<SupportOperator> operatorList = DataSupport.where(condition, "1").find(SupportOperator.class);
        if (operatorList.size() != 0) {
            Logger.i(operatorList.toString());
            String job_number = operatorList.get(0).getJob_number();
            String operator_name = operatorList.get(0).getOperator_name();
            textView.setText(job_number + "(" + operator_name + ")");
        } else {
            textView.setText("500001(苏三)");
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public static void setCheckedBeanConnectListener(CheckedBeanConnectListener listener) {
        if (mTextConclusionView != null) {
            sConclusionQ = mTextConclusionView.getText().toString().trim();
        }
        if (mEditDescriptionView != null) {
            sDescriptionQ = mEditDescriptionView.getText().toString().trim();
        }
        //拿到现场检查人,登记人
        if (mToggleIsFree != null) {
            sIsFree = mToggleIsFree.isChecked();
        }
        //拿到容积,是否免费的信息
        if (mToggleIsRoom != null) {
            sIsRoom = mToggleIsRoom.isChecked();
        }
        if (mSiteCheck != null) {
            sSiteCheck_Q = mSiteCheck.getText().toString().trim();
        }

        if (mSiteLogin != null) {
            sSiteLogin_Q = mSiteLogin.getText().toString().trim();
        }
        CheckedBean bean = new CheckedBean();
        bean.setConclusion(sConclusionQ);
        bean.setDescription(sDescriptionQ);
        bean.setSiteCheck(sSiteCheck_Q);
        bean.setSiteLogin(sSiteLogin_Q);
        bean.setIsRoom(sIsRoom ? 1 : 0);
        bean.setIsFree(sIsFree ? 1 : 0);

        listener.beanConnect(bean);

    }

    @Override
    public void onResume() {
        super.onResume();
        ConclusionActivity.setTextChangeListener(conclusions ->{
            mConclusions = conclusions;
        });
        mTextConclusionView.setText(mConclusions);

        setOperatorInfo("check_select = ?", mSiteCheck);
        setOperatorInfo("login_select = ?", mSiteLogin);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checked_conclusion_text:
                ConclusionActivity.actionStart(getContext(),
                        mTextConclusionView.getText().toString().trim());
                break;
            case R.id.site_check_operator:
                openSettingActivity();
                break;
            case R.id.site_login_operator:
                openSettingActivity();
                break;

            default:
                break;
        }
    }

    private void openSettingActivity() {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        startActivity(intent);
    }
    public interface CheckedBeanConnectListener {
        void beanConnect(CheckedBean bean);
    }

}
