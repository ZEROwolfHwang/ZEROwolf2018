package com.zero.wolf.greenroad.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.DividerGridItemDecoration;
import com.zero.wolf.greenroad.adapter.SureGoodsAdapter;
import com.zero.wolf.greenroad.bean.SerializableGoods;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.interfacy.TextChangeWatcher;
import com.zero.wolf.greenroad.interfacy.TextFragmentListener;
import com.zero.wolf.greenroad.smartsearch.PinyinComparator;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.PingYinUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/7/17.
 */

public class GoodsFragment extends Fragment implements TextChangeWatcher.AfterTextListener {

    private static final String GOODS_DIR = "good_icon";
    private static GoodsFragment sFragment;
    Unbinder unbinder;

    private static EditText mEditText;

    @BindView(R.id.recycler_view_goods_sure)
    RecyclerView mRecyclerView;

    private SureGoodsAdapter mGoodsAdapter;

    private String mCar_goods;

    private String goodsText;
    private static SerializableMain2Sure mMain2Sure;
    private static String mGoods_i;
    private AssetManager mAssetManager;
    private String[] mGoodsNames;

    private static String[] alias = {"给weus骄傲的行情无限", "说的侮辱大家才能", "都无二的勘测机",
            "卖家说的法第五", ",设计费都是对你是否及时打开",
            "1234124戛洒hiu瓦斯鉴定表", "奥斯卡单位澳大马上", "IQ网上订餐MAU爱打架"};
    private ArrayList<SerializableGoods> mGoodsArrayList;
    private String[] scientific_names;
    private ArrayList<SerializableGoods> mAsObject;
    private StringBuilder mGoodsBuilder;


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

