package com.android.htc.greenroad.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.htc.greenroad.R;
import com.android.htc.greenroad.SpinnerPopupWindow;
import com.android.htc.greenroad.activity.ShowActivity;
import com.android.htc.greenroad.adapter.RecycleViewDivider;
import com.android.htc.greenroad.adapter.SpinnerAdapter;
import com.android.htc.greenroad.bean.ScanInfoBean;
import com.android.htc.greenroad.litepalbean.SupportLane;
import com.android.htc.greenroad.litepalbean.SupportScan;
import com.android.htc.greenroad.tools.SPListUtil;
import com.android.htc.greenroad.tools.SPUtils;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ScanFragment extends Fragment {

    private static final int REQUEST_CODE_SCAN = 901;


    Unbinder unbinder;

    /* @BindView(R.id.scan_qr_code)
     TextView mScanQrCode;*/
   /* @BindView(R.id.btn_edit_able)
    ToggleButton mBtnEditAble;*/
    private static ToggleButton mToggleIsLimit;
    private static TextView mText_table_1;
    private static EditText mText_table_4;
    private static EditText mText_table_5;
    private static EditText mText_table_6;
    private static EditText mText_table_10;
    private static TextView mText_table_12;

    private static ScanFragment sFragment;
    private static SupportScan sSupportScan;
    private static String enterType;
    @BindView(R.id.btn_scan_lane)
    ImageButton mBtnScanLane;

    private EditText[] mEditTextsScan;
    private int mThemeTag;
    private String mLicenceNumber;
    private int mWidth;
    private SpinnerAdapter mAdapterLane;
    private SpinnerPopupWindow mPopupWindow;
    private float mDimension;
    private ArrayList<String> mLaneList;

    public ScanFragment() {
        // Required empty public constructor
    }

    public static ScanFragment newInstance(String type) {
        if (sFragment == null) {
            sFragment = new ScanFragment();
        }
        enterType = type;
        return sFragment;
    }

    public static ScanFragment newInstance(String type, SupportScan scanInfoBean) {
        if (sFragment == null) {
            sFragment = new ScanFragment();
        }
        sSupportScan = scanInfoBean;
        enterType = type;
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThemeTag = (int) SPUtils.get(getContext(), SPUtils.KEY_THEME_TAG, 1);
        mDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120,
                getResources().getDisplayMetrics());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView(view);
        return view;
    }

    private void initView(View view) {


        mText_table_1 = (TextView) view.findViewById(R.id.text_table_1);
        mText_table_4 = (EditText) view.findViewById(R.id.text_table_4);
        mText_table_5 = (EditText) view.findViewById(R.id.text_table_5);
        mText_table_6 = (EditText) view.findViewById(R.id.text_table_6);
        mText_table_10 = (EditText) view.findViewById(R.id.text_table_10);
        mText_table_12 = (TextView) view.findViewById(R.id.text_table_12);
        mToggleIsLimit = (ToggleButton) view.findViewById(R.id.toggle_is_limit);

        String station = SPListUtil.getStrListValue(getActivity(), SPListUtil.APPCONFIGINFO).get(2);
        if (station != null && !"".equals(station)) {
            mText_table_10.setText(station);
        }

        mText_table_12.setText(SPUtils.get(getActivity(), SPUtils.TEXTLANE, "X08") + "");

        mEditTextsScan = new EditText[]{mText_table_4, mText_table_5,
                mText_table_6,
        };


        //从草稿的详情页进入采集界面进行修改,会初始化扫描的内容
        if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(enterType)) {
            mText_table_1.setText(sSupportScan.getScan_01Q());
            mText_table_4.setText(sSupportScan.getScan_04Q());
            mText_table_5.setText(sSupportScan.getScan_05Q());
            mText_table_6.setText(sSupportScan.getScan_06Q());
            mText_table_10.setText(sSupportScan.getScan_10Q());
            mText_table_12.setText(sSupportScan.getScan_12Q());
            mToggleIsLimit.setChecked(sSupportScan.getIsLimit() == 0 ? false : true);

        } else {
            mToggleIsLimit.setChecked(true);
        }

    }


    @OnClick({
            R.id.toggle_is_limit,
            R.id.btn_scan_lane})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toggle_is_limit:
                isLimit();
                break;

            case R.id.btn_scan_lane:
                List<SupportLane> laneList = DataSupport.findAll(SupportLane.class);
                if (mLaneList == null) {
                    mLaneList = new ArrayList<>();
                } else {
                    mLaneList.clear();
                }
                mLaneList.addAll(laneList.get(0).getLane());

                mWidth = mText_table_12.getWidth();
                mAdapterLane = new SpinnerAdapter((AppCompatActivity) getActivity(), mLaneList, position -> {
                    mText_table_12.setText(mLaneList.get(position));
                    mPopupWindow.dismissPopWindow();
                });

                mPopupWindow = new SpinnerPopupWindow.Builder(getActivity())
                        .setmLayoutManager(null, 0)
                        .setmAdapter(mAdapterLane)
                        .setmItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 5, Color.GRAY))
                        .setmHeight(600).setmWidth(mWidth)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();

                mPopupWindow.showPopWindow(view, (int) mDimension);
                break;
            default:
                break;
        }
    }

    /**
     * 超限率,是否超限,默认否
     */
    private void isLimit() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static void setScanConnectListener(ScanBeanConnectListener listener) {

        String scan_01Q = mText_table_1.getText().toString().trim();
        String scan_04Q = mText_table_4.getText().toString().trim();
        String scan_05Q = mText_table_5.getText().toString().trim();
        String scan_06Q = mText_table_6.getText().toString().trim();
        String scan_10Q = mText_table_10.getText().toString().trim();
        String scan_12Q = mText_table_12.getText().toString().trim();
        boolean isLimit = mToggleIsLimit.isChecked();

        ScanInfoBean bean = new ScanInfoBean();

        bean.setScan_01Q(scan_01Q);
        bean.setScan_04Q(scan_04Q);
        bean.setScan_05Q(scan_05Q);
        bean.setScan_06Q(scan_06Q);
        bean.setScan_10Q(scan_10Q);
        bean.setScan_12Q(scan_12Q);
        bean.setIsLimit(isLimit ? 0 : 1);



        Logger.i("!!!!!!!!!!!!!!!!!" + bean.toString());
        listener.beanConnect(bean);
    }

    public static void notifyNumberChange(String carNumber) {
        if (carNumber != null && mText_table_1 != null) {
            Logger.i(carNumber);
            mText_table_1.setText(carNumber);
        }
    }


    public interface ScanBeanConnectListener {
        void beanConnect(ScanInfoBean bean);
    }
}
