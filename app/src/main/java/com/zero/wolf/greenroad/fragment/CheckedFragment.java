package com.zero.wolf.greenroad.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.CheckedBean;
import com.zero.wolf.greenroad.litepalbean.SupportOperator;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CheckedFragment extends Fragment implements View.OnClickListener {


    Unbinder unbinder;
    @BindView(R.id.check_111_001)
    CheckBox mCheck111001;
    @BindView(R.id.check_222_001)
    CheckBox mCheck222001;
    @BindView(R.id.check_222_002)
    CheckBox mCheck222002;
    @BindView(R.id.check_222_003)
    CheckBox mCheck222003;
    @BindView(R.id.check_222_004)
    CheckBox mCheck222004;
    @BindView(R.id.check_333_001)
    CheckBox mCheck333001;
    @BindView(R.id.check_333_002)
    CheckBox mCheck333002;
    @BindView(R.id.check_333_003)
    CheckBox mCheck333003;
    @BindView(R.id.check_333_004)
    CheckBox mCheck333004;
    @BindView(R.id.check_444_001)
    CheckBox mCheck444001;
    @BindView(R.id.check_444_002)
    CheckBox mCheck444002;
    @BindView(R.id.check_555_001)
    CheckBox mCheck555001;
    @BindView(R.id.check_555_002)
    CheckBox mCheck555002;
    @BindView(R.id.check_666_001)
    CheckBox mCheck666001;
    @BindView(R.id.check_666_002)
    CheckBox mCheck666002;
    @BindView(R.id.check_666_003)
    CheckBox mCheck666003;
    @BindView(R.id.check_777_001)
    CheckBox mCheck777001;

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
    private TextView mCheckOperator;
    private TextView mLogonOperator;
    private static CheckedFragment sFragment;
    private RelativeLayout mRelativeLayout;
    private Button mBtnSureConclusion;
    private CheckBox[] mCheckBoxes;
    private static String sDescriptionQ;
    private static String sConclusionQ;
    private static boolean sIsFree;
    private static boolean sIsRoom;
    private static TextView mSiteCheck;
    private static TextView mSiteLogin;
    private static String sSiteLogin_Q;
    private String sSiteCheck_q;
    private static String sSiteCheck_Q;

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
        if (mBuilder == null) {
            mBuilder = new StringBuilder();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checked, container, false);
        unbinder = ButterKnife.bind(this, view);

        mTextConclusionView = (TextView) view.findViewById(R.id.checked_conclusion_text);
        mEditDescriptionView = (EditText) view.findViewById(R.id.checked_description_text);
        mCheckOperator = (TextView) view.findViewById(R.id.tv_operator_check_main);
        mLogonOperator = (TextView) view.findViewById(R.id.tv_operator_login_main);
        mToggleIsRoom = (ToggleButton) view.findViewById(R.id.toggle_is_room);
        mToggleIsFree = (ToggleButton) view.findViewById(R.id.toggle_is_free);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_conclusion);
        mBtnSureConclusion = (Button) view.findViewById(R.id.btn_sure_conclusion);
        mSiteCheck = (TextView) view.findViewById(R.id.tv_operator_check_main);
        mSiteLogin = (TextView) view.findViewById(R.id.tv_operator_login_main);

        setOperatorInfo("check_select = ?", mCheckOperator);
        setOperatorInfo("login_select = ?", mLogonOperator);

        mTextConclusionView.setOnClickListener(this);
        mBtnSureConclusion.setOnClickListener(this);

        mTextConclusionView.setText(mConclusion_I);
        //初始化各个checkbox的状态


        String conclusion = mTextConclusionView.getText().toString();

        mCheckBoxes = new CheckBox[]{mCheck111001, mCheck222001, mCheck222002, mCheck222003,
                mCheck222004, mCheck333001, mCheck333002, mCheck333003, mCheck333004,
                mCheck444001, mCheck444002, mCheck555001, mCheck555002,
                mCheck666001, mCheck666002, mCheck666003, mCheck777001};

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
        bean.setConclusion(sDescriptionQ);
        bean.setConclusion(sSiteCheck_Q);
        bean.setConclusion(sSiteLogin_Q);
        bean.setIsRoom(sIsRoom ? 1 : 0);
        bean.setIsFree(sIsFree ? 1 : 0);

        listener.beanConnect(bean);

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
                mRelativeLayout.setVisibility(View.VISIBLE);
                mEditDescriptionView.setFocusable(false);
                initOnclick(mBuilder, mCheck111001);
                initOnclick(mBuilder, mCheck222001);
                initOnclick(mBuilder, mCheck222002);
                initOnclick(mBuilder, mCheck222003);
                initOnclick(mBuilder, mCheck222004);
                initOnclick(mBuilder, mCheck333001);
                initOnclick(mBuilder, mCheck333002);
                initOnclick(mBuilder, mCheck333003);
                initOnclick(mBuilder, mCheck333004);
                initOnclick(mBuilder, mCheck444001);
                initOnclick(mBuilder, mCheck444002);
                initOnclick(mBuilder, mCheck555001);
                initOnclick(mBuilder, mCheck555002);
                initOnclick(mBuilder, mCheck666001);
                initOnclick(mBuilder, mCheck666002);
                initOnclick(mBuilder, mCheck666003);
                initOnclick(mBuilder, mCheck777001);

                break;
            case R.id.btn_sure_conclusion:
                mRelativeLayout.setVisibility(View.GONE);
                mEditDescriptionView.setFocusable(true);
                mTextConclusionView.setText(mBuilder.toString());
                break;
            default:
                break;
        }
    }

    /**
     * 对所有的checkBox进行点击操作
     *
     * @param builder
     */
    private void initOnclick(StringBuilder builder, CheckBox checkBox) {
        checkBox.setOnClickListener(v -> {
            String newStr = "";
            if (checkBox.isChecked()) {
                builder.append(checkBox.getText() + ";");
                newStr = builder.toString();
            } else {
                String str = checkBox.getText() + ";";
                newStr = builder.toString().replaceAll(str, "");
                builder.delete(0, builder.length());
                builder.append(newStr);
            }
        });
    }

    public interface CheckedBeanConnectListener {
        void beanConnect(CheckedBean bean);
    }

}
