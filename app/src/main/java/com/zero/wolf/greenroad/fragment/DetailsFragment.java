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

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.SureGoodsActivity111;
import com.zero.wolf.greenroad.adapter.DetailsRecyclerAdapter;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.manager.CarColorManager;
import com.zero.wolf.greenroad.manager.GlobalManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;
    @BindView(R.id.radio_group_color)
    RadioGroup mRadioGroupColor;
    @BindView(R.id.recycler_view_shoot_photo)
    RecyclerView mRecyclerViewShootPhoto;
    @BindView(R.id.tv_change_number_detail)
    TextView mTvChangeNumberDetail;
    @BindView(R.id.tv_change_goods_detail)
    TextView mTvChangeGoodsDetail;
    @BindView(R.id.config_conclusion_text)
    TextView mConfigConclusionText;
    @BindView(R.id.config_description_text)
    TextView mConfigDescriptionText;


    private String mCurrent_color;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String mCarNumber;
    private String mCarGoods;
    private String mConclusionText;
    private String mDescriptionEditText;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRadioColor();
        initView();
        initRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        CarNumberFragment.setTextChangedFragment((edittext -> {
            mCarNumber = edittext;
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


        Logger.i(mCarNumber + "]]]]]]]]]");
        mTvChangeNumberDetail.setText(mCarNumber);
        mTvChangeGoodsDetail.setText(mCarGoods);
        mConfigConclusionText.setText(mConclusionText);
        mConfigDescriptionText.setText(mDescriptionEditText);

    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewShootPhoto.setLayoutManager(manager);
        DetailsRecyclerAdapter adapter = new DetailsRecyclerAdapter(getContext());
        mRecyclerViewShootPhoto.setAdapter(adapter);
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

    private void initView() {

    }

    @OnClick({R.id.tv_change_number_detail, R.id.tv_change_goods_detail,
            R.id.btn_conclusion_selection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change_number_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_NUMBER);
                break;
            case R.id.tv_change_goods_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_GOODS);

                Snackbar.make(view, "进入货物的选择业", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.btn_conclusion_selection:
                enterSureActivity(GlobalManager.ENTERTYPE_CHECK);
                Snackbar.make(view, "检查结论", Snackbar.LENGTH_SHORT).show();
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

        SerializableMain2Sure main2Sure = new SerializableMain2Sure();
        main2Sure.setCarNumber_I(carNumber);
        main2Sure.setGoods_I(goods);
        main2Sure.setConclusion_I(conclusion);
        main2Sure.setDescription_I(description);


        SureGoodsActivity111.actionStart(getActivity(),main2Sure,type);

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
        void onFragmentInteraction(Uri uri);
    }
}