        scientific_names = getResources().getStringArray(R.array.science_name);

    }

    private void initGoodsData() {

        mAsObject = (ArrayList<SerializableGoods>) ACache
                .get(getActivity()).getAsObject(ACache.GOODSACACHE);

//        Logger.i(mAsObject.size() + "");

        mGoodsArrayList = new ArrayList<>();

        Logger.i(mGoodsArrayList.size() + "");

        if (mAsObject != null && mAsObject.size() != 0) {
            if (mAsObject.size() == scientific_names.length) {
                Logger.i("goods走的缓存");
                if (mGoodsArrayList.size() == 0) {
                } else {
                    mGoodsArrayList.clear();
                }
                mGoodsArrayList.addAll(mAsObject);
            } else {
                Logger.i("goods未加载完毕未缓存");
                addGoodsData();
            }
        } else {
            Logger.i("goods直接从头加载数据");
            addGoodsData();
        }


    }

    private void addGoodsData() {
        mAssetManager = getContext().getAssets();
        try {
            mGoodsNames = mAssetManager.list(GOODS_DIR);
            if (mGoodsArrayList.size() == 0) {
            } else {
                mGoodsArrayList.clear();
            }
            for (int i = 0; i < mGoodsNames.length; i++) {
                Logger.i(mGoodsNames.length + "");
                //Bitmap bitmap = getImageFromAssetsFile();

                String scientific_name = scientific_names[i];
                String alia = alias[i];
                String bitmap_url = GOODS_DIR + "/" + mGoodsNames[i];

                SerializableGoods goods = new SerializableGoods();

                goods.setAlias(alia);
                goods.setScientific_name(scientific_name);
                goods.setBitmapUrl(bitmap_url);

                String sortLetters = PingYinUtil.getInstance().getSortLetterBySortKey(scientific_name);
                if (sortLetters == null) {
                    sortLetters = PingYinUtil.getInstance().getSortLetter(alia);
                }
                goods.setSortLetters(sortLetters);

                String sortKey = PingYinUtil.format(scientific_name + alias);
                goods.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));
                goods.setWholeSpell(PingYinUtil.getInstance().parseSortKeyWholeSpell(sortKey));


                mGoodsArrayList.add(goods);


            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.i(e.getMessage());
            return;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);

        unbinder = ButterKnife.bind(this, view);

        initEditText(view);


        initGoodsData();

        initView(view);
        initRecyclerView();
        return view;
    }

    /**
     * 初始化EditText中的内容
     *
     * @param view
     */
    private void initEditText(View view) {
        if (mGoodsBuilder == null) {
            mGoodsBuilder = new StringBuilder();
        } else {
            if (mGoodsBuilder.length() == 0) {
                mGoodsBuilder.append(mGoods_i);
            } else {
                mGoodsBuilder.delete(0, mGoodsBuilder.length());
                mGoodsBuilder.append(mGoods_i);

            }
        }

        mEditText = (EditText) view.findViewById(R.id.goods_edit_text);

        mEditText.setText(mGoods_i);
        mEditText.setSelection(mGoods_i.length());


    }

    private void initRecyclerView() {

        if (mAsObject == null) {
            PinyinComparator pinyinComparator = new PinyinComparator();
            Collections.sort(mGoodsArrayList, pinyinComparator);// 根据a-z进行排序源数据
        }
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mGoodsAdapter = new SureGoodsAdapter(getContext(), mGoodsArrayList, new SureGoodsAdapter.onItemClick() {
            @Override
            public void itemClick(SerializableGoods serializableGoods, int position) {

                String scientificname = serializableGoods.getScientific_name();
                mGoodsBuilder.append(scientificname + ";");
                mEditText.setText(mGoodsBuilder.toString());
                mEditText.setSelection(mGoodsBuilder.length());
                //进行置顶操作
                serializableGoods.setTop(1);
                serializableGoods.setTime(System.currentTimeMillis());
                refreshView();
            }
        });
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(),
                3));
        // mListView.setAdapter(mGoodsAdapter);
        mRecyclerView.setAdapter(mGoodsAdapter);
    }

    private void refreshView() {
        Collections.sort(mGoodsArrayList);
        mGoodsAdapter.updateListView(mGoodsArrayList);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String goodString = mEditText.getText().toString().trim();
        if (mGoodsBuilder != null) {
            mGoodsBuilder.delete(0, mGoodsBuilder.length());
            mGoodsBuilder.append(goodString);
        }
        String[] split = goodString.split(";");
        Logger.i("" + split.length + "////////////////" + split.toString());
        for (int i = 0; i < split.length; i++) {
            Logger.i("~~~~~~~~~~~~~" + split[i]);
        }

        if ("".equals(goodString)) {
            mGoodsAdapter.updateListView(mGoodsArrayList);
        }/* else if (split.length == 1) {
            if (split[0].endsWith(";")) {
                mGoodsAdapter.updateListView(mGoodsArrayList);
            } else {
                String last_edit = split[split.length - 1];
                List<SerializableGoods> fileterList = PingYinUtil.getInstance()
                        .search_goods(mGoodsArrayList, last_edit);
                Logger.i(fileterList.toString());
                mGoodsAdapter.updateListView(fileterList);
            }
        }*/ else {
                Logger.i(split[split.length - 1]+"********");
            if (split[split.length - 1].endsWith(";")) {
                Logger.i(split[split.length - 1]+"********");
                mGoodsAdapter.updateListView(mGoodsArrayList);
            } else {
                String last_edit = split[split.length - 1];
                Logger.i(last_edit);
                List<SerializableGoods> fileterList = PingYinUtil.getInstance()
                        .search_goods(mGoodsArrayList, last_edit);
                for (int i = 0; i < fileterList.size(); i++) {
                    Logger.i(fileterList.get(i).getScientific_name());
                    Logger.i(fileterList.get(i).getSimpleSpell());
                }
                mGoodsAdapter.updateListView(fileterList);
            }
        }

        if (goodString.length() > 0) {
            //mAdapter.updateData(mContacts);
        } else {
            if (mGoodsAdapter != null) {
                mGoodsAdapter.updateListView(mGoodsArrayList);
            }
        }
    }


    private void initView(View view) {

        mEditText.addTextChangedListener(new TextChangeWatcher(this));
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
    public void onPause() {
        super.onPause();

        ACache.get(getActivity()).put(ACache.GOODSACACHE, mGoodsArrayList);

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Object asObject = ACache.get(getActivity()).getAsObject(ACache.GOODSACACHE);
        ArrayList<SerializableGoods> asObject1 = (ArrayList<SerializableGoods>) ACache.get(getActivity()).getAsObject(ACache.GOODSACACHE);
        Logger.i("++++++++" + asObject.toString() + "____" + asObject1.size() + asObject1.get(0).getScientific_name());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
    /*@OnClick(R.id.goods_edit_text)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_edit_text:
                String goodsEdit = mEditText.getText().toString().trim();
                if (goodsEdit != null && !"".equals(goodsEdit)) {
                    mEditText.setSelection(goodsEdit.length());
                }
                break;
            default:
                break;
        }
    }*/
}
