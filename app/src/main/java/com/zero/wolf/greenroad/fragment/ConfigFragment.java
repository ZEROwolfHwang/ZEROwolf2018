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

    private static final int REQUEST_CODE_SCAN= 901;



    Unbinder unbinder;
    @BindView(R.id.tv_change_station_config)
    TextView mTvChangeStationConfig;
    @BindView(R.id.tv_change_lane_config)
    TextView mTvChangeLaneConfig;

    @BindView(R.id.tv_operator_check_config)
    TextView mTvOperatorCheckConfig;

    @BindView(R.id.tv_operator_login_config)
    TextView mTvOperatorLoginConfig;
    @BindView(R.id.scan_qr_code)
    ImageButton mScanQrCode;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public ConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2) {
        Logger.i(param1 + "_____" + param2);
        ConfigFragment fragment = new ConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
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
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.singleToast("解析二维码失败");
                }
            }
        }
    }

    private void initView() {
        List<String> strListValue = SPListUtil.getStrListValue(getContext(), SPListUtil.APPCONFIGINFO);
        for (int i = 0; i < strListValue.size(); i++) {
            String string = strListValue.get(i).toString();
            Logger.i(string);
        }
        mTvChangeStationConfig.setText(strListValue.get(2).toString());
        // TODO: 2017/8/5
        mTvOperatorCheckConfig.setOnClickListener(v -> openSettingActivity());
        mTvOperatorLoginConfig.setOnClickListener(v -> openSettingActivity());
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
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
