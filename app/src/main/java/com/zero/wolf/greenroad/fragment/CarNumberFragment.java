package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.content.IntentFilter;
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

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.interfacy.OnFragmentAttachListener;
import com.zero.wolf.greenroad.interfacy.TextFragmentListener;
import com.zero.wolf.greenroad.tools.LicenseKeyboardUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2017/7/17.
 */

public class CarNumberFragment extends Fragment {

    public static final String INPUT_LICENSE_COMPLETE = "me.kevingo.licensekeyboard.input.comp";
    public static final String INPUT_LICENSE_KEY = "LICENSE";

    Unbinder unbinder;


    private static CarNumberFragment sFragment;


    @BindView(R.id.keyboard_view)
    KeyboardView mKeyboardView;
    private LicenseKeyboardUtil keyboardUtil;

    private OnFragmentAttachListener mListener;
    private static EditText mEtInputBox1;
    private static EditText mEtInputBox2;
    private static EditText mEtInputBox3;
    private static EditText mEtInputBox4;
    private static EditText mEtInputBox5;
    private static EditText mEtInputBox6;
    private static EditText mEtInputBox7;
    private static SerializableMain2Sure mMain2Sure;
    private static String mNumber_I;
    private EditText[] mEditTextViews;

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

        Logger.i(mNumber_I);

        initView(view);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;
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

        //输入车牌完成后的intent过滤器
        IntentFilter finishFilter = new IntentFilter(INPUT_LICENSE_COMPLETE);

     /*   final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String license = intent.getStringExtra(INPUT_LICENSE_KEY);
                if (license != null && license.length() > 0) {
                    if (keyboardUtil != null) {
                        keyboardUtil.hideKeyboard();
                    }

                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("车牌号为:" + license);
                    alertDialog = builder.create();
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                }
                getActivity().unregisterReceiver(this);
            }
        };
        getActivity().registerReceiver(receiver, finishFilter);*/

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
        if (mNumber_I.length() == 7) {

            String edit_1_I = mNumber_I.substring(0, 1);
            String edit_2_I = mNumber_I.substring(1, 2);
            String edit_3_I = mNumber_I.substring(2, 3);
            String edit_4_I = mNumber_I.substring(3, 4);
            String edit_5_I = mNumber_I.substring(4, 5);
            String edit_6_I = mNumber_I.substring(5, 6);
            String edit_7_I = mNumber_I.substring(6, 7);

            String[] edit_texts = {edit_1_I, edit_2_I, edit_3_I, edit_4_I, edit_5_I, edit_6_I, edit_7_I};
            for (int i = 0; i < edit_texts.length; i++) {
                mEditTextViews[i].setText(edit_texts[i]);
            }
        }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentAttachListener) {
            mListener = (OnFragmentAttachListener) context;
        }/* else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }


}
