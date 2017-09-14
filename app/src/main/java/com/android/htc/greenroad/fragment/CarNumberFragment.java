package com.android.htc.greenroad.fragment;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.htc.greenroad.R;
import com.android.htc.greenroad.interfacy.TextFragmentListener;
import com.android.htc.greenroad.tools.LicenseKeyboardUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2017/7/17.
 */

public class CarNumberFragment extends Fragment {
    Unbinder unbinder;
    private static CarNumberFragment sFragment;

    @BindView(R.id.keyboard_view)
    KeyboardView mKeyboardView;
    private LicenseKeyboardUtil keyboardUtil;

    private static EditText mEtInputBox1;
    private static EditText mEtInputBox2;
    private static EditText mEtInputBox3;
    private static EditText mEtInputBox4;
    private static EditText mEtInputBox5;
    private static EditText mEtInputBox6;
    private static EditText mEtInputBox7;
    private static String mNumber_I;
    private static EditText[] mEditTextViews;

    public static CarNumberFragment newInstance(String number) {
        if (sFragment == null) {
            sFragment = new CarNumberFragment();
        }
        mNumber_I = number;
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number, container, false);
        ButterKnife.bind(this, view);

        initView(view);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;

        //这些步骤是用来阻止点击editView弹出系统输入法的
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            mEtInputBox1.setInputType(InputType.TYPE_NULL);
            mEtInputBox2.setInputType(InputType.TYPE_NULL);
            mEtInputBox3.setInputType(InputType.TYPE_NULL);
            mEtInputBox4.setInputType(InputType.TYPE_NULL);
            mEtInputBox5.setInputType(InputType.TYPE_NULL);
            mEtInputBox6.setInputType(InputType.TYPE_NULL);
            mEtInputBox7.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName,
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mEtInputBox1, false);
                setShowSoftInputOnFocus.invoke(mEtInputBox2, false);
                setShowSoftInputOnFocus.invoke(mEtInputBox3, false);
                setShowSoftInputOnFocus.invoke(mEtInputBox4, false);
                setShowSoftInputOnFocus.invoke(mEtInputBox5, false);
                setShowSoftInputOnFocus.invoke(mEtInputBox6, false);
                setShowSoftInputOnFocus.invoke(mEtInputBox7, false);
            } catch (NoSuchMethodException e) {
                mEtInputBox1.setInputType(InputType.TYPE_NULL);
                mEtInputBox2.setInputType(InputType.TYPE_NULL);
                mEtInputBox3.setInputType(InputType.TYPE_NULL);
                mEtInputBox4.setInputType(InputType.TYPE_NULL);
                mEtInputBox5.setInputType(InputType.TYPE_NULL);
                mEtInputBox6.setInputType(InputType.TYPE_NULL);
                mEtInputBox7.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        keyboardUtil = new LicenseKeyboardUtil(getContext(), view, mEditTextViews, mNumber_I.length() == 7 ? 7 : 0);
        keyboardUtil.showKeyboard();
        return view;
    }

    private void initView(View view) {
        mEtInputBox1 = (EditText) view.findViewById(R.id.et_input_box_1);
        mEtInputBox2 = (EditText) view.findViewById(R.id.et_input_box_2);
        mEtInputBox3 = (EditText) view.findViewById(R.id.et_input_box_3);
        mEtInputBox4 = (EditText) view.findViewById(R.id.et_input_box_4);
        mEtInputBox5 = (EditText) view.findViewById(R.id.et_input_box_5);
        mEtInputBox6 = (EditText) view.findViewById(R.id.et_input_box_6);
        mEtInputBox7 = (EditText) view.findViewById(R.id.et_input_box_7);

        mEditTextViews = new EditText[]{mEtInputBox1, mEtInputBox2,
                mEtInputBox3, mEtInputBox4, mEtInputBox5, mEtInputBox6, mEtInputBox7};
        inflateNumber(mNumber_I);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static void setTextChangedFragment(TextFragmentListener listener) {
        if (mEtInputBox1 != null && mEtInputBox2 != null && mEtInputBox3 != null &&
                mEtInputBox4 != null && mEtInputBox5 != null && mEtInputBox6 != null && mEtInputBox7 != null) {
            String number = mEtInputBox1.getText().toString().trim() +
                    mEtInputBox2.getText().toString().trim() +
                    mEtInputBox3.getText().toString().trim() +
                    mEtInputBox4.getText().toString().trim() +
                    mEtInputBox5.getText().toString().trim() +
                    mEtInputBox6.getText().toString().trim() +
                    mEtInputBox7.getText().toString().trim();
            listener.textChanged(number);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 当采集界面退出时,初始化numberfragment的数据
     */
    public static void notifyDataChange() {
        if (mEditTextViews != null) {
            for (int i = 0; i < mEditTextViews.length; i++) {
                mEditTextViews[i].setText("");
            }
        }
    }

    /**
     * 当采集界面退出时,初始化numberfragment的数据
     */
    public static void notifyDataChangeFromDraft(String number) {

        if (mEditTextViews != null) {
            inflateNumber(number);
        }
    }
    private static void inflateNumber(String carNumber) {
        if (carNumber.length() == 7) {
            String edit_1_I = carNumber.substring(0, 1);
            String edit_2_I = carNumber.substring(1, 2);
            String edit_3_I = carNumber.substring(2, 3);
            String edit_4_I = carNumber.substring(3, 4);
            String edit_5_I = carNumber.substring(4, 5);
            String edit_6_I = carNumber.substring(5, 6);
            String edit_7_I = carNumber.substring(6, 7);

            String[] edit_texts = {edit_1_I, edit_2_I, edit_3_I, edit_4_I, edit_5_I, edit_6_I, edit_7_I};
            for (int i = 0; i < edit_texts.length; i++) {
                mEditTextViews[i].setText(edit_texts[i]);
            }
        }
    }
}
