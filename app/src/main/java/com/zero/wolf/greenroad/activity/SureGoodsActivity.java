package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.BaseActivity;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.SpinnerPopupWindow;
import com.zero.wolf.greenroad.adapter.SureCarNumberAdapter;
import com.zero.wolf.greenroad.adapter.SureCarStationAdapter;
import com.zero.wolf.greenroad.adapter.SureGoodsAdapter;
import com.zero.wolf.greenroad.bean.CarStation;
import com.zero.wolf.greenroad.litepalbean.CarNumberHead;
import com.zero.wolf.greenroad.litepalbean.StationInfo;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.Session;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zero.wolf.greenroad.R.id.tv_change;

public class SureGoodsActivity extends BaseActivity {


    private List<Session> sessionList = new ArrayList<>();
    private List<CarStation> stationList = new ArrayList<>();

    private RecyclerView mRecycler_view_goods;
    private ArrayList<String> mList;
    private Context mContext;
    private EditText mEt_change3;
    private EditText mEt_change1;
    private AppCompatActivity mActivity;
    private ArrayList<String> mList_local;
    private SpinnerPopupWindow mPopupWindow_1;
    private SpinnerPopupWindow mPopupWindow_2;


    private RecyclerView.Adapter mHeadAdapter;
    private EditText mEt_change2;
    private RecyclerView.Adapter mStationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_goods);
        mContext = this;
        mActivity = this;
        initData();
        initView();
        initRecycler();
        initSend();
    }

    private void initSend() {

    }

    private void initData() {
        /**
         * 加载并缓存车牌号头的数据
         * */

        List<CarNumberHead> headList = DataSupport.findAll(CarNumberHead.class);

        ArrayList<Session> sessions = (ArrayList<Session>) ACache.get(mActivity).getAsObject("sessions");
        Logger.i(sessions.get(5).getName());


        //如果跟数据库长度相同则不作更改，不然则更新
        if (sessions.size() == headList.size())
            sessionList.addAll(sessions);
            //更新数据需要删除缓存
        else {
            sessions.clear();
            Logger.i("" + headList.size());
            for (int i = 0; i < headList.size(); i++) {
                Session session = new Session();
                Logger.i("" + headList.get(i).getHeadName());
                session.setName(headList.get(i).getHeadName());
                sessionList.add(session);
            }
        }

        List<StationInfo> stationInfos = DataSupport.findAll(StationInfo.class);

        /**
         * 加载收费站名的数据
         * */
        ArrayList<CarStation> stations = (ArrayList<CarStation>) ACache.get(mActivity).getAsObject("stations");

        if (stations != null) {
            if (stations.size() == stationInfos.size()) {
                stationList.addAll(stations);
            } else {
                stations.clear();
                addStationData(stationInfos);
            }
        } else {
            addStationData(stationInfos);

        }
    }

    /**
     * 填充station的数据
     * @param stationInfos
     */
    private void addStationData(List<StationInfo> stationInfos) {
        for (int i = 0; i < stationInfos.size(); i++) {
            CarStation carStation = new CarStation();
            carStation.setStationName(stationInfos.get(i).getStationName());
            stationList.add(carStation);
        }
    }


    private void initRecycler() {

    }

    private void initView() {

        initToolbar();

        LinearLayout mLayout_top = (LinearLayout) findViewById(R.id.layout_top_sure);
        LinearLayout mLayout_center = (LinearLayout) findViewById(R.id.layout_center_sure);
        LinearLayout mLayout_bottom = (LinearLayout) findViewById(R.id.layout_bottom_sure);

        //找到固定的textview
        TextView textView1 = (TextView) mLayout_top.findViewById(R.id.layout_group_sure).findViewById(R.id.tv_no_change);
        textView1.setText(getString(R.string.text_car_number_sure));
        TextView textView2 = (TextView) mLayout_center.findViewById(R.id.layout_group_sure).findViewById(R.id.tv_no_change);
        textView2.setText(getString(R.string.text_station_name_sure));
        TextView textView3 = (TextView) mLayout_bottom.findViewById(R.id.layout_group_sure).findViewById(R.id.tv_no_change);
        textView3.setText(getString(R.string.text_car_goods_sure));

        //找到改变的TextView
        mEt_change1 = (EditText) mLayout_top.findViewById(R.id.layout_group_sure).findViewById(tv_change);
        mEt_change2 = (EditText) mLayout_center.findViewById(R.id.layout_group_sure).findViewById(tv_change);
        mEt_change3 = (EditText) mLayout_bottom.findViewById(R.id.layout_group_sure).findViewById(tv_change);


        //mEt_change1.setText("鲁A888888");

        mEt_change1.setText(sessionList.get(0).getName());
        mEt_change2.setText(stationList.get(0).getStationName());
        mEt_change3.setHint("西兰花");

//        mEt_change1.setFocusable(false);
//        et_change2.setFocusable(false);
//        mEt_change3.setFocusable(true);

        /**
         *车牌号的点击事件
         * */
        mEt_change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPopupWindow_1 = new SpinnerPopupWindow.Builder(SureGoodsActivity.this)
                        .setmLayoutManager(null, 1)
                        .setmAdapter(new SureCarNumberAdapter(mActivity, sessionList, new SureCarNumberAdapter.onItemClick() {
                            @Override
                            public void itemClick(Session session, int position) {
                                updatePupop(position, 001);
                            }

                            @Override
                            public void onTop(Session session) {
                                session.setTop(1);
                                session.setTime(System.currentTimeMillis());
                                refreshView(001);
                            }

                            @Override
                            public void onCancel(Session session) {
                            }
                        }))
                        .setmHeight(700).setmWidth(800)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();


                mHeadAdapter = SpinnerPopupWindow.Builder.getmAdapter();

                mPopupWindow_1.showPopWindowCenter(v);
            }
        });

        mEt_change2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow_2 = new SpinnerPopupWindow.Builder(SureGoodsActivity.this)
                        .setmLayoutManager(null, 0)
                        .setmAdapter(new SureCarStationAdapter(mActivity, stationList, new SureCarStationAdapter.onItemClick() {
                            @Override
                            public void itemClick(CarStation station, int position) {
                                updatePupop(position, 002);
                                station.setIsTop(1);
                                station.setTime(System.currentTimeMillis());
                                refreshView(002);
                            }
                        }))
                        .setmHeight(700).setmWidth(800)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();

                mStationAdapter = SpinnerPopupWindow.Builder.getmAdapter();
                mPopupWindow_2.showPopWindowCenter(v);
            }

        });

        /**
         * 货物的点击事件
         */
        mRecycler_view_goods = (RecyclerView) findViewById(R.id.recycler_view_goods);
        // mRecycler_view_goods.setVisibility(View.INVISIBLE);
        initRecyclerData();
        initRecyclerView();

        mEt_change3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecycler_view_goods.setVisibility(View.VISIBLE);

            }
        });

        Button bt_ok_msg = (Button) findViewById(R.id.bt_ok_msg);
        bt_ok_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SureGoodsActivity.this);
                dialog.setTitle(getString(R.string.dialog_title_sure));
                dialog.setMessage(getDialogSendMessage());
                dialog.setPositiveButton(getString(R.string.dialog_messge_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SureGoodsActivity.this, "querenfasong", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton(getString(R.string.dialog_message_Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SureGoodsActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

                CarNumberCut();
            }
        });
    }

    /**
     * 将未上传车辆的数字减去一
     */
    private void CarNumberCut() {
        SPUtils.cut_one(mActivity, SPUtils.CAR_NOT_COUNT);
        int cra_not_count = (int) SPUtils.get(mActivity, SPUtils.CAR_NOT_COUNT, 0);
        Logger.i("cra_not_count------------" + cra_not_count);
    }

    private void refreshView(int stype) {
        if (stype == 001) {
            Collections.sort(sessionList);
            mHeadAdapter.notifyDataSetChanged();
        }
        if (stype == 002) {
            Collections.sort(stationList);
            mStationAdapter.notifyDataSetChanged();
        }

    }

    private void updatePupop(int position, int type) {
        if (type == 001) {
            mEt_change1.setText(sessionList.get(position).getName());
            mEt_change1.setSelection((sessionList.get(position).getName()).length());
            mPopupWindow_1.dismissPopWindow();
        } else if (type == 002) {
            mEt_change2.setText(stationList.get(position).getStationName());
            mEt_change2.setSelection((stationList.get(position).getStationName()).length());
            mPopupWindow_2.dismissPopWindow();
        }

        //指定操作


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sure);
        setSupportActionBar(toolbar);

        TextView title_text_view = ActionBarTool.getInstance(mActivity).getTitle_text_view();
        title_text_view.setText(getString(R.string.sure_goods_type));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示返回上级的箭头
        //getSupportActionBar().setDisplayShowTitleEnabled(false);//将actionbar原有的标题去掉（这句一般是用在xml方法一实现）
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getDialogSendMessage() {
        String car_number = mEt_change1.getText().toString();
        String car_goods = mEt_change3.getText().toString();

        String dialog_message = "车  牌  号：" + car_number + "\n"
                + "货物名称：" + car_goods + "\n"
                + "点击“确认”将提交信息" + "\n"
                + "点击“取消”可再次修改";
        return dialog_message;
    }

    /**
     * 初始化RecyclerView的数据
     */
    private void initRecyclerData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 20) {
                mList.add("title" + i);
            }
            if (i < 40) {
                mList.add("absd" + i);
            }
            if (i < 60) {
                mList.add("1287239" + i);
            }

        }
    }

    /**
     * 初始化RecyclerView的显示
     */
    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecycler_view_goods.setLayoutManager(manager);

        SureGoodsAdapter adapter = new SureGoodsAdapter(mContext, mList, mEt_change3);
        mRecycler_view_goods.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_bar_cancer) {
            Toast.makeText(SureGoodsActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //存入缓存
        ACache.get(mActivity).put("sessions", (ArrayList<Session>) sessionList);
        ACache.get(mActivity).put("stations", (ArrayList<CarStation>) stationList);

    }
}

