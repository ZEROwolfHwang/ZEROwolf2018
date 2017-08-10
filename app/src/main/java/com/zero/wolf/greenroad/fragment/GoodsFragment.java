package com.zero.wolf.greenroad.fragment;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.adapter.SureGoodsAdapter;
import com.zero.wolf.greenroad.bean.SerializableGoods;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.interfacy.TextFragmentListener;
import com.zero.wolf.greenroad.smartsearch.SortModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/7/17.
 */

public class GoodsFragment extends Fragment {

    private static final String GOODS_DIR = "good_icon";
    private static GoodsFragment sFragment;
    Unbinder unbinder;

    private static EditText mEditText;
    @BindView(R.id.goods_img_clear_text)
    ImageView mIvClearTextGoods;
    @BindView(R.id.recycler_view_goods_sure)
    RecyclerView mRecyclerView;

    private SureGoodsAdapter mGoodsAdapter;

    private String mCar_goods;

    private static List<SortModel> sGoodsList;

    private String goodsText;
    private static SerializableMain2Sure mMain2Sure;
    private static String mGoods_i;
    private AssetManager mAssetManager;
    private String[] mGoodsNames;

    private static String[] scientific_names = {"苹果", "杏子", "西蓝花", "西瓜", "水蜜桃", "梨子", "白刺","茄子"};
    private static String[] alias = {"给weus骄傲的行情无限", "说的侮辱大家才能", "都无二的勘测机",
            "卖家说的法第五", ",设计费都是对你是否及时打开",
            "1234124戛洒hiu瓦斯鉴定表", "奥斯卡单位澳大马上", "IQ网上订餐MAU爱打架"};
    private ArrayList<SerializableGoods> mGoodsArrayList;

