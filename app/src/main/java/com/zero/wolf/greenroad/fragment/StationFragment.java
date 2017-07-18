package com.zero.wolf.greenroad.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.SureCarStationAdapter;
import com.zero.wolf.greenroad.bean.SerializableStation;
import com.zero.wolf.greenroad.interfacy.TextFragmentListener;
import com.zero.wolf.greenroad.litepalbean.SupportStation;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.PingYinUtil;
import com.zero.wolf.greenroad.tools.ViewUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zero.wolf.greenroad.R.id.tv_change;

/**
 * Created by Administrator on 2017/7/17.
 */

public class StationFragment extends Fragment implements TextWatcher {

    private ArrayList<SerializableStation> mAcacheStations;
    private List<SerializableStation> mStationList = new ArrayList<>();
    private static StationFragment sFragment;

    private RecyclerView mRecyclerView;
    private static EditText mEditText;
    private ImageView mIvClearText_station;
    private SureCarStationAdapter mStationAdapter;
    private LinearLayoutManager mManager;

    public static StationFragment newInstance() {
        if (sFragment == null) {
            sFragment = new StationFragment();
        }
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAcacheStations = (ArrayList<SerializableStation>) ACache
                .get(getActivity()).getAsObject("stations");

        initStationData();
    }

    /**
     * 加载收费站名的数据
     */
    private void initStationData() {
        List<SupportStation> supportStations = DataSupport.findAll(SupportStation.class);

        if (mAcacheStations != null) {
            if (mAcacheStations.size() == supportStations.size()) {
                if (mStationList != null) {
                    mStationList.clear();
                }
                mStationList.addAll(mAcacheStations);

            } else {
                mAcacheStations.clear();
                addStationData(supportStations);
            }
        } else {
            addStationData(supportStations);
        }
    }

    /**
     * 填充station的数据
     *
     * @param supportStations
     */
    private void addStationData(List<SupportStation> supportStations) {
        for (int i = 0; i < supportStations.size(); i++) {

            String stationName = supportStations.get(i).getStationName();

            SerializableStation serializableStation = new SerializableStation();

            serializableStation.setStationName(stationName);

            String sortKey = PingYinUtil.format(stationName);
            serializableStation.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));
            serializableStation.setWholeSpell(PingYinUtil.getInstance().parseSortKeyWholeSpell(sortKey));

            mStationList.add(serializableStation);
        }
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

        mEditText.addTextChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStationAdapter = new SureCarStationAdapter((AppCompatActivity) getActivity(), mStationList, new SureCarStationAdapter.onItemClick() {
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
        Collections.sort(mStationList);
        mStationAdapter.updateListView(mStationList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String stationString = ViewUtils.showAndDismiss_clear_text(mEditText, mIvClearText_station);
        if (stationString.length() > 0) {
            List<SerializableStation> fileterList = PingYinUtil.getInstance()
                    .search_station(mStationList, stationString);
            Logger.i(fileterList.toString());
            mStationAdapter.updateListView(fileterList);
            //mAdapter.updateData(mContacts);
        } else {
            if (mStationAdapter != null) {
                mStationAdapter.updateListView(mStationList);
            }
        }
    }

    public static void setTextChangedFragment(TextFragmentListener listener) {
        String number = mEditText.getText().toString().trim();
        listener.textChanged(number);
    }

    @Override
    public void onStop() {
        super.onStop();
        ACache.get(getActivity()).put("stations", (ArrayList<SerializableStation>) mStationList);
    }
}
