package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.SettingActivity;
import com.zero.wolf.greenroad.bean.ConfigInfoBean;
import com.zero.wolf.greenroad.litepalbean.SupportOperator;
import com.zero.wolf.greenroad.tools.SPListUtil;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_CODE_SCAN = 901;


    Unbinder unbinder;
    @BindView(R.id.tv_change_station_config)
    TextView mTvChangeStationConfig;
    @BindView(R.id.tv_change_lane_config)
    TextView mTvChangeLaneConfig;

    //@BindView(R.id.tv_operator_check_config)
    private  static TextView mTvOperatorCheckConfig;

    //@BindView(R.id.tv_operator_login_config)
    private static TextView mTvOperatorLoginConfig;
    @BindView(R.id.scan_qr_code)
    ImageButton mScanQrCode;


    // TODO: Rename and change types of parameters


    private OnFragmentListener mListener;
    private static TextView mTextExportNumber;
    private static TextView mText_table_1;
    private static TextView mText_table_2;
    private static TextView mText_table_3;
    private static TextView mText_table_4;
    private static TextView mText_table_5;
    private static TextView mText_table_6;
    private static TextView mText_table_7;
    private static TextView mText_table_8;
    private static TextView mText_table_9;
    private static TextView mText_table_10;
    private static TextView mText_table_11;
    private static TextView mText_table_12;
    private String mRoad_Q;
    private String mStation_Q;

    public ConfigFragment() {
        // Required empty public constructor
    }

    public static ConfigFragment newInstance() {
        ConfigFragment fragment = new ConfigFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);
        unbinder = ButterKnife.bind(this, view);

        mTvOperatorCheckConfig= (TextView) view.findViewById(R.id.tv_operator_check_config);
        mTvOperatorLoginConfig= (TextView) view.findViewById(R.id.tv_operator_login_config);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mTextExportNumber = (TextView) view.findViewById(R.id.export_number);
        mText_table_1 = (TextView) view.findViewById(R.id.text_table_1);
        mText_table_2 = (TextView) view.findViewById(R.id.text_table_2);
        mText_table_3 = (TextView) view.findViewById(R.id.text_table_3);
        mText_table_4 = (TextView) view.findViewById(R.id.text_table_4);
        mText_table_5 = (TextView) view.findViewById(R.id.text_table_5);
        mText_table_6 = (TextView) view.findViewById(R.id.text_table_6);
        mText_table_7 = (TextView) view.findViewById(R.id.text_table_7);
        mText_table_8 = (TextView) view.findViewById(R.id.text_table_8);
        mText_table_9 = (TextView) view.findViewById(R.id.text_table_9);
        mText_table_10 = (TextView) view.findViewById(R.id.text_table_10);
        mText_table_11 = (TextView) view.findViewById(R.id.text_table_11);
        mText_table_12 = (TextView) view.findViewById(R.id.text_table_12);
    }
    private void initData() {
        // TODO: 2017/8/14 拿到初始化注册时的注册信息
        List<String> strListValue = SPListUtil.getStrListValue(getContext(), SPListUtil.APPCONFIGINFO);
        for (int i = 0; i < strListValue.size(); i++) {
            String string = strListValue.get(i).toString();
            Logger.i(string);
        }
        mRoad_Q = strListValue.get(1).toString();
        mStation_Q = strListValue.get(2).toString();
        mTvChangeStationConfig.setText(mStation_Q);
        // TODO: 2017/8/5
        mTvOperatorCheckConfig.setOnClickListener(v -> openSettingActivity());
        mTvOperatorLoginConfig.setOnClickListener(v -> openSettingActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        setOperatorInfo("check_select = ?", mTvOperatorCheckConfig);
        setOperatorInfo("login_select = ?", mTvOperatorLoginConfig);
        mTvChangeLaneConfig.setText((String) SPUtils.get(getActivity(), SPUtils.TEXTLANE, "66"));

    }

    @OnClick(R.id.scan_qr_code)
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    /**
     * 对扫描二维码后的信息进行解析
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 处理二维码扫描结果
        if (requestCode == REQUEST_CODE_SCAN) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    ToastUtils.singleToast("解析结果:" + result);
                    String[] result_QrCode = result.split(";");
                    mTextExportNumber.setText(result_QrCode[0]);

                    mText_table_1.setText(result_QrCode[1]);
                    mText_table_3.setText(result_QrCode[10]);
                    mText_table_5.setText(result_QrCode[14]);
                    //入口路段
                    mText_table_7.setText(result_QrCode[2]);
                    mText_table_9.setText(result_QrCode[3]);
                    mText_table_11.setText(result_QrCode[6]);

                    mText_table_2.setText(result_QrCode[11]);
                    mText_table_4.setText(result_QrCode[12]);
                    mText_table_6.setText(result_QrCode[13]);
                    //出口路段
                    mText_table_8.setText(result_QrCode[4]);
                    mText_table_10.setText(result_QrCode[5]);
                    mText_table_12.setText(result_QrCode[9]);

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.singleToast("解析二维码失败");
                }
            }
        }
    }



    private void openSettingActivity() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
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
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentListener) {
            mListener = (OnFragmentListener) context;
            if (mTvOperatorCheckConfig != null && mTvOperatorLoginConfig != null) {
                mListener.onFragmentInteraction(mTvOperatorCheckConfig.getText().toString().trim(),
                        mTvOperatorLoginConfig.getText().toString().trim());
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public void setSubmitInfoListener(OnFragmentListener listener) {
        String checkOperator_Q = mTvOperatorCheckConfig.getText().toString().trim();
        String loginOperator_Q = mTvOperatorLoginConfig.getText().toString().trim();

        String scan_01Q = mText_table_1.getText().toString().trim();
        String scan_02Q = mText_table_2.getText().toString().trim();
        String scan_03Q = mText_table_3.getText().toString().trim();
        String scan_04Q = mText_table_4.getText().toString().trim();
        String scan_05Q = mText_table_5.getText().toString().trim();
        String scan_06Q = mText_table_6.getText().toString().trim();
        String scan_07Q = mText_table_7.getText().toString().trim();
        String scan_08Q = mText_table_8.getText().toString().trim();
        String scan_09Q = mText_table_9.getText().toString().trim();
        String scan_10Q = mText_table_10.getText().toString().trim();
        String scan_11Q = mText_table_11.getText().toString().trim();
        String scan_12Q = mText_table_12.getText().toString().trim();

        ConfigInfoBean bean = new ConfigInfoBean();
        bean.setCheckOperator(checkOperator_Q);
        bean.setLoginOperator(loginOperator_Q);
        bean.setScan_01Q(scan_01Q);
        bean.setScan_02Q(scan_02Q);
        bean.setScan_03Q(scan_03Q);
        bean.setScan_04Q(scan_04Q);
        bean.setScan_05Q(scan_05Q);
        bean.setScan_06Q(scan_06Q);
        bean.setScan_07Q(scan_07Q);
        bean.setScan_08Q(scan_08Q);
        bean.setScan_09Q(scan_09Q);
        bean.setScan_10Q(scan_10Q);
        bean.setScan_11Q(scan_11Q);
        bean.setScan_12Q(scan_12Q);

        listener.onFragmentInteraction(bean);
    }

    public interface OnFragmentListener {
        void onFragmentInteraction(ConfigInfoBean bean);
    }
}
