package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.SureGoodsActivity111;
import com.zero.wolf.greenroad.adapter.DetailsRecyclerAdapter;
import com.zero.wolf.greenroad.bean.DetailInfoBean;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.manager.CarColorManager;
import com.zero.wolf.greenroad.manager.GlobalManager;

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
    private static RadioGroup mRadioGroupColor;
    private static
    TextView mTvChangeNumberDetail;
    private static
    TextView mTvChangeGoodsDetail;
    private static
    TextView mConfigConclusionText;
    private static
    TextView mConfigDescriptionText;
    private static
    ToggleButton mToggleIsRoom;
    private static
    ToggleButton mToggleIsFree;


    private static String mCurrent_color;


    private OnFragmentInteractionListener mListener;
    private String mCarNumber;
    private String mCarGoods;
    private String mConclusionText;
    private String mDescriptionEditText;
    private static ArrayList<MyBitmap> mMyBitmaps;
    private DetailsRecyclerAdapter mAdapter;

    public DetailsFragment() {
        // Required empty public constructor
    }


    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();

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
                mCarNumber = "车牌号格式不正确";
            }
        }));

        GoodsFragment.setTextChangedFragment(edittext -> {
            mCarGoods = edittext;
        });

        CheckFragment.setTextChangedFragment(edittext -> {
            mConclusionText = edittext;
        });
        CheckFragment.setEditChangedFragment(edittext -> {
            mDescriptionEditText = edittext;
        });

        PhotoFragment.setBitmapListListener(bitmaps -> {
            mMyBitmaps = bitmaps;
        });

        Logger.i(mCarNumber + "]]]]]]]]]");
        mTvChangeNumberDetail.setText(mCarNumber);
        mTvChangeGoodsDetail.setText(mCarGoods);
        mConfigConclusionText.setText(mConclusionText);
        mConfigDescriptionText.setText(mDescriptionEditText);

        if (mMyBitmaps != null && mMyBitmaps.size() != 0) {
            mAdapter.updateListView(mMyBitmaps);
        }
    }


    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewShootPhoto.setLayoutManager(manager);
        mAdapter = new DetailsRecyclerAdapter(getContext(), mMyBitmaps, () -> {
            enterSureActivity(GlobalManager.ENTERTYPE_PHOTO);
        });
        mRecyclerViewShootPhoto.setAdapter(mAdapter);
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
                    Logger.i(mCurrent_color);
                    mCurrent_color = CarColorManager.COLOR_WHITE;
                    break;

                default:
                    break;
            }
        });
    }

    private void initView(View view) {
        mConfigConclusionText = (TextView) view.findViewById(R.id.config_conclusion_text);
        mConfigDescriptionText = (TextView) view.findViewById(R.id.config_description_text);
        mTvChangeNumberDetail = (TextView) view.findViewById(R.id.tv_change_number_detail);
        mTvChangeGoodsDetail = (TextView) view.findViewById(R.id.tv_change_goods_detail);
        mToggleIsRoom = (ToggleButton) view.findViewById(R.id.toggle_is_room);
        mToggleIsFree = (ToggleButton) view.findViewById(R.id.toggle_is_free);
        mRadioGroupColor = (RadioGroup) view.findViewById(R.id.radio_group_color);

    }

    @OnClick({R.id.tv_change_number_detail, R.id.tv_change_goods_detail,
            R.id.config_conclusion_text, R.id.config_description_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change_number_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_NUMBER);
                break;
            case R.id.tv_change_goods_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_GOODS);

                Snackbar.make(view, "进入货物的选择业", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.config_conclusion_text:
                enterSureActivity(GlobalManager.ENTERTYPE_CHECK);
                Snackbar.make(view, "检查结论", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.config_description_text:
                enterSureActivity(GlobalManager.ENTERTYPE_CHECK);
                Snackbar.make(view, "检查说明", Snackbar.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void enterSureActivity(String type) {
        String carNumber = mTvChangeNumberDetail.getText().toString();
        String goods = mTvChangeGoodsDetail.getText().toString();
        String conclusion = mConfigConclusionText.getText().toString();
        String description = mConfigDescriptionText.getText().toString();

       /* ArrayList<MyBitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < mMyBitmaps.size(); i++) {
            MyBitmap bitmap = new MyBitmap();
            bitmap.setPath(mMyBitmaps.get(i).getPath());
            bitmap.setInfo(mMyBitmaps.get(i).getInfo());
            bitmaps.add(bitmap);
        }*/

        SerializableMain2Sure main2Sure = new SerializableMain2Sure();
        main2Sure.setCarNumber_I(carNumber);
        main2Sure.setGoods_I(goods);
        main2Sure.setConclusion_I(conclusion);
        main2Sure.setDescription_I(description);

        SureGoodsActivity111.actionStart(getActivity(), main2Sure, type);

    }

    public void onButtonPressed(Uri uri) {

        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static void setBitmapListListener(BitmapListListener listener) {
        if (mMyBitmaps != null) {
            listener.BitmapListener(mMyBitmaps);
        }
    }


    public interface BitmapListListener {
        void BitmapListener(ArrayList<MyBitmap> bitmaps);
    }

    public void setSubmitInfoListener(OnFragmentListener listener) {

        ArrayList<String> bitmap_path = new ArrayList<>();
        if (mMyBitmaps != null) {
            for (int i = 0; i < mMyBitmaps.size(); i++) {
                bitmap_path.add(mMyBitmaps.get(i).getPath());
            }
        }

        String conclusion = mConfigConclusionText.getText().toString().trim();
        String description = mConfigDescriptionText.getText().toString().trim();
        String number = mTvChangeNumberDetail.getText().toString().trim();
        String goods = mTvChangeGoodsDetail.getText().toString().trim();
        boolean isRoom = mToggleIsRoom.isChecked();
        boolean isFree = mToggleIsFree.isChecked();

        DetailInfoBean bean = new DetailInfoBean();
        bean.setColor(mCurrent_color);
        bean.setConclusion(conclusion);
        bean.setDescription(description);
        bean.setNumber(number);
        bean.setGoods(goods);
        bean.setIsRoom(isRoom ? 0 : 1);
        bean.setIsFree(isFree ? 0 : 1);
        bean.setBitmapPaths(bitmap_path);

      /*  //监听图片路径,方便图文上传
        ArrayList<String> photoPath = new ArrayList<>();
        for (int i = 0; i < mMyBitmaps.size(); i++) {
            photoPath.add(mMyBitmaps.get(i).getPath());
        }*/
        listener.onFragmentInteraction(bean);
    }

    public interface OnFragmentListener {

        void onFragmentInteraction(DetailInfoBean bean);
    }
}
