package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.zero.wolf.greenroad.activity.SaveToLocation;
import com.zero.wolf.greenroad.activity.PhotoActivity;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.adapter.SureGoodsAdapter;
import com.zero.wolf.greenroad.bean.PostContent;
import com.zero.wolf.greenroad.interfacy.TextChangeWatcher;
import com.zero.wolf.greenroad.manager.CarNumberCount;
import com.zero.wolf.greenroad.presenter.NetWorkManager;
import com.zero.wolf.greenroad.servicy.PostIntentService;
import com.zero.wolf.greenroad.smartsearch.PinyinComparator;
import com.zero.wolf.greenroad.smartsearch.SortModel;
import com.zero.wolf.greenroad.tools.PathUtil;
import com.zero.wolf.greenroad.tools.PingYinUtil;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;
import com.zero.wolf.greenroad.tools.ViewUtils;

import java.util.Collections;
import java.util.List;

import okhttp3.MultipartBody;

import static com.zero.wolf.greenroad.R.id.tv_change;

/**
 * Created by Administrator on 2017/7/17.
 */

public class GoodsFragment extends Fragment implements TextChangeWatcher.AfterTextListener {

    private static GoodsFragment sFragment;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private ImageView mIvClearTextGoods;
    private SureGoodsAdapter mGoodsAdapter;
    private static Context sContext;
    private Button mButton;
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

    public static GoodsFragment newInstance(String username, String stationName,
                                            String color, String photoPath1, String photoPath2, String photoPath3, List<SortModel> goodsList, Context context) {
        if (sFragment == null) {
            sFragment = new GoodsFragment();
        }
        sGoodsList = goodsList;
        sContext = context;
        mUsername = username;
        sStationName = stationName;
        mColor = color;
        mPhotoPath1 = photoPath1;
        mPhotoPath2 = photoPath2;
        mPhotoPath3 = photoPath3;
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_goods_sure);
        mButton = (Button) view.findViewById(R.id.bt_ok_msg);
        LinearLayout mLayout_bottom = (LinearLayout) view.findViewById(R.id.layout_bottom_sure);

        //找到固定的textview
        TextView textView1 = (TextView) mLayout_bottom.findViewById(R.id.layout_group_sure).findViewById(R.id.tv_no_change);
        textView1.setText(getString(R.string.text_car_goods_sure));

        //找到改变的TextView
        mEditText = (EditText) mLayout_bottom.findViewById(R.id.layout_group_sure).findViewById(tv_change);
        if (sGoodsList.size() == 0) {
            mEditText.setText("西兰花");
        } else {
            mEditText.setText(sGoodsList.get(0).getScientificname());
        }
        //找到清除text的控件
        mIvClearTextGoods = (ImageView) mLayout_bottom.findViewById(R.id.layout_group_sure).findViewById(R.id.iv_clear_Text);
        mIvClearTextGoods.setOnClickListener((v -> mEditText.setText("")));

        mEditText.addTextChangedListener(new TextChangeWatcher(this));

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
                            SaveToLocation.saveLocalLite(mCurrentTimeTos,"卡车", mUsername, mColor,
                                    mCar_number,mCar_station,mCar_goods,
                                    mPhotoPath1, mPhotoPath2, mPhotoPath3, 0);
                            backToPhotoActivity();
                            ToastUtils.singleToast("上传失败,已保存至本地");
                        }
                    }
                });
                dialog.setNegativeButton(getString(R.string.dialog_message_Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
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
        content.setParts(parts);
        content.setCurrentTime(mCurrentTimeTos);
        content.setPhotoPath1(mPhotoPath1);
        content.setPhotoPath2(mPhotoPath2);
        content.setPhotoPath3(mPhotoPath3);

        PostIntentService.startActionPost((AppCompatActivity) getActivity(),content);
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
}
