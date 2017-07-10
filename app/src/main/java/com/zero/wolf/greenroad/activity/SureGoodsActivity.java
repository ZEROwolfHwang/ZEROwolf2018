package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.SpinnerPopupWindow;
import com.zero.wolf.greenroad.adapter.SureCarNumberAdapter;
import com.zero.wolf.greenroad.adapter.SureCarStationAdapter;
import com.zero.wolf.greenroad.adapter.SureGoodsAdapter;
import com.zero.wolf.greenroad.bean.SerializableNumber;
import com.zero.wolf.greenroad.bean.SerializableStation;
import com.zero.wolf.greenroad.httpresultbean.HttpResultPostImg;
import com.zero.wolf.greenroad.httpresultbean.SerializableGoods;
import com.zero.wolf.greenroad.https.HttpMethods;
import com.zero.wolf.greenroad.https.HttpUtilsApi;
import com.zero.wolf.greenroad.litepalbean.SupportCarNumber;
import com.zero.wolf.greenroad.litepalbean.SupportGoods;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;
import com.zero.wolf.greenroad.litepalbean.SupportStation;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zero.wolf.greenroad.R.id.tv_change;

public class SureGoodsActivity extends BaseActivity {


    private List<SerializableNumber> mSerializableNumberList = new ArrayList<>();
    private List<SerializableStation> stationList = new ArrayList<>();
    private List<SerializableGoods> goodsList = new ArrayList<>();

    private RecyclerView mRecycler_view_goods;
    private Context mContext;
    private EditText mEt_change3;
    private EditText mEt_change1;
    private AppCompatActivity mActivity;
    private SpinnerPopupWindow mPopupWindow_1;
    private SpinnerPopupWindow mPopupWindow_2;


    private RecyclerView.Adapter mHeadAdapter;
    private EditText mEt_change2;
    private RecyclerView.Adapter mStationAdapter;
    private List<SupportGoods> mSupportGoodsList;
    private SureGoodsAdapter mGoodsAdapter;
    private String mUsername;
    private String mPhotoPath1;
    private String mPhotoPath2;
    private String mPhotoPath3;
    private String mLicense_plate;
    private String mCar_goods;
    private String mCar_station;
    private String mFile1;
    private String mFile2;
    private String mFile3;
    private String mColor;

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

