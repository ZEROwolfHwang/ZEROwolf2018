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

import com.zero.wolf.greenroad.BaseActivity;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.SpinnerPopupWindow;
import com.zero.wolf.greenroad.adapter.SureCarNumberAdapter;
import com.zero.wolf.greenroad.adapter.SureGoodsAdapter;
import com.zero.wolf.greenroad.bean.CarStation;
import com.zero.wolf.greenroad.litepalbean.CarNumberHead;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zero.wolf.greenroad.R.id.tv_change;

public class SureGoodsActivity extends BaseActivity {

    String[] car_local = {"津A", "津B", "津C", "津D", "豫A", "豫B", "豫C", "豫D", "豫E", "粤A", "粤B"
            , "粤C", "粤D", "皖A", "皖B", "皖C", "皖D"};
    String[] station_names = {"泰安东收费站", "公明收费站", "松岗收费站", "南山收费站", "宝安收费站", "长安收费站", "东莞收费站", "深圳收费站"};


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


    private RecyclerView.Adapter mAdapter;
    private EditText mEt_change2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_goods);
        mContext = this;
        mActivity = this;
        initData();
        initView();
        initRecycler();

    }

    private void initData() {
        /**
         * 加载并缓存车牌号头的数据
         * */
        ArrayList<Session> sessions = (ArrayList<Session>) ACache.get(mActivity).getAsObject("sessions");
        if (sessions!=null)
            sessionList.addAll(sessions);
        else {
            for (int i = 0; i < car_local.length; i++) {
                Session session = new Session();
                session.setName(car_local[i]);
                sessionList.add(session);
            }
        }
        /**
         * 加载收费站名的数据
         * */
        ArrayList<CarStation> stations = (ArrayList<CarStation>) ACache.get(mActivity).getAsObject("stations");
        if (stations!=null)
            stationList.addAll(stations);
        else {
            for (int i = 0; i < station_names.length; i++) {
                CarStation carStation = new CarStation();
                carStation.setStationName(station_names[i]);
                stationList.add(carStation);
            }
        }

       // sessionList.remove(sessionList.size()-1);
       // sessionList.add(new Session(sessionList.size()));
        ////////////////////////////////////////////////////////

     /*   mList_local = new ArrayList<>();
        for (int i = 0; i < car_local.length; i++) {
            mList_local.add(car_local[i]);
        }*/

        initLitePal(car_local);

       // TypedArray typedArray = this.getResources().obtainTypedArray(R.array.icon_array);
       /* sessionList = new ArrayList<>();
        for (int i = 0; i < car_local.length; i++) {
            Session session = new Session();
            session.setName(car_local[i]);
            sessionList.add(session);
        }

        for (int i = 0; i < sessionList.size(); i++) {
            Logger.i(sessionList.get(i).getName());
        }*/
       /* mRecycler_list = new ArrayList<>();
        for (int i = 0; i < mHeadList.size(); i++) {
           mRecycler_list.add(mHeadList.get(i).getHeadName());
        }*/

    }

    /**
     * 填充数据库
     * @param car_local
     */
    private void initLitePal(String[] car_local) {
        for (int i = 0; i < car_local.length; i++) {
            CarNumberHead head = new CarNumberHead();
            head.setHeadName(car_local[i]);
            head.save();
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
        mEt_change2.setText("泰安东收费站");
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
                        .setmLayoutManager(null,1)
                        .setmAdapter(new SureCarNumberAdapter(mActivity, sessionList, new SureCarNumberAdapter.onItemClick() {
                            @Override
                            public void itemClick(Session session, int position) {
                                updatePupop(position);
                            }

                            @Override
                            public void onTop(Session session) {
                                session.setTop(1);
                                session.setTime(System.currentTimeMillis());
                                refreshView();
                            }

                            @Override
                            public void onCancel(Session session) {
                            }
                        }))
                        .setmHeight(700).setmWidth(800)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();


                mAdapter = SpinnerPopupWindow.Builder.getmAdapter();

                mPopupWindow_1.showPopWindowCenter(v);
            }
        });

        /**
         * 收费站名称点击事件
         */
         mEt_change2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mPopupWindow_2 = new SpinnerPopupWindow.Builder(SureGoodsActivity.this)
                         .setmLayoutManager(null,0)
                         .setmAdapter(new SureCarNumberAdapter(mActivity, sessionList, new SureCarNumberAdapter.onItemClick() {
                             @Override
                             public void itemClick(Session session, int position) {
                                 updatePupop(position);
                             }

                             @Override
                             public void onTop(Session session) {
                                 session.setTop(1);
                                 session.setTime(System.currentTimeMillis());
                                 refreshView();
                             }

                             @Override
                             public void onCancel(Session session) {
                             }
                         }))
                         .setmHeight(700).setmWidth(800)
                         .setOutsideTouchable(true)
                         .setFocusable(true)
                         .build();


                 mAdapter = SpinnerPopupWindow.Builder.getmAdapter();

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
               /* dialog.setItems(new String[]{"123", "456", "789"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });*/
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
            }
        });
    }

    private void refreshView() {
        Collections.sort(sessionList);
        mAdapter.notifyDataSetChanged();

    }

    private void updatePupop(int position) {
        mEt_change1.setText(sessionList.get(position).getName());
        mEt_change1.setSelection((sessionList.get(position).getName()).length());

        //指定操作


        mPopupWindow_1.dismissPopWindow();

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
        ACache.get(mActivity).put("sessions",(ArrayList<Session>) sessionList);
        ACache.get(mActivity).put("stations",(ArrayList<CarStation>) stationList);

    }
}

