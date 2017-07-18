package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.PhotoActivity;
import com.zero.wolf.greenroad.adapter.SureGoodsAdapter;
import com.zero.wolf.greenroad.httpresultbean.HttpResultPostImg;
import com.zero.wolf.greenroad.https.HttpMethods;
import com.zero.wolf.greenroad.interfacy.TextChangeListenner;
import com.zero.wolf.greenroad.litepalbean.SupportGoods;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;
import com.zero.wolf.greenroad.manager.CarNumberCount;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.smartsearch.PinyinComparator;
import com.zero.wolf.greenroad.smartsearch.SortModel;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.PathUtil;
import com.zero.wolf.greenroad.tools.PingYinUtil;
import com.zero.wolf.greenroad.tools.RxHolder;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;
import com.zero.wolf.greenroad.tools.ViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.Subscriber;

import static com.zero.wolf.greenroad.R.id.tv_change;
import static org.litepal.crud.DataSupport.findAll;

/**
 * Created by Administrator on 2017/7/17.
 */

public class GoodsFragment extends Fragment implements TextChangeListenner.AfterTextListener {

    private List<SortModel> mGoodsList = new ArrayList<>();
    private ArrayList<SortModel> mAcacheGoods;

    private static GoodsFragment sFragment;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private ImageView mIvClearTextGoods;
    private SureGoodsAdapter mGoodsAdapter;
    private static Context sContext;
    private Button mButton;
    private static String sUsername;
    private static String sStationName;
    private static String sColor;
    private static String sPhotoPath1;
    private static String sPhotoPath2;
    private static String sPhotoPath3;
    private String mCar_goods;
    private String mCar_number;
    private String mCar_station;

    public static GoodsFragment newInstance(String username, String stationName,
                                            String color, String photoPath1, String photoPath2, String photoPath3, Context context) {
        if (sFragment == null) {
            sFragment = new GoodsFragment();
        }
        sContext = context;
        sUsername = username;
        sStationName = stationName;
        sColor = color;
        sPhotoPath1 = photoPath1;
        sPhotoPath2 = photoPath2;
        sPhotoPath3 = photoPath3;
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAcacheGoods = (ArrayList<SortModel>) ACache
                .get(getActivity()).getAsObject("goods");

        initGoodsData();
    }

    /**
     * 加载货物的数据及缓存
     */
    private void initGoodsData() {
        List<SupportGoods> supportGoodses = findAll(SupportGoods.class);

        if (mAcacheGoods != null) {
            if (mAcacheGoods.size() == supportGoodses.size()) {
                if (mGoodsList != null) {
                    mGoodsList.clear();
                }
                mGoodsList.addAll(mAcacheGoods);
            } else {
                mAcacheGoods.clear();
                addGoodsData(supportGoodses);
            }
        } else {
            addGoodsData(supportGoodses);
        }
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);

        //View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_goods, null, false);

        initView(view);

        initData();