    public static void actionStart(Context context, String color, String username
            , String photoPath1, String photoPath2, String photoPath3) {
        Intent intent = new Intent(context, SureGoodsActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("photoPath1", photoPath1);
        intent.putExtra("photoPath2", photoPath2);
        intent.putExtra("photoPath3", photoPath3);
        intent.putExtra("color", color);

        context.startActivity(intent);
    }


    private void initData() {

        getIntentData();
        /**
         * 加载并缓存车牌号头的数据
         * */

        List<SupportCarNumber> headList = DataSupport.findAll(SupportCarNumber.class);

        ArrayList<SerializableNumber> serializableNumbers = (ArrayList<SerializableNumber>) ACache.get(mActivity).getAsObject("sessions");

        //如果跟数据库长度相同则不作更改，不然则更新
        if (serializableNumbers != null) {
            if (serializableNumbers.size() == headList.size()) {
                mSerializableNumberList.addAll(serializableNumbers);
            } else {
                //更新数据需要删除缓存
                serializableNumbers.clear();
                Logger.i("" + headList.size());
                for (int i = 0; i < headList.size(); i++) {
                    SerializableNumber serializableNumber = new SerializableNumber();
                    Logger.i("" + headList.get(i).getHeadName());
                    serializableNumber.setName(headList.get(i).getHeadName());
                    mSerializableNumberList.add(serializableNumber);
                }
            }
        } else {
            for (int i = 0; i < headList.size(); i++) {
                SerializableNumber serializableNumber = new SerializableNumber();
                Logger.i("" + headList.get(i).getHeadName());
                serializableNumber.setName(headList.get(i).getHeadName());
                mSerializableNumberList.add(serializableNumber);
            }
        }

        /**
         * 加载收费站名的数据
         * */
        List<SupportStation> supportStations = DataSupport.findAll(SupportStation.class);
        ArrayList<SerializableStation> stations = (ArrayList<SerializableStation>) ACache.get(mActivity).getAsObject("stations");

        if (stations != null) {
            if (stations.size() == supportStations.size()) {
                stationList.addAll(stations);
            } else {
                stations.clear();
                addStationData(supportStations);
            }
        } else {
            addStationData(supportStations);
        }


        /**
         * 加载货物的数据及缓存
         * */
        List<SupportGoods> supportGoodses = DataSupport.findAll(SupportGoods.class);
        Logger.i("" + supportGoodses.size());
        ArrayList<SerializableGoods> goods = (ArrayList<SerializableGoods>) ACache.get(mActivity).getAsObject("goods");
        if (goods != null) {
            if (goods.size() == supportGoodses.size()) {
                goodsList.addAll(goods);
            } else {
                goods.clear();
                addGoodsData(supportGoodses);
            }
        } else {
            addGoodsData(supportGoodses);
        }
    }

    /**
     * 得到从上一个activity中拿到的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        mColor = intent.getStringExtra("color");
        mPhotoPath1 = intent.getStringExtra("photoPath1");
        mPhotoPath2 = intent.getStringExtra("photoPath3");
        mPhotoPath3 = intent.getStringExtra("photoPath3");
        Logger.i(mUsername);
        Logger.i(mPhotoPath1);
        Logger.i(mPhotoPath2);
        Logger.i(mPhotoPath3);
    }

    private void addGoodsData(List<SupportGoods> supportGoodses) {
        for (int i = 0; i < supportGoodses.size(); i++) {
            SerializableGoods serializableGoods = new SerializableGoods();
            serializableGoods.setScientific_name(supportGoodses.get(i).getScientificname());
            serializableGoods.setAlias(supportGoodses.get(i).getAlias());
            //// TODO: 2017/7/6 填充图片

            goodsList.add(serializableGoods);
        }
    }

    /**
     * 填充station的数据
     *
     * @param supportStations
     */
    private void addStationData(List<SupportStation> supportStations) {
        for (int i = 0; i < supportStations.size(); i++) {
            SerializableStation serializableStation = new SerializableStation();
            serializableStation.setStationName(supportStations.get(i).getStationName());
            stationList.add(serializableStation);
        }
    }


    /**
     * 加载货物的布局以及填充数据
     */
    private void initRecycler() {
        mSupportGoodsList = DataSupport.findAll(SupportGoods.class);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecycler_view_goods.setLayoutManager(manager);

        mGoodsAdapter = new SureGoodsAdapter(mContext, goodsList, new SureGoodsAdapter.onItemClick() {
            @Override
            public void itemClick(SerializableGoods serializableGoods, int position) {
                updataRecycler(serializableGoods, position);
            }
        });
        mRecycler_view_goods.setAdapter(mGoodsAdapter);

    }

    private void updataRecycler(SerializableGoods serializableGoods, int position) {
        serializableGoods.setTop(1);
        serializableGoods.setTime(System.currentTimeMillis());
        mEt_change3.setText(goodsList.get(position).getScientific_name());
        mEt_change3.setSelection(goodsList.get(position).getScientific_name().length());

        Collections.sort(goodsList);
        mGoodsAdapter.notifyDataSetChanged();

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


        initEditText();

        /**
         *车牌号的点击事件
         * */
        mEt_change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPopupWindow_1 = new SpinnerPopupWindow.Builder(SureGoodsActivity.this)
                        .setmLayoutManager(null, 1)
                        .setmAdapter(new SureCarNumberAdapter(mActivity, mSerializableNumberList, new SureCarNumberAdapter.onItemClick() {
                            @Override
                            public void itemClick(SerializableNumber serializableNumber, int position) {
                                updatePupop(position, 001);
                            }

                            @Override
                            public void onTop(SerializableNumber serializableNumber) {
                                serializableNumber.setTop(1);
                                serializableNumber.setTime(System.currentTimeMillis());
                                refreshView(001);
                            }

                            @Override
                            public void onCancel(SerializableNumber serializableNumber) {
                            }
                        }))
                        .setmHeight(700).setmWidth(500)
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
                            public void itemClick(SerializableStation station, int position) {
                                updatePupop(position, 002);
                                station.setIsTop(1);
                                station.setTime(System.currentTimeMillis());
                                refreshView(002);
                            }
                        }))
                        .setmHeight(500).setmWidth(500)
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
        initRecycler();

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
                        //saveLocalLite();
                        String currentTime = TimeUtil.getCurrentTimeTos();
                        postAccept(currentTime);
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


    /**
     * 保存到本地数据库
     * @param currentTime
     */
    private void saveLocalLite(String currentTime) {
        SupportPhotoLite supportPhotoLite = new SupportPhotoLite();
        supportPhotoLite.setShuttime(currentTime);
        supportPhotoLite.setUuid(UUID.randomUUID());
        supportPhotoLite.setUsername(mUsername);
        supportPhotoLite.setGoods(mCar_goods);
        supportPhotoLite.setLicense_plate(mLicense_plate);
        supportPhotoLite.setStation(mCar_station);
        supportPhotoLite.setPhotoPath1(mFile1);
        supportPhotoLite.setPhotoPath2(mFile2);
        supportPhotoLite.setPhotoPath3(mFile3);
        supportPhotoLite.setLicense_color(mColor);
        supportPhotoLite.save();
    }


    private void initEditText() {
        if (mSerializableNumberList.size() == 0) {
            mEt_change1.setText("粤B");
        } else {
            mEt_change1.setText(mSerializableNumberList.get(0).getName());
        }
        if (stationList.size() == 0) {
            mEt_change2.setText("泰安东收费站");
        } else {
            mEt_change2.setText(stationList.get(0).getStationName());
        }
        if (goodsList.size() == 0) {
            mEt_change3.setText("西兰花");
        } else {
            mEt_change3.setText(goodsList.get(0).getScientific_name());
        }
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
            Collections.sort(mSerializableNumberList);
            mHeadAdapter.notifyDataSetChanged();
        }
        if (stype == 002) {
            Collections.sort(stationList);
            mStationAdapter.notifyDataSetChanged();
        }

    }

    private void updatePupop(int position, int type) {
        if (type == 001) {
            mEt_change1.setText(mSerializableNumberList.get(position).getName());
            mEt_change1.setSelection((mSerializableNumberList.get(position).getName()).length());
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
        mLicense_plate = mEt_change1.getText().toString();
        mCar_station = mEt_change2.getText().toString();
        mCar_goods = mEt_change3.getText().toString();

        String dialog_message = "车  牌  号：" + mLicense_plate + "\n"
                + "货物名称：" + mCar_goods + "\n"
                + "点击“确认”将提交信息" + "\n"
                + "点击“取消”可再次修改";
        return dialog_message;
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


    /**
     * 向服务器Post所有的信息
     * @param currentTime
     */
    private void postAccept(String currentTime) {

        //SimplePhoto();
        List<String> pathList = getPathList();
        Logger.i("" + getPathList().size());
        Logger.i("" + pathList.size());

        MultipartBody.Builder builder = new MultipartBody.Builder();

//        Map<String, RequestBody> bodyMap = new HashMap<>();

        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);//image/png
            builder.addFormDataPart("image" + i, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
           /* File file = new File(pathList.get(i));
            bodyMap.put("image"+i+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"),file));*/
        }
        List<MultipartBody.Part> parts = builder.build().parts();

        HttpUtilsApi httpUtilsApi = HttpMethods.getInstance().getApi();

        Logger.i(mUsername);
        Logger.i(mCar_station);
        Logger.i(mLicense_plate);
        Logger.i(mCar_goods);

        Observable<HttpResultPostImg> postThreeImg = httpUtilsApi.postThreeImg(currentTime,mUsername, mCar_station, mLicense_plate, mCar_goods, parts);
        postThreeImg.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultPostImg>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("三张照片上传成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultPostImg httpResultPostImg) {
                        int code = httpResultPostImg.getCode();
                        String msg = httpResultPostImg.getMsg();
                        if (code == 200) {
                            CarNumberCut();
                            Intent intent = new Intent(SureGoodsActivity.this, PhotoActivity.class);
                            intent.putExtra("username", mUsername);
                            startActivity(intent);
                        } else if (code == 300) {
                            ToastUtils.singleToast("上传失败");
                            saveLocalLite(currentTime);
                            // TODO: 2017/7/7 上传失败需要保存到数据库
                        }
                        Logger.i("" + code);
                        Logger.i("" + msg);
                    }
                });
    }

    /**
     * 获取多张待上传图片的地址列表
     *
     * @return
     */
    private List<String> getPathList() {


        /*ArrayList<String> list = new ArrayList<>();
        list.add(mPhotoPath1);
        list.add(mPhotoPath2);
        list.add(mPhotoPath3);
        */
        mFile1 = "/mnt/sdcard/Download/car_body_light.png";
        mFile2 = "/mnt/sdcard/Download/car_goods_light.png";
        mFile3 = "/mnt/sdcard/Download/car_station_light.png";

        ArrayList<String> list = new ArrayList<>();
        list.add(mFile1);
        list.add(mFile2);
        list.add(mFile3);

        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //存入缓存
        ACache.get(mActivity).put("sessions", (ArrayList<SerializableNumber>) mSerializableNumberList);
        ACache.get(mActivity).put("stations", (ArrayList<SerializableStation>) stationList);
        ACache.get(mActivity).put("goods", (ArrayList<SerializableGoods>) goodsList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

