package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.zero.wolf.greenroad.https.HttpMethods;
import com.zero.wolf.greenroad.litepalbean.SupportCarNumber;
import com.zero.wolf.greenroad.litepalbean.SupportGoods;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;
import com.zero.wolf.greenroad.litepalbean.SupportStation;
import com.zero.wolf.greenroad.manager.CarNumberCount;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.smartsearch.PinyinComparator;
import com.zero.wolf.greenroad.smartsearch.SortModel;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.PathUtil;
import com.zero.wolf.greenroad.tools.PingYinUtil;
import com.zero.wolf.greenroad.tools.RxHolder;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.Subscriber;

import static com.zero.wolf.greenroad.R.id.tv_change;

public class SureGoodsActivity extends BaseActivity {


    private List<SerializableNumber> mNumberList = new ArrayList<>();
    private List<SerializableStation> mStationList = new ArrayList<>();
    private List<SortModel> mGoodsList = new ArrayList<>();

    private RecyclerView mRecycler_view_goods;
    private Context mContext;
    private EditText mEt_change3;
    private EditText mEt_change1;
    private AppCompatActivity mActivity;
    private SpinnerPopupWindow mPopupWindow_1;
    private SpinnerPopupWindow mPopupWindow_2;


    private EditText mEt_change2;
    private List<SupportGoods> mSupportGoodsList;

    private String mUsername;
    private String mPhotoPath1;
    private String mPhotoPath2;
    private String mPhotoPath3;
    private String mLicense_plate;
    private String mCar_goods;
    private String mCar_station;
    private String mColor;
    private List<SupportGoods> mSupportGoodses;

    private SureGoodsAdapter mGoodsAdapter;


    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;


    private ArrayList<SerializableNumber> mAcacheNumbers;
    private ArrayList<SerializableStation> mAcacheStations;
    private ArrayList<SortModel> mAcacheGoods;
    private SureCarStationAdapter mStationAdapter;
    private SureCarNumberAdapter mNumberAdapter;
    private ArrayList<SerializableNumber> mSerializableNumberArrayList;
    private ImageView mIvClearText_station;
    private ImageView mIvClearText_number;
    private ImageView mIvClearText_goods;

