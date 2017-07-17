package com.zero.wolf.greenroad.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.zero.wolf.greenroad.adapter.SureCarStationAdapter;
import com.zero.wolf.greenroad.bean.SerializableStation;

import java.util.Collections;
import java.util.List;

import static com.zero.wolf.greenroad.R.id.tv_change;

/**
 * Created by Administrator on 2017/7/17.
 */

public class StationFragment extends Fragment {

    private static StationFragment sFragment;

    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private ImageView mIvClearText_station;
    private static List<SerializableStation> sStationList;
    private SureCarStationAdapter mStationAdapter;
    private LinearLayoutManager mManager;

    public static StationFragment newInstance(List<SerializableStation> stationList) {
        if (sFragment == null) {
            sFragment = new StationFragment();
        }
        sStationList = stationList;
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);

        initView(view);


        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_station);

        LinearLayout mLayout_center = (LinearLayout) view.findViewById(R.id.layout_center_sure);

        //找到固定的textview
        TextView textView1 = (TextView) mLayout_center.findViewById(R.id.layout_group_sure).findViewById(R.id.tv_no_change);
        textView1.setText(getString(R.string.text_station_name_sure));

        //找到改变的TextView
        mEditText = (EditText) mLayout_center.findViewById(R.id.layout_group_sure).findViewById(tv_change);

        //找到清除text的控件
        mIvClearText_station = (ImageView) mLayout_center.findViewById(R.id.layout_group_sure).findViewById(R.id.iv_clear_Text);
        mIvClearText_station.setOnClickListener((v -> mEditText.setText("")));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStationAdapter = new SureCarStationAdapter((AppCompatActivity) getActivity(), sStationList, new SureCarStationAdapter.onItemClick() {
            @Override
            public void itemClick(SerializableStation station) {
                mEditText.setText(station.getStationName());
                mEditText.setSelection((station.getStationName().length()));
                station.setIsTop(1);
                station.setTime(System.currentTimeMillis());
                refreshView();
            }
        });

        mManager = new LinearLayoutManager(getContext());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);

        mRecyclerView.setAdapter(mStationAdapter);

    }

    private void refreshView() {
        Collections.sort(sStationList);
        mStationAdapter.updateListView(sStationList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


}
