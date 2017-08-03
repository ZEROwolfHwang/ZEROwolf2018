package com.zero.wolf.greenroad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.SettingOperatorAdapter;
import com.zero.wolf.greenroad.bean.SettingOperatorInfo;
import com.zero.wolf.greenroad.tools.ActionBarTool;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar_setting)
    Toolbar mToolbarSetting;
    @BindView(R.id.text_setting_road)
    TextView mTextSettingRoad;
    @BindView(R.id.text_setting_road_selection)
    TextView mTextSettingRoadSelection;
    @BindView(R.id.text_setting_station)
    TextView mTextSettingStation;
    @BindView(R.id.text_setting_station_selection)
    TextView mTextSettingStationSelection;
    @BindView(R.id.text_setting_lane_selection)
    TextView mTextSettingLaneSelection;
    @BindView(R.id.setting_recycler_view)
    RecyclerView mSettingRecyclerView;
    @BindView(R.id.text_setting_lane)
    EditText mTextSettingLane;
    @BindView(R.id.text_setting_add_selection)
    TextView mTextSettingAddSelection;

    private SettingActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mActivity = this;
        initToolbar();
        initRecyclerView();
    }

    /**
     * 初始化RecyclerView的布局
     */
    private void initRecyclerView() {
        ArrayList<SettingOperatorInfo> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            SettingOperatorInfo info = new SettingOperatorInfo();
            info.setName("title" + i);
            info.setJob_number("" + 500000 + i);
            list.add(info);
        }
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        SettingOperatorAdapter adapter = new SettingOperatorAdapter(this, list);
        mSettingRecyclerView.setLayoutManager(manager);

        mSettingRecyclerView.setAdapter(adapter);
    }

    private void initToolbar() {

        setSupportActionBar(mToolbarSetting);


        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText(getString(R.string.setting_default));

        mToolbarSetting.setNavigationIcon(R.drawable.back_up_logo);

        mToolbarSetting.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.text_setting_add_selection,
            R.id.text_setting_lane, R.id.text_setting_lane_selection,
            R.id.text_setting_road, R.id.text_setting_station_selection})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.text_setting_add_selection:
                addOperator();
                break;

            default:
                break;
        }

    }

    /**
     * 添加操作员
     */
    private void addOperator() {
        Intent intent = new Intent(this, AddOperatorActivity.class);
        startActivity(intent);

    }
}