    private int pop_1_currentState;
    private Button mBt_ok_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure_goods);
        mContext = this;
        mActivity = this;
        pop_1_currentState = 222;


        initData();
        initView();
        //initRecycler();
        initListener();

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

        mAcacheNumbers = (ArrayList<SerializableNumber>) ACache
                .get(mActivity).getAsObject("sessions");

        mAcacheStations = (ArrayList<SerializableStation>) ACache
                .get(mActivity).getAsObject("stations");

        mAcacheGoods = (ArrayList<SortModel>) ACache
                .get(mActivity).getAsObject("goods");

        initGoodsData();
        iniNumberData();
        initStationData();

        initAdapter();

    }

    private void initAdapter() {
        mNumberAdapter = new SureCarNumberAdapter(mActivity, mNumberList, new SureCarNumberAdapter.onItemClick() {
            @Override
            public void itemClick(SerializableNumber serializableNumber) {

                if (mPopupWindow_1.isShowing()) {
                    mPopupWindow_1.dismissPopWindow();
                }

                mEt_change1.setText(serializableNumber.getName());
                mEt_change1.setSelection((serializableNumber.getName().length()));
                serializableNumber.setTop(1);
                serializableNumber.setTime(System.currentTimeMillis());
                refreshView(001);
            }
        });

        mStationAdapter = new SureCarStationAdapter(mActivity, mStationList, new SureCarStationAdapter.onItemClick() {
            @Override
            public void itemClick(SerializableStation station) {
                mEt_change2.setText(station.getStationName());
                mEt_change2.setSelection(station.getStationName().length());
                if (mPopupWindow_2.isShowing()) {
                    mPopupWindow_2.dismissPopWindow();
                }
                station.setIsTop(1);
                station.setTime(System.currentTimeMillis());
                refreshView(002);
            }
        });


    }

    /**
     * 加载并缓存车牌号头的数据
     */
    private void iniNumberData() {
        List<SupportCarNumber> headList = DataSupport.findAll(SupportCarNumber.class);

        //如果跟数据库长度相同则不作更改，不然则更新
        if (mAcacheNumbers != null) {
            if (mAcacheNumbers.size() == headList.size()) {
                mNumberList.addAll(mAcacheNumbers);
            } else {
                //更新数据需要删除缓存
                mAcacheNumbers.clear();
                Logger.i("" + headList.size());
                addNumberData(headList);
            }
        } else {
            addNumberData(headList);
        }
    }

    /**
     * 加载收费站名的数据
     */
    private void initStationData() {
        List<SupportStation> supportStations = DataSupport.findAll(SupportStation.class);

        if (mAcacheStations != null) {
            if (mAcacheStations.size() == supportStations.size()) {
                mStationList.addAll(mAcacheStations);
            } else {
                mAcacheStations.clear();
                addStationData(supportStations);
            }
        } else {
            addStationData(supportStations);
        }
    }

    private void addNumberData(List<SupportCarNumber> headList) {
        for (int i = 0; i < headList.size(); i++) {
            String headName = headList.get(i).getHeadName();
            SerializableNumber serializableNumber = new SerializableNumber();

            serializableNumber.setName(headName);
            String sortKey = PingYinUtil.format(headName);
            serializableNumber.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));
            serializableNumber.setWholeSpell(PingYinUtil.getInstance().parseSortKeyWholeSpell(sortKey));

            mNumberList.add(serializableNumber);
        }
    }

    /**
     * 加载货物的数据及缓存
     */
    private void initGoodsData() {
        List<SupportGoods> supportGoodses = DataSupport.findAll(SupportGoods.class);

        if (mAcacheGoods != null) {
            if (mAcacheGoods.size() == supportGoodses.size()) {
                mGoodsList.addAll(mAcacheGoods);
            } else {
                mAcacheGoods.clear();
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
    }

    private void addGoodsData(List<SupportGoods> supportGoodses) {
        for (int i = 0; i < supportGoodses.size(); i++) {

            String scientificname = supportGoodses.get(i).getScientificname();
            String alias = supportGoodses.get(i).getAlias();
            String imgurl = supportGoodses.get(i).getImgurl();

            SortModel sortModel = new SortModel();
            sortModel.setScientificname(scientificname);
            sortModel.setAlias(alias);

            Bitmap bitmap = BitmapFactory.decodeFile(imgurl);

            sortModel.setBitmap(bitmap);

            String sortLetters = PingYinUtil.getInstance().getSortLetterBySortKey(scientificname);
            if (sortLetters == null) {
                sortLetters = PingYinUtil.getInstance().getSortLetter(alias);
            }
            sortModel.setSortLetters(sortLetters);

            String sortKey = PingYinUtil.format(scientificname + alias);
            sortModel.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));
            sortModel.setWholeSpell(PingYinUtil.getInstance().parseSortKeyWholeSpell(sortKey));


            mGoodsList.add(sortModel);
        }
            Logger.i(mGoodsList.get(0).toString());
            Logger.i(mGoodsList.get(0).getBitmap().toString());
            Logger.i(mGoodsList.get(1).getBitmap().toString());
            Logger.i(mGoodsList.get(2).getBitmap().toString());
        Logger.i("" + mGoodsList.size());
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

    /**
     * 加载货物的布局以及填充数据
     */
    private void initRecycler() {
        // mListView = (ListView) findViewById(R.id.lv_view_goods);
        mRecycler_view_goods = (RecyclerView) findViewById(R.id.recycler_goods_sure);

        pinyinComparator = new PinyinComparator();

        Collections.sort(mGoodsList, pinyinComparator);// 根据a-z进行排序源数据

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler_view_goods.setLayoutManager(manager);

        mGoodsAdapter = new SureGoodsAdapter(this, mGoodsList, new SureGoodsAdapter.onItemClick() {
            @Override
            public void itemClick(SortModel sortModel, int position) {
                if ((mPopupWindow_1 == null || !mPopupWindow_1.isShowing())
                        && (mPopupWindow_2 == null || !mPopupWindow_2.isShowing())
                        &&(!mEt_change1.hasFocus())
                        &&(!mEt_change2.hasFocus())) {
                    String scientificname = sortModel.getScientificname();
                    mEt_change3.setText(scientificname);
                    mGoodsAdapter.updateListView(mGoodsList);
                    mEt_change3.setSelection(scientificname.length());
                } else {
                    return;
                }
            }
        });
        // mListView.setAdapter(mGoodsAdapter);
        mRecycler_view_goods.setAdapter(mGoodsAdapter);
    }

    /**
     * 取消Edittext的焦点
     *
     * @param editText
     */
    private void dissmissFocus(EditText editText) {
        if (editText.hasFocus()) {
            editText.setFocusable(false);
        }
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

        //找到清除text的控件
        mIvClearText_number = (ImageView) mLayout_top.findViewById(R.id.layout_group_sure).findViewById(R.id.iv_clear_Text);
        mIvClearText_number.setOnClickListener((v -> mEt_change1.setText("")));

        mIvClearText_station = (ImageView) mLayout_center.findViewById(R.id.layout_group_sure).findViewById(R.id.iv_clear_Text);
        mIvClearText_station.setOnClickListener((v -> mEt_change2.setText("")));

        mIvClearText_goods = (ImageView) mLayout_bottom.findViewById(R.id.layout_group_sure).findViewById(R.id.iv_clear_Text);
        mIvClearText_goods.setOnClickListener((v -> mEt_change3.setText("")));

        mBt_ok_msg = (Button) findViewById(R.id.bt_ok_msg);

        initEditText();


        /**
         * 货物的点击事件
         */

        // mRecycler_view_goods.setVisibility(View.INVISIBLE);

        initRecycler();


    }

    private void initListener() {
        editFocusListener();
        editClickListener();
        editAddTextListener();

        mBt_ok_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(mEt_change1.getText().toString().substring(2).trim())) {
                    ToastUtils.singleToast(getString(R.string.sure_number));
                    return;
                }
                if ("".equals(mEt_change2.getText().toString().trim())) {
                    ToastUtils.singleToast(getString(R.string.sure_station));
                    return;
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(SureGoodsActivity.this);
                dialog.setTitle(getString(R.string.dialog_title_sure));
                dialog.setMessage(getDialogSendMessage());
                dialog.setPositiveButton(getString(R.string.dialog_messge_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //saveLocalLite();
                        carNumberCount();

                        if (NetWorkManager.isnetworkConnected(mContext)) {
                            postAccept(TimeUtil.getCurrentTimeTos());
                        } else {
                            saveLocalLite(TimeUtil.getCurrentTimeTos());
                        }
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

    private void editAddTextListener() {
        mEt_change1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String stationString = showAndDismiss_clear_text(mEt_change1, mIvClearText_number);
                if (stationString.length() > 0) {
                    List<SerializableNumber> fileterList = PingYinUtil.getInstance()
                            .search_numbers(mNumberList, stationString);
                    Logger.i(fileterList.toString());
                    mNumberAdapter.updateListView(fileterList);
                    //mAdapter.updateData(mContacts);
                } else {
                    if (mNumberAdapter != null) {
                        mNumberAdapter.updateListView(mNumberList);
                    }
                }
            }
        });
        mEt_change2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String stationString = showAndDismiss_clear_text(mEt_change2, mIvClearText_station);
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
        });
        mEt_change3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String stationString = showAndDismiss_clear_text(mEt_change3, mIvClearText_goods);
                if (stationString.length() > 0) {
                    List<SortModel> fileterList = PingYinUtil.getInstance()
                            .search_goods(mGoodsList, stationString);
                    Logger.i(fileterList.toString());
                    mGoodsAdapter.updateListView(fileterList);
                    //mAdapter.updateData(mContacts);
                } else {
                    if (mGoodsAdapter != null) {
                        mGoodsAdapter.updateListView(mGoodsList);
                    }
                }
            }
        });
    }

    private void editFocusListener() {
        mEt_change1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showAndDismiss_clear(mEt_change1, mIvClearText_number);
                    dissmissFocus(mEt_change2);
                    dissmissFocus(mEt_change3);
                    dissmissPop(mPopupWindow_2);
                } else {
                    mIvClearText_number.setVisibility(View.INVISIBLE);
                }
            }
        });
        mEt_change2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showAndDismiss_clear(mEt_change2, mIvClearText_station);
                    dissmissFocus(mEt_change1);
                    dissmissFocus(mEt_change3);
                    dissmissPop(mPopupWindow_1);
                } else

                {
                    mIvClearText_station.setVisibility(View.INVISIBLE);
                }
            }
        });
        mEt_change3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //mIvClearText_goods.setVisibility(View.VISIBLE);
                } else {
                    mIvClearText_goods.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void dissmissPop(SpinnerPopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismissPopWindow();
        }
    }

    private void editClickListener() {
        /**
         *车牌号的点击事件
         * */
        mEt_change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPopupWindow_1 != null) {
                    if (mPopupWindow_1.isShowing()) {
                        return;
                    }
                }
                if (mRecycler_view_goods.hasFocus()) {
                    mRecycler_view_goods.setFocusable(false);
                }

                dissmissFocus(mEt_change2);
                dissmissFocus(mEt_change3);
               /* if (mIvClearText_goods.getVisibility() == View.VISIBLE) {
                    mIvClearText_goods.setVisibility(View.INVISIBLE);
                }*/
                mPopupWindow_1 = new SpinnerPopupWindow.Builder(SureGoodsActivity.this)
                        .setmLayoutManager(null, 1)
                        .setmAdapter(mNumberAdapter)
                        //  .setmHeight(450).setmWidth(500)
                        .setmHeight(500).setmWidth(400)
                        .setOutsideTouchable(false)
                        .setmDrawable(new BitmapDrawable())
                        .setFocusable(false)
                        .build();

                if (!mPopupWindow_1.isShowing()) {
                    mPopupWindow_1.showPopWindowCenter(v);
                }
            }
        });

        mEt_change2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPopupWindow_2 != null) {
                    if (mPopupWindow_2.isShowing()) {
                        return;
                    }
                }
                if (mRecycler_view_goods.hasFocus()) {
                    mRecycler_view_goods.setFocusable(false);
                }
                dissmissFocus(mEt_change1);
                dissmissFocus(mEt_change3);
                /*if (mIvClearText_goods.getVisibility() == View.VISIBLE) {
                    mIvClearText_goods.setVisibility(View.INVISIBLE);
                }*/

                mPopupWindow_2 = new SpinnerPopupWindow.Builder(SureGoodsActivity.this)
                        .setmLayoutManager(null, 0)
                        .setmAdapter(mStationAdapter)
                        .setmHeight(400).setmWidth(500)
                        .setOutsideTouchable(false)
                        .setmDrawable(new BitmapDrawable())
                        .setFocusable(false)
                        .build();

                mPopupWindow_2.showPopWindowCenter(v);
            }

        });
        mEt_change3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmissFocus(mEt_change1);
                dissmissFocus(mEt_change2);
                showAndDismiss_clear(mEt_change3, mIvClearText_goods);
            }
        });
    }

    private String showAndDismiss_clear_text(EditText editText, ImageView imageView) {
        String content = editText.getText().toString();
        if ("".equals(content)) {
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
        return content;
    }

    @NonNull
    private void showAndDismiss_clear(EditText editText, ImageView imageView) {
        String content = editText.getText().toString();
        if ("".equals(content)) {
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 保存到本地数据库
     *
     * @param currentTime
     */
    private void saveLocalLite(String currentTime) {
        SupportPhotoLite supportPhotoLite = new SupportPhotoLite();
        supportPhotoLite.setShuttime(currentTime);
        supportPhotoLite.setUsername(mUsername);
        supportPhotoLite.setGoods(mCar_goods);
        supportPhotoLite.setLicense_plate(mLicense_plate);
        supportPhotoLite.setStation(mCar_station);
        supportPhotoLite.setPhotoPath1(mPhotoPath1);
        supportPhotoLite.setPhotoPath2(mPhotoPath2);
        supportPhotoLite.setPhotoPath3(mPhotoPath3);
        supportPhotoLite.setLicense_color(mColor);
        supportPhotoLite.setCar_type("绿皮车");
        supportPhotoLite.save();
    }


    private void initEditText() {
        if (mNumberList.size() == 0) {
            mEt_change1.setText("粤B");
        } else {
            mEt_change1.setText(mNumberList.get(0).getName());
            mEt_change1.setSelection(mNumberList.get(0).getName().length());
        }
        if (mStationList.size() == 0) {
            mEt_change2.setText("泰安东收费站");
        } else {
            mEt_change2.setText(mStationList.get(0).getStationName());
            mEt_change2.setSelection(mStationList.get(0).getStationName().length());
        }
        if (mGoodsList.size() == 0) {
            mEt_change3.setText("西兰花");
        } else {
            mEt_change3.setText(mGoodsList.get(0).getScientificname());
        }
    }


    private void refreshView(int stype) {
        /*if (stype == 001) {
            Collections.sort(mNumberList);
            mNumberAdapter.notifyDataSetChanged();
        }
        */
        if (stype == 001) {
            Collections.sort(mNumberList);
            mNumberAdapter.updateListView(mNumberList);
        }

        if (stype == 002) {
            Collections.sort(mStationList);
            mStationAdapter.updateListView(mStationList);
        }

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
     *
     * @param currentTime
     */
    private void postAccept(String currentTime) {

    /*    //SimplePhoto();
        List<String> pathList = getPathList();
        Logger.i("" + getPathList().size());
        Logger.i("" + pathList.size());

        MultipartBody.Builder builder = new MultipartBody.Builder();

//        Map<String, RequestBody> bodyMap = new HashMap<>();

        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);//image/png
            builder.addFormDataPart("image" + i, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
           *//* File file = new File(pathList.get(i));
            bodyMap.put("image"+i+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"),file));*//*
        }
        List<MultipartBody.Part> parts = builder.build().parts();*/

        List<MultipartBody.Part> parts = PathUtil.getMultipartBodyPart(mPhotoPath1, mPhotoPath2, mPhotoPath3);

        Observable<HttpResultPostImg> observable = HttpMethods.getInstance().getApi()
                .postThreeImg("大货车", mColor, currentTime, mUsername,
                        mCar_station, mLicense_plate, mCar_goods, parts);

        observable.compose(RxHolder.io_main()).subscribe(new Subscriber<HttpResultPostImg>() {
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
                    CarNumberCount.CarNumberCut(mContext);
                    Intent intent = new Intent(SureGoodsActivity.this, PhotoActivity.class);
                    intent.putExtra("username", mUsername);
                    startActivity(intent);
                } else if (code == 300) {
                    ToastUtils.singleToast("上传失败");
                    saveLocalLite(currentTime);
                }
                Logger.i("" + code);
                Logger.i("" + msg);
            }
        });
    }

    /**
     * 对已拍摄车辆以及未上传车辆进行计数
     */
    private void carNumberCount() {
        SPUtils.add_one(mActivity, SPUtils.CAR_COUNT);
        int car_count = (int) SPUtils.get(mActivity, SPUtils.CAR_COUNT, 0);
        Logger.i("car_count------------" + car_count);

        SPUtils.add_one(mActivity, SPUtils.CAR_NOT_COUNT);
        int car_not_count = (int) SPUtils.get(mActivity, SPUtils.CAR_NOT_COUNT, 0);
        Logger.i("car_not_count------------" + car_not_count);
    }



    @Override
    protected void onPause() {
        super.onPause();
        //存入缓存
        ACache.get(mActivity).put("sessions", (ArrayList<SerializableNumber>) mNumberList);
        ACache.get(mActivity).put("stations", (ArrayList<SerializableStation>) mStationList);
        ACache.get(mActivity).put("goods", (ArrayList<SortModel>) mGoodsList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