    public static GoodsFragment newInstance(String goods) {
        if (sFragment == null) {
            sFragment = new GoodsFragment();
        }
        mGoods_i = goods;
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void initGoodsData() {
        mAssetManager = getContext().getAssets();
        mGoodsArrayList = new ArrayList<>();
        try {
            mGoodsNames = mAssetManager.list(GOODS_DIR);
            for (int i = 0; i < mGoodsNames.length; i++) {
                Logger.i(mGoodsNames.length + "");
                Bitmap bitmap = getImageFromAssetsFile(GOODS_DIR+"/"+mGoodsNames[i]);
                SerializableGoods goods = new SerializableGoods();
                goods.setAlias(alias[i]);
                goods.setScientific_name(scientific_names[i]);
                goods.setBitmap(bitmap);
                mGoodsArrayList.add(goods);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.i(e.getMessage());
            return;
        }
    }
     //从Assets中读取图片
    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);

        unbinder = ButterKnife.bind(this, view);
        mEditText = (EditText) view.findViewById(R.id.goods_edit_text);

        mEditText.setText(mGoods_i);
        initGoodsData();

        initView(view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mGoodsAdapter = new SureGoodsAdapter(getContext(), mGoodsArrayList, new SureGoodsAdapter.onItemClick() {
            @Override
            public void itemClick(SerializableGoods sortModel, int position) {
                String scientificname = sortModel.getScientific_name();
                mEditText.setText(scientificname);
                mEditText.setSelection(scientificname.length());
            }
        });
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),
                LinearLayoutManager.HORIZONTAL, 10, Color.WHITE));
        // mListView.setAdapter(mGoodsAdapter);
        mRecyclerView.setAdapter(mGoodsAdapter);
    }


    private void initView(View view) {

       /* //找到改变的TextView
        String aCacheGoodsText = ACache
                .get(getActivity()).getAsString("goodsText");
        String editText = mEditText.getText().toString().trim();
        if (editText != null) {
            mEditText.setSelection(editText.length());
        }
        if (aCacheGoodsText == null || "".equals(aCacheGoodsText)) {
            mEditText.setText("西兰花");
            goodsText = "苹果";
        } else {
            goodsText = aCacheGoodsText;
            mEditText.setText(aCacheGoodsText);
        }*/
        //找到清除text的控件

        mIvClearTextGoods.setOnClickListener((v -> mEditText.setText("")));



       /* mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarNumberFragment.setTextChangedFragment((edittext -> {
                    mCar_number = edittext;
                }));

                if ("".equals(mCar_number.substring(2).trim())) {
                    ToastUtils.singleToast(getString(R.string.sure_number));
                    return;
                }


                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle(getString(R.string.dialog_title_sure));
                dialog.setMessage(getDialogSendMessage());
                dialog.setPositiveButton(getString(R.string.dialog_messge_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //saveLocalLite();
                        CarNumberCount.CarNumberAdd(getContext());
                        mCurrentTimeTos = TimeUtil.getCurrentTimeTos();
                        if (NetWorkManager.isnetworkConnected(getContext())) {
                            startPostIntentService();
                            //BackToPhotoActivityHelper.backToPhotoActivity((AppCompatActivity) getActivity(),mUsername,sStationName);
                            backToPhotoActivity();
                            // postAccept(TimeUtil.getCurrentTimeTos());
                        } else {
                            SaveToLocation.saveLocalLite(mCurrentTimeTos, "卡车", mOperator, mUsername, mColor,
                                    mCar_number, mCar_station, mCar_goods,
                                    mPhotoPath1, mPhotoPath2, mPhotoPath3, 0);
                            Logger.i(mOperator + "///////////////////");
                            backToPhotoActivity();
                            ToastUtils.singleToast("上传失败,已保存至本地");
                        }
                    }
                });
                dialog.setNegativeButton(getString(R.string.dialog_message_Cancel), (dialog1, which) -> {
                    Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
                    dialog1.dismiss();
                });

                dialog.show();

        }
    });*/
    }

    public static void setTextChangedFragment(TextFragmentListener listener) {
        if (mEditText != null) {
            String number = mEditText.getText().toString().trim();
            listener.textChanged(number);
        }
    }


 /*   private void startPostIntentService() {
        List<MultipartBody.Part> parts = PathUtil
                .getMultipartBodyPart(mPhotoPath1, mPhotoPath2, mPhotoPath3);
        PostContent content = new PostContent();
        content.setStatiomName(sStationName);
        content.setCar_type("卡车");
        content.setColor(mColor);
        content.setCar_number(mCar_number);
        content.setCar_station(mCar_station);
        content.setCar_goods(mCar_goods);
        content.setUsername(mUsername);
        content.setOperator(mOperator);
        content.setParts(parts);
        content.setCurrentTime(mCurrentTimeTos);
        content.setPhotoPath1(mPhotoPath1);
        content.setPhotoPath2(mPhotoPath2);
        content.setPhotoPath3(mPhotoPath3);

        PostIntentService.startActionPost((AppCompatActivity) getActivity(), content);
    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*
        PinyinComparator pinyinComparator = new PinyinComparator();

        Collections.sort(sGoodsList, pinyinComparator);// 根据a-z进行排序源数据

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mGoodsAdapter = new SureGoodsAdapter(getContext(), sGoodsList, new SureGoodsAdapter.onItemClick() {
            @Override
            public void itemClick(SortModel sortModel, int position) {
                String scientificname = sortModel.getScientificname();
                mEditText.setText(scientificname);
                mGoodsAdapter.updateListView(sGoodsList);
                mEditText.setSelection(scientificname.length());
            }
        });
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),
                LinearLayoutManager.HORIZONTAL, 10, Color.WHITE));
        // mListView.setAdapter(mGoodsAdapter);
        mRecyclerView.setAdapter(mGoodsAdapter);*/

     /*   mEditText.addTextChangedListener(new TextChangeWatcher(this));
        mEditText.setOnClickListener(this);*/
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

 /*   @Override
    public void afterTextChanged(Editable editable) {
        mCar_goods = mEditText.getText().toString();
        String stationString = ViewUtils.showAndDismiss_clear_text(mEditText, mIvClearTextGoods);
        if (stationString.length() > 0) {
            List<SortModel> fileterList = PingYinUtil.getInstance()
                    .search_goods(sGoodsList, stationString);
            Logger.i(fileterList.toString());
            mGoodsAdapter.updateListView(fileterList);
            //mAdapter.updateData(mContacts);
        } else {
            if (mGoodsAdapter != null) {
                mGoodsAdapter.updateListView(sGoodsList);
            }
        }

    }*/

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick(R.id.goods_edit_text)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_edit_text:
                String goodsEdit = mEditText.getText().toString().trim();
                if (goodsEdit != null && !"".equals(goodsEdit)) {
                    mEditText.setSelection(goodsEdit.length());
                    mIvClearTextGoods.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
    }
}
