package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.interfacy.TextFragmentListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CheckFragment extends Fragment {


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

    Unbinder unbinder;

    private static final String ARG_CONCLUSION = "conclusion";
    private static final String ARG_DESCRIPTION = "description";

    private OnFragmentInteractionListener mListener;
    private static TextView mTextConclusionView;
    private static EditText mEditDescriptionView;
    private String mConclusion_I;
    private String mDescription_I;
    private StringBuilder mBuilder;
    private CheckBox[] mCheckBoxes;

    public CheckFragment() {

    }


    public static CheckFragment newInstance(String conclusion, String description) {
        CheckFragment fragment = new CheckFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONCLUSION, conclusion);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mConclusion_I = getArguments().getString(ARG_CONCLUSION);
            if (mBuilder == null) {
                mBuilder = new StringBuilder();
                mBuilder.append(mConclusion_I);
            }
            mDescription_I = getArguments().getString(ARG_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        unbinder = ButterKnife.bind(this, view);

        mTextConclusionView = (TextView) view.findViewById(R.id.check_fragment_text);
        mEditDescriptionView = (EditText) view.findViewById(R.id.check_fragment_edit);

        mTextConclusionView.setText(mConclusion_I);
        mEditDescriptionView.setText(mDescription_I);
        //初始化各个checkbox的状态

        mCheckBoxes = new CheckBox[]{mCheck111001, mCheck222001, mCheck222002, mCheck222003,
                mCheck222004, mCheck333001, mCheck333002, mCheck333003, mCheck333004,
                mCheck444001, mCheck444002,mCheck555001, mCheck555002,
                mCheck666001, mCheck666002, mCheck666003, mCheck777001};

        initCheckBox();


        return view;
    }

    /**
     * 进入界面时首先将点击的checkbox显示出来
     */
    private void initCheckBox() {
        initCheckBoxItem(mCheck111001);
        initCheckBoxItem(mCheck222001);
        initCheckBoxItem(mCheck222002);
        initCheckBoxItem(mCheck222003);
        initCheckBoxItem(mCheck222004);
        initCheckBoxItem(mCheck333001);
        initCheckBoxItem(mCheck333002);
        initCheckBoxItem(mCheck333003);
        initCheckBoxItem(mCheck333004);
        initCheckBoxItem(mCheck444001);
        initCheckBoxItem(mCheck444002);
        initCheckBoxItem(mCheck555001);
        initCheckBoxItem(mCheck555002);
        initCheckBoxItem(mCheck666001);
        initCheckBoxItem(mCheck666002);
        initCheckBoxItem(mCheck666003);
        initCheckBoxItem(mCheck777001);
    }

    private void initCheckBoxItem(CheckBox checkBox) {
        if (mConclusion_I.contains(checkBox.getText().toString() + ";")) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


       // initOnclick101(mBuilder, mCheck111001);
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

    }

   /* private void initOnclick101(StringBuilder builder, CheckBox checkBox) {
        checkBox.setOnClickListener(v -> {
            String newStr = "";
            if (checkBox.isChecked()) {
                builder.delete(0, builder.length());
                builder.append(checkBox.getText() + ";");
                newStr = builder.toString();
                for (int i = 1; i < mCheckBoxes.length - 1; i++) {
                    if (mCheckBoxes[i].isChecked()) {
                        mCheckBoxes[i].setChecked(false);
                    }
                }
            } else {
                String str = checkBox.getText() + ";";
                newStr = builder.toString().replaceAll(str, "");
                builder.delete(0, builder.length());
                builder.append(newStr);
            }
            mTextConclusionView.setText(builder.toString());
        });
    }*/

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
            mTextConclusionView.setText(builder.toString());
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static void setTextChangedFragment(TextFragmentListener listener) {
        if (mTextConclusionView != null) {
            String number = mTextConclusionView.getText().toString().trim();
            listener.textChanged(number);
        }
    }

    public static void setEditChangedFragment(TextFragmentListener listener) {
        if (mEditDescriptionView != null) {
            String number = mEditDescriptionView.getText().toString().trim();
            listener.textChanged(number);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
