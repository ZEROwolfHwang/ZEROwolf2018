package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.PhotoActivity;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.adapter.SureGoodsAdapter;
import com.zero.wolf.greenroad.bean.PostContent;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.interfacy.TextChangeWatcher;
import com.zero.wolf.greenroad.interfacy.TextFragmentListener;
import com.zero.wolf.greenroad.servicy.PostIntentService;
import com.zero.wolf.greenroad.smartsearch.PinyinComparator;
import com.zero.wolf.greenroad.smartsearch.SortModel;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.PathUtil;
import com.zero.wolf.greenroad.tools.PingYinUtil;
import com.zero.wolf.greenroad.tools.ViewUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MultipartBody;

/**
 * Created by Administrator on 2017/7/17.
 */

public class GoodsFragment extends Fragment implements TextChangeWatcher.AfterTextListener, View.OnClickListener {

    private static GoodsFragment sFragment;
    Unbinder unbinder;

    private static EditText mEditText;
    @BindView(R.id.goods_img_clear_text)
    ImageView mIvClearTextGoods;
    @BindView(R.id.recycler_view_goods_sure)
    RecyclerView mRecyclerView;

    private SureGoodsAdapter mGoodsAdapter;
    private static Context sContext;
    private static String mUsername;
    private static String sStationName;
    private static String mColor;
    private static String mPhotoPath1;
    private static String mPhotoPath2;
    private static String mPhotoPath3;
    private String mCar_goods;
    private String mCar_number;
    private String mCar_station;
    private static List<SortModel> sGoodsList;
    private String mCurrentTimeTos;
    private static String mOperator;
    private String goodsText;
    private static SerializableMain2Sure mMain2Sure;
    private static String mGoods_i;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);

        unbinder = ButterKnife.bind(this, view);
        mEditText = (EditText) view.findViewById(R.id.goods_edit_text);

        mEditText.setText(mGoods_i);

        initView(view);


        return view;
    }

    private String getDialogSendMessage() {
        String dialog_message = "车  牌  号：" + mCar_number + "\n"
                + "货物名称：" + mCar_goods + "\n"
                + "点击“确认”将提交信息" + "\n"
                + "点击“取消”可再次修改";
        return dialog_message;
    }


    private void initView(View view) {

        //找到改变的TextView
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
        }
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

    private void backToPhotoActivity() {
        getActivity().finish();
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra("username", mUsername);
        intent.putExtra("stationName", sStationName);
        startActivity(intent);
    }


    private void startPostIntentService() {
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        mRecyclerView.setAdapter(mGoodsAdapter);

        mEditText.addTextChangedListener(new TextChangeWatcher(this));
        mEditText.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
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

    }

    @Override
    public void onResume() {
        super.onResume();

    }

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok_msg:
                sendToService();
                getActivity().finish();
        }
    }*/

    /**
     *
     */
    private void sendToService() {

    }

    @Override
    public void onPause() {
        super.onPause();
        String editText = mEditText.getText().toString().trim();
        goodsText = editText;
        ACache.get(getActivity()).put("goodsText", (String) goodsText);

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