        return view;
    }

    private void initData() {


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CarNumberFragment.setTextChangedFragment((edittext -> {
                    mCar_number = edittext;
                }));
                StationFragment.setTextChangedFragment((edittext -> {
                    mCar_station = edittext;
                }));

                if ("".equals(mCar_number.substring(2).trim())) {
                    ToastUtils.singleToast(getString(R.string.sure_number));
                    return;
                }
                if ("".equals(mCar_station)) {
                    ToastUtils.singleToast(getString(R.string.sure_station));
                    return;
                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle(getString(R.string.dialog_title_sure));
                dialog.setMessage("1111"/*+getDialogSendMessage()*/);
                dialog.setPositiveButton(getString(R.string.dialog_messge_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //saveLocalLite();
                        carNumberCount();

                        if (NetWorkManager.isnetworkConnected(getContext())) {
                            postAccept(TimeUtil.getCurrentTimeTos());
                        } else {
                            saveLocalLite(TimeUtil.getCurrentTimeTos());
                        }
                    }
                });
                dialog.setNegativeButton(getString(R.string.dialog_message_Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
    }

    /**
     * 保存到本地数据库
     *
     * @param currentTime
     */
    private void saveLocalLite(String currentTime) {
        SupportPhotoLite supportPhotoLite = new SupportPhotoLite();
        supportPhotoLite.setShuttime(currentTime);
        supportPhotoLite.setUsername(sUsername);
        supportPhotoLite.setGoods(mCar_goods);
        supportPhotoLite.setLicense_plate(mCar_number);
        supportPhotoLite.setStation(mCar_station);
        supportPhotoLite.setPhotoPath1(sPhotoPath1);
        supportPhotoLite.setPhotoPath2(sPhotoPath2);
        supportPhotoLite.setPhotoPath3(sPhotoPath3);
        supportPhotoLite.setLicense_color(sColor);
        supportPhotoLite.setCar_type("绿皮车");
        supportPhotoLite.save();
    }


    /**
     * 向服务器Post所有的信息
     *
     * @param currentTime
     */
    private void postAccept(String currentTime) {

        List<MultipartBody.Part> parts = PathUtil.getMultipartBodyPart(sPhotoPath1, sPhotoPath2, sPhotoPath3);

        Observable<HttpResultPostImg> observable = HttpMethods.getInstance().getApi()
                .postThreeImg("大货车", "蓝牌", currentTime, sUsername,
                        mCar_station, mCar_number, mCar_goods, parts);


/*
        observable.unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultPostImg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResultPostImg httpResultPostImg) {

                    }
                });*/

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
                    CarNumberCount.CarNumberCut(getContext());
                    Intent intent = new Intent(getActivity(), PhotoActivity.class);
                    intent.putExtra("username", sUsername);
                    intent.putExtra("stationName", sStationName);
                    startActivity(intent);
                } else if (code == 300) {
                    ToastUtils.singleToast("上传失败");
                    saveLocalLite(currentTime);
                } else {
                    ToastUtils.singleToast("其他错误");
                }
                Logger.i("" + code);
                Logger.i("" + msg);
            }
        });
    }

    private void carNumberCount() {
        SPUtils.add_one(getContext(), SPUtils.CAR_COUNT);
        int car_count = (int) SPUtils.get(getContext(), SPUtils.CAR_COUNT, 0);
        Logger.i("car_count------------" + car_count);

        SPUtils.add_one(getContext(), SPUtils.CAR_NOT_COUNT);
        int car_not_count = (int) SPUtils.get(getContext(), SPUtils.CAR_NOT_COUNT, 0);
        Logger.i("car_not_count------------" + car_not_count);
    }

    /* private String getDialogSendMessage() {
      *//*   mLicense_plate = mEt_change1.getText().toString();
        mCar_station = mEt_change2.getText().toString();
        mCar_goods = mEt_change3.getText().toString();*//*
*//*
        String dialog_message = "车  牌  号：" + mLicense_plate + "\n"
                + "货物名称：" + mCar_goods + "\n"
                + "点击“确认”将提交信息" + "\n"
                + "点击“取消”可再次修改";
        return dialog_message;*//*
    }*/
    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_goods_sure);
        mButton = (Button) view.findViewById(R.id.bt_ok_msg);
        LinearLayout mLayout_bottom = (LinearLayout) view.findViewById(R.id.layout_bottom_sure);

        //找到固定的textview
        TextView textView1 = (TextView) mLayout_bottom.findViewById(R.id.layout_group_sure).findViewById(R.id.tv_no_change);
        textView1.setText(getString(R.string.text_car_goods_sure));

        //找到改变的TextView
        mEditText = (EditText) mLayout_bottom.findViewById(R.id.layout_group_sure).findViewById(tv_change);

        //找到清除text的控件
        mIvClearTextGoods = (ImageView) mLayout_bottom.findViewById(R.id.layout_group_sure).findViewById(R.id.iv_clear_Text);
        mIvClearTextGoods.setOnClickListener((v -> mEditText.setText("")));

        mEditText.addTextChangedListener(new TextChangeListenner(this));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PinyinComparator pinyinComparator = new PinyinComparator();

        Collections.sort(mGoodsList, pinyinComparator);// 根据a-z进行排序源数据

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mGoodsAdapter = new SureGoodsAdapter(getContext(), mGoodsList, new SureGoodsAdapter.onItemClick() {
            @Override
            public void itemClick(SortModel sortModel, int position) {
                String scientificname = sortModel.getScientificname();
                mEditText.setText(scientificname);
                mGoodsAdapter.updateListView(mGoodsList);
                mEditText.setSelection(scientificname.length());
            }
        });
        // mListView.setAdapter(mGoodsAdapter);
        mRecyclerView.setAdapter(mGoodsAdapter);

        ACache.get(getActivity()).put("goods", (ArrayList<SortModel>) mGoodsList);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mCar_goods = mEditText.getText().toString();
        String stationString = ViewUtils.showAndDismiss_clear_text(mEditText, mIvClearTextGoods);
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
