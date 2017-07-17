package com.zero.wolf.greenroad.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.SureCarNumberAdapter;
import com.zero.wolf.greenroad.bean.SerializableNumber;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

import static com.zero.wolf.greenroad.R.id.tv_change;

/**
 * Created by Administrator on 2017/7/17.
 */

public class CarNumberFragment extends Fragment {

    private static CarNumberFragment sFragment;
    private static List<SerializableNumber> sNumberList;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private ImageView mIvClearText_number;
    private SureCarNumberAdapter mNumberAdapter;
    private GridLayoutManager mManager;

    public static CarNumberFragment newInstance(List<SerializableNumber> numberList) {
        if (sFragment == null) {
            sFragment = new CarNumberFragment();
        }
        sNumberList = numberList;
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


        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_number);

        LinearLayout mLayout_top = (LinearLayout) view.findViewById(R.id.layout_top_sure);

        //找到固定的textview
        TextView textView1 = (TextView) mLayout_top.findViewById(R.id.layout_group_sure).findViewById(R.id.tv_no_change);
        textView1.setText(getString(R.string.text_car_number_sure));

        //找到改变的TextView
        mEditText = (EditText) mLayout_top.findViewById(R.id.layout_group_sure).findViewById(tv_change);

        //找到清除text的控件
        mIvClearText_number = (ImageView) mLayout_top.findViewById(R.id.layout_group_sure).findViewById(R.id.iv_clear_Text);
        mIvClearText_number.setOnClickListener((v -> mEditText.setText("")));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNumberAdapter = new SureCarNumberAdapter((AppCompatActivity) getActivity(), sNumberList, new SureCarNumberAdapter.onItemClick() {
            @Override
            public void itemClick(SerializableNumber serializableNumber) {

                mEditText.setText(serializableNumber.getName());
                mEditText.setSelection((serializableNumber.getName().length()));
                serializableNumber.setTop(1);
                serializableNumber.setTime(System.currentTimeMillis());
                refreshView();
            }
        });

        mManager = new GridLayoutManager(getContext(), 4);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);

        mRecyclerView.setAdapter(mNumberAdapter);

    }

    private void refreshView() {
        Collections.sort(sNumberList);
        mNumberAdapter.updateListView(sNumberList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


}
