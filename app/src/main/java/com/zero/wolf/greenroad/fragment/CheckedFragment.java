package com.zero.wolf.greenroad.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.SpinnerPopupWindow;
import com.zero.wolf.greenroad.activity.ConclusionActivity;
import com.zero.wolf.greenroad.activity.ShowActivity;
import com.zero.wolf.greenroad.adapter.BasePhotoAdapter;
import com.zero.wolf.greenroad.adapter.BasePhotoViewHolder;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.bean.CheckedBean;
import com.zero.wolf.greenroad.litepalbean.SupportChecked;
import com.zero.wolf.greenroad.litepalbean.SupportOperator;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
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
    private static EditText mEditDescriptionView;
    private static CheckedFragment sFragment;


    private static boolean sIsFree;
    private static boolean sIsRoom;
    private static TextView mSiteCheck;
    private static TextView mSiteLogin;
    private String mConclusions;

    private static String mLoginOperator;
    private static String mCheckOperator;
    private static String sConclusionQ;
    private static String sDescriptionQ;
    private static String sEnterType;
    private static SupportChecked sSupportChecked;
    private List<SupportOperator> mOperatorList;
    private SpinnerPopupWindow mPopupWindow_check;
    private SpinnerPopupWindow mPopupWindow_login;
    private ArrayList<String> mOperators;
    private int mDimension;


    public CheckedFragment() {

    }


    public static CheckedFragment newInstance(String enterType) {
        if (sFragment == null) {
            sFragment = new CheckedFragment();
        }
        sEnterType = enterType;
        return sFragment;
    }

    public static CheckedFragment newInstance(String enterType, SupportChecked supportChecked) {
        if (sFragment == null) {
            sFragment = new CheckedFragment();
        }
        sEnterType = enterType;
        sSupportChecked = supportChecked;
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

        initData();
        initView(view);

        //初始化各个checkbox的状态


        return view;
    }

    private void initData() {
        mOperatorList = DataSupport.findAll(SupportOperator.class);
        mOperators = new ArrayList<>();
        for (int i = 0; i < mOperatorList.size(); i++) {
            mOperators.add(mOperatorList.get(i).getJob_number() + "/" +
                    mOperatorList.get(i).getOperator_name());
        }

        mDimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120,
                getResources().getDisplayMetrics());

    }

    private void initView(View view) {
        mTextConclusionView = (TextView) view.findViewById(R.id.checked_conclusion_text);
        mEditDescriptionView = (EditText) view.findViewById(R.id.checked_description_text);
        mToggleIsRoom = (ToggleButton) view.findViewById(R.id.toggle_is_room);
        mToggleIsFree = (ToggleButton) view.findViewById(R.id.toggle_is_free);
        mSiteCheck = (TextView) view.findViewById(R.id.site_check_operator);
        mSiteLogin = (TextView) view.findViewById(R.id.site_login_operator);


        mTextConclusionView.setOnClickListener(this);
        mSiteCheck.setOnClickListener(this);
        mSiteLogin.setOnClickListener(this);

        if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(sEnterType)) {
            ConclusionActivity.notifyDataChangeFromDraft(sSupportChecked.getConclusion());
            mEditDescriptionView.setText(sSupportChecked.getDescription());
            //默认是是   是为1   点击是否 否为0（有点绕）
            mToggleIsRoom.setChecked(sSupportChecked.getIsRoom() == 0 ? true : false);
            mToggleIsFree.setChecked(sSupportChecked.getIsFree() == 0 ? true : false);
            mCheckOperator = sSupportChecked.getSiteCheck();
            mLoginOperator = sSupportChecked.getSiteLogin();
        } else if (ShowActivity.TYPE_MAIN_ENTER_SHOW.equals(sEnterType)) {
            mCheckOperator = setOperatorInfo("check_select = ?");
            mLoginOperator = setOperatorInfo("login_select = ?");
        }
     /*   if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(sEnterType)) {
            ConclusionActivity.notifyDataChangeFromDraft(sSupportChecked.getConclusion());
            mEditDescriptionView.setText(sSupportChecked.getDescription());
            //默认是是   是为1   点击是否 否为0（有点绕）
            mToggleIsRoom.setChecked(sSupportChecked.getIsRoom() == 0 ? true : false);
            mToggleIsFree.setChecked(sSupportChecked.getIsFree() == 0 ? true : false);
        } else if (ShowActivity.TYPE_MAIN_ENTER_SHOW.equals(sEnterType)) {
            mCheckOperator = setOperatorInfo("check_select = ?", mSiteCheck);
            mLoginOperator = setOperatorInfo("login_select = ?", mSiteLogin);
        }*/
            mSiteCheck.setText(mCheckOperator);
            mSiteLogin.setText(mLoginOperator);
    }


    private String setOperatorInfo(String condition) {
        List<SupportOperator> operatorList = DataSupport.where(condition, "1").find(SupportOperator.class);
        if (operatorList.size() != 0) {
            Logger.i(operatorList.toString());
            String mJob_number = operatorList.get(0).getJob_number();
            String mOperator_name = operatorList.get(0).getOperator_name();
            return mJob_number + "/" + mOperator_name;
        }
        return "500001/苏三";
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
        CheckedBean bean = new CheckedBean();

        bean.setConclusion(sConclusionQ);
        bean.setDescription(sDescriptionQ);
        if (mCheckOperator != null) {
            bean.setSiteCheck(mSiteCheck.getText().toString());
        }
        if (mLoginOperator != null) {
            bean.setSiteLogin(mSiteLogin.getText().toString());
        }
        //默认是"是"的状态,所以点击后为true,返回0"否"的状态
        bean.setIsRoom(sIsRoom ? 0 : 1);
        bean.setIsFree(sIsFree ? 0 : 1);

        Logger.i(bean.toString());
        listener.beanConnect(bean);

    }

    @Override
    public void onResume() {
        super.onResume();
        ConclusionActivity.setTextChangeListener(conclusions -> {
            mConclusions = conclusions;
        });
        mTextConclusionView.setText(mConclusions);

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
                BasePhotoAdapter<String> adapter_check = new BasePhotoAdapter<String>(getContext(), R.layout.item_black_text, mOperators) {
                    @Override
                    public void convert(BasePhotoViewHolder holder, int position, String s) {
                        TextView textView = holder.getView(R.id.text_item_black);
                        textView.setText(s);
                        textView.setOnClickListener(v1 -> {
                            mSiteCheck.setText(s);
                            mPopupWindow_check.dismissPopWindow();
                        });
                    }
                };

                mPopupWindow_check = new SpinnerPopupWindow.Builder(getContext())
                        .setmLayoutManager(null, 0)
                        .setmAdapter(adapter_check)
                        .setmItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 5, Color.DKGRAY))
                        .setmHeight(800).setmWidth(600)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();

                mPopupWindow_check.showPopWindow(v, mDimension);
                break;
            case R.id.site_login_operator:
                BasePhotoAdapter<String> adapter_login = new BasePhotoAdapter<String>(getContext(), R.layout.item_black_text, mOperators) {
                    @Override
                    public void convert(BasePhotoViewHolder holder, int position, String s) {
                        TextView textView = holder.getView(R.id.text_item_black);
                        textView.setText(s);
                        textView.setOnClickListener(v1 -> {
                            mSiteLogin.setText(s);
                            mPopupWindow_login.dismissPopWindow();
                        });
                    }
                };

                mPopupWindow_login = new SpinnerPopupWindow.Builder(getContext())
                        .setmLayoutManager(null, 0)
                        .setmAdapter(adapter_login)
                        .setmItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 5, Color.DKGRAY))
                        .setmHeight(800).setmWidth(600)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();

                mPopupWindow_login.showPopWindow(v, mDimension);
                break;


            default:
                break;
        }
    }

    public interface CheckedBeanConnectListener {
        void beanConnect(CheckedBean bean);
    }

}
