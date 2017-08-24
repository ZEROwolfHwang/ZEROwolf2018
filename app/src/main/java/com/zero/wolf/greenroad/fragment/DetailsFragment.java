package com.zero.wolf.greenroad.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.ShowActivity;
import com.zero.wolf.greenroad.activity.SureGoodsActivity;
import com.zero.wolf.greenroad.adapter.DetailsRecyclerAdapter;
import com.zero.wolf.greenroad.bean.DetailInfoBean;
import com.zero.wolf.greenroad.bean.PathTitleBean;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.litepalbean.SupportDetail;
import com.zero.wolf.greenroad.manager.CarColorManager;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class DetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;
    @BindView(R.id.recycler_view_shoot_photo)
    RecyclerView mRecyclerViewShootPhoto;
    private static TextView mTvChangeNumberDetail;
    private static TextView mTvChangeGoodsDetail;

    private static String mCurrent_color;

    private String mCarNumber;
    private String mCarGoods;

    private static ArrayList<MyBitmap> mMyBitmaps_sanzheng;
    private static ArrayList<MyBitmap> mMyBitmaps_cheshen;
    private static ArrayList<MyBitmap> mMyBitmaps_huowu;
    private DetailsRecyclerAdapter mAdapter;
    private ArrayList<MyBitmap> mMyBitmaps_recycler_all;
    private static Bitmap mBitmap_add;
    private RadioGroup mRadioGroup_top;
    private RadioGroup mRadioGroup_bottom;
    private static RadioButton mLicense_yellow;
    private static RadioButton mLicense_blue;
    private static RadioButton mLicense_black;
    private static RadioButton mLicense_white;
    private static RadioButton mLicense_green;
    private LinearLayoutManager mLayoutManager;

    private static RadioGroup mRadioGroupColor;
    private static String sEnterType;
    private static SupportDetail sSupportDetail;

    public DetailsFragment() {
        // Required empty public constructor
    }


    public static DetailsFragment newInstance(String enterType) {
        DetailsFragment fragment = new DetailsFragment();
        sEnterType = enterType;
        return fragment;
    }
  public static DetailsFragment newInstance(String enterType , SupportDetail supportDetail) {
        DetailsFragment fragment = new DetailsFragment();
      sEnterType = enterType;
      sSupportDetail = supportDetail;
      return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);

        initRadioColor();
        initRecyclerView();
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        CarNumberFragment.setTextChangedFragment((edittext -> {
            if (edittext.length() == 7) {
                mCarNumber = edittext;
            } else {
                mCarNumber = "";
            }
        }));

        GoodsFragment.setTextChangedFragment(edittext -> {
            mCarGoods = edittext;
        });

        PhotoFragment.setBitmapListListener((mSanZhengBitmaps, mCheShenBitmaps, mHuowuBitmaps) -> {
            mMyBitmaps_sanzheng = mSanZhengBitmaps;
            mMyBitmaps_cheshen = mCheShenBitmaps;
            mMyBitmaps_huowu = mHuowuBitmaps;
        });
        if (mMyBitmaps_recycler_all == null) {
            initRecyclerAdd();
        } else {
            mMyBitmaps_recycler_all.clear();
            initRecyclerAdd();
        }
        if (mMyBitmaps_sanzheng != null && mMyBitmaps_sanzheng.size() != 0) {
            for (int i = 0; i < mMyBitmaps_sanzheng.size() - 1; i++) {
                mMyBitmaps_recycler_all.add(mMyBitmaps_sanzheng.get(i));
            }
        }
        if (mMyBitmaps_cheshen != null && mMyBitmaps_cheshen.size() != 0) {
            for (int i = 0; i < mMyBitmaps_cheshen.size() - 1; i++) {
                mMyBitmaps_recycler_all.add(mMyBitmaps_cheshen.get(i));
            }
        }
        if (mMyBitmaps_huowu != null && mMyBitmaps_huowu.size() != 0) {
            for (int i = 0; i < mMyBitmaps_huowu.size() - 1; i++) {
                mMyBitmaps_recycler_all.add(mMyBitmaps_huowu.get(i));
            }
        }

        Logger.i(mCarNumber + "]]]]]]]]]");
        mTvChangeNumberDetail.setText(mCarNumber);
        mTvChangeGoodsDetail.setText(mCarGoods);

        if (mMyBitmaps_recycler_all != null && mMyBitmaps_recycler_all.size() != 0) {
            mAdapter.updateListView(mMyBitmaps_recycler_all);

            if (mMyBitmaps_recycler_all.size() > 3 && mLayoutManager != null) {
                scrollToPosition(mLayoutManager, 3);
            }
        }
    }

    private void scrollToPosition(LinearLayoutManager manager, int index) {

        try {
            manager.scrollToPositionWithOffset(index,
                    (int) manager.computeScrollVectorForPosition(index).y);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("重新进入照片数据异常");
        }

    }


    private void initRecyclerView() {
        initRecyclerAdd();
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewShootPhoto.setLayoutManager(mLayoutManager);
        mAdapter = new DetailsRecyclerAdapter(getContext(), mMyBitmaps_recycler_all, () -> {
            enterSureActivity(GlobalManager.ENTERTYPE_PHOTO);
        });
//        mRecyclerViewShootPhoto.scrollToPosition(3);
        // scrollToPosition(mLayoutManager,3);
        mRecyclerViewShootPhoto.setAdapter(mAdapter);
        /*LinearLayoutManager llm = (LinearLayoutManager) mRecyclerViewShootPhoto.getLayoutManager();
        llm.scrollToPositionWithOffset(3, 0);
        llm.setStackFromEnd(false);*/

    }

    private void initRecyclerAdd() {
        if (mMyBitmaps_recycler_all == null) {
            mMyBitmaps_recycler_all = new ArrayList<>();
        }
        mBitmap_add = BitmapFactory.decodeResource(getResources(), R.drawable.image_photo_add);
        MyBitmap bitmap = null;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                bitmap = new MyBitmap(mBitmap_add, "三证");
            }
            if (i == 1) {
                bitmap = new MyBitmap(mBitmap_add, "车身车型");
            }
            if (i == 2) {
                bitmap = new MyBitmap(mBitmap_add, "货照");
            }
            mMyBitmaps_recycler_all.add(bitmap);
        }
    }


    private void initView(View view) {

        mTvChangeNumberDetail = (TextView) view.findViewById(R.id.tv_change_number_detail);
        mTvChangeGoodsDetail = (TextView) view.findViewById(R.id.tv_change_goods_detail);
        mRadioGroupColor = (RadioGroup) view.findViewById(R.id.radio_group_color);


        mLicense_yellow = (RadioButton) view.findViewById(R.id.license_yellow);
        mLicense_blue = (RadioButton) view.findViewById(R.id.license_blue);
        mLicense_black = (RadioButton) view.findViewById(R.id.license_black);
        mLicense_white = (RadioButton) view.findViewById(R.id.license_green);
        mLicense_green = (RadioButton) view.findViewById(R.id.license_white);

        if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(sEnterType)) {
            Logger.i(sSupportDetail.toString());

            mTvChangeNumberDetail.setText(sSupportDetail.getNumber());
            mTvChangeGoodsDetail.setText(sSupportDetail.getGoods());

            String color = sSupportDetail.getColor();
            if (CarColorManager.COLOR_YELLOW.equals(color)) {
                mLicense_yellow.setChecked(true);
                mCurrent_color = CarColorManager.COLOR_YELLOW;
            }
            else if (CarColorManager.COLOR_BLUE.equals(color)) {
                mLicense_blue.setChecked(true);
                mCurrent_color = CarColorManager.COLOR_BLUE;
            }
            else if (CarColorManager.COLOR_BLACK.equals(color)) {
                mLicense_black.setChecked(true);
                mCurrent_color = CarColorManager.COLOR_BLACK;
            }
            else if (CarColorManager.COLOR_GREEN.equals(color)) {
                mLicense_green.setChecked(true);
                mCurrent_color = CarColorManager.COLOR_GREEN;
            }
            else if (CarColorManager.COLOR_WHITE.equals(color)) {
                mLicense_white.setChecked(true);
                mCurrent_color = CarColorManager.COLOR_WHITE;
            }

        }

    }

    private void initRadioColor() {

        mRadioGroupColor.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.license_yellow:
                    mCurrent_color = CarColorManager.COLOR_YELLOW;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_blue:
                    mCurrent_color = CarColorManager.COLOR_BLUE;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_black:
                    mCurrent_color = CarColorManager.COLOR_BLACK;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_green:
                    mCurrent_color = CarColorManager.COLOR_GREEN;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_white:
                    mCurrent_color = CarColorManager.COLOR_WHITE;
                    Logger.i(mCurrent_color);
                    break;

                default:
                    break;
            }
        });
    }
    @OnClick({R.id.tv_change_number_detail, R.id.tv_change_goods_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change_number_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_NUMBER);
                ToastUtils.singleToast("进入车牌号的选择业");
                break;
            case R.id.tv_change_goods_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_GOODS);
                ToastUtils.singleToast("进入货物的选择业");
                break;
            default:
                break;
        }
    }

    private void enterSureActivity(String type) {
        String carNumber = mTvChangeNumberDetail.getText().toString();
        String goods = mTvChangeGoodsDetail.getText().toString();

        SerializableMain2Sure main2Sure = new SerializableMain2Sure();
        main2Sure.setCarNumber_I(carNumber);
        main2Sure.setGoods_I(goods);

        SureGoodsActivity.actionStart(getActivity(), main2Sure, type);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 将数据再传递进子fragment的回调操作
     *
     * @param listener
     */
    public static void setBitmapListListener(BitmapListListener listener) {
        if (mMyBitmaps_sanzheng != null && mMyBitmaps_cheshen != null && mMyBitmaps_huowu != null) {

            listener.BitmapListener(mMyBitmaps_sanzheng, mMyBitmaps_cheshen, mMyBitmaps_huowu);
        }
    }

    public interface BitmapListListener {
        void BitmapListener(ArrayList<MyBitmap> mMyBitmaps_sanzheng,
                            ArrayList<MyBitmap> mMyBitmaps_cheshen, ArrayList<MyBitmap> mMyBitmaps_huowu);
    }

    public static void setDetailsConnectListener(DetailsBeanConnectListener listener) {

        ArrayList<PathTitleBean> pathTitleList_sanzheng = new ArrayList<>();
        ArrayList<PathTitleBean> pathTitleList_cheshen = new ArrayList<>();
        ArrayList<PathTitleBean> pathTitleList_huowu = new ArrayList<>();
        if (mMyBitmaps_sanzheng != null) {
            for (int i = 0; i < mMyBitmaps_sanzheng.size(); i++) {
                MyBitmap myBitmap = mMyBitmaps_sanzheng.get(i);
                PathTitleBean titleBean = new PathTitleBean(myBitmap.getPath(), myBitmap.getTitle());
                pathTitleList_sanzheng.add(titleBean);
            }
        }
       /* if (mMyBitmaps_cheshen != null) {
            for (int i = 0; i < mMyBitmaps_cheshen.size(); i++) {
                MyBitmap myBitmap = mMyBitmaps_cheshen.get(i);
                PathTitleBean titleBean = new PathTitleBean(myBitmap.getPath(), myBitmap.getTitle());
                pathTitleList_cheshen.add(titleBean);
            }
        }
        if (mMyBitmaps_huowu != null) {
            for (int i = 0; i < mMyBitmaps_huowu.size(); i++) {
                MyBitmap myBitmap = mMyBitmaps_huowu.get(i);
                PathTitleBean titleBean = new PathTitleBean(myBitmap.getPath(), myBitmap.getTitle());
                pathTitleList_huowu.add(titleBean);
            }
        }*/



        String number = mTvChangeNumberDetail.getText().toString().trim();
        String goods = mTvChangeGoodsDetail.getText().toString().trim();

        DetailInfoBean bean = new DetailInfoBean();
        bean.setColor(mCurrent_color);
        bean.setNumber(number);
        bean.setGoods(goods);
        bean.setPath_sanzheng(pathTitleList_sanzheng);
        bean.setPath_cheshen(pathTitleList_cheshen);
        bean.setPath_huowu(pathTitleList_huowu);
        listener.beanConnect(bean);
    }

    public interface DetailsBeanConnectListener {

        void beanConnect(DetailInfoBean bean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyBitmaps_recycler_all.clear();

    }
}
