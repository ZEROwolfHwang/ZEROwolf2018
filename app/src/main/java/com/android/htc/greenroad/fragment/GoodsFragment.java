package com.android.htc.greenroad.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.android.htc.greenroad.R;
import com.android.htc.greenroad.adapter.DividerGridItemDecoration;
import com.android.htc.greenroad.adapter.GoodsTextAdapter;
import com.android.htc.greenroad.adapter.SureGoodsAdapter;
import com.android.htc.greenroad.bean.SerializableGoods;
import com.android.htc.greenroad.bean.SerializableMain2Sure;
import com.android.htc.greenroad.interfacy.TextChangeWatcher;
import com.android.htc.greenroad.interfacy.TextFragmentListener;
import com.android.htc.greenroad.tools.ACache;
import com.android.htc.greenroad.tools.Cn2Spell;
import com.android.htc.greenroad.tools.PingYinUtil;
import com.android.htc.greenroad.tools.ToastUtils;

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

public class GoodsFragment extends Fragment implements TextChangeWatcher.AfterTextListener, RadioGroup.OnCheckedChangeListener {

    private static final String GOODS_DIR_SHUCAI = "goods_shucai";
    private static final String GOODS_DIR_SHUIGUO = "goods_shuiguo";
    private static final String GOODS_DIR_SHUICHANPIN = "goods_shuichanpin";
    private static final String GOODS_DIR_CHUQIN = "goods_chuqin";
    private static final String GOODS_DIR_ROUDANNAI = "goods_roudannai";
    private static final String GOODS_DIR_ZALIANG = "goods_zaliang";
    private static final String GOODS_DIR_QITA = "goods_qita";

    private static final String KIND_SHUCAI = "kind_shucai";
    private static final String KIND_SHUIGUO = "kind_shuiguo";
    private static final String KIND_SHUICHANPIN = "kind_shuichanpin";
    private static final String KIND_CHUQIN = "kind_xuqin";
    private static final String KIND_ROUDANNAI = "kind_roudannai";
    private static final String KIND_ZALIANG = "kind_zaliang";
    private static final String KIND_QITA = "kind_qita";
    private static GoodsFragment sFragment;
    Unbinder unbinder;

    private static EditText mEditText;

    @BindView(R.id.recycler_view_goods_sure)
    RecyclerView mRecyclerView;
    @BindView(R.id.edit_text_qita)
    EditText mEditTextQita;
    @BindView(R.id.btn_sure_qita)
    Button mBtnSureQita;
    @BindView(R.id.rl_edit_qita)
    RelativeLayout mRlEditQita;

    private SureGoodsAdapter mGoodsAdapter;

    private String mCar_goods;

    private String goodsText;
    private static SerializableMain2Sure mMain2Sure;
    private static String mGoods_i;
    private AssetManager mAssetManager;

    private String[] mGoodsAsset_shucai;
    private String[] mGoodsAsset_shuiguo;
    private String[] mGoodsAsset_shuichanpin;
    private String[] mGoodsAsset_chuqin;
    private String[] mGoodsAsset_roudannai;
    private String[] mGoodsAsset_zaliang;

    private ArrayList<SerializableGoods> mGoodsAllList;
    private ArrayList<SerializableGoods> mGoodsShuCais;
    private ArrayList<SerializableGoods> mGoodsShuiGuos;
    private ArrayList<SerializableGoods> mGoodsShuiChanPins;
    private ArrayList<SerializableGoods> mGoodsChuQins;
    private ArrayList<SerializableGoods> mGoodsRouDanNais;
    private ArrayList<SerializableGoods> mGoodsZaLiangs;

    private ArrayList<SerializableGoods> mAsObject_all;
    private ArrayList<SerializableGoods> mAsObject_shucai;
    private ArrayList<SerializableGoods> mAsObject_shuiguo;
    private ArrayList<SerializableGoods> mAsObject_shuichanpin;
    private ArrayList<SerializableGoods> mAsObject_chuqin;
    private ArrayList<SerializableGoods> mAsObject_roudannai;
    private ArrayList<SerializableGoods> mAsObject_zaliang;

    private String[] mGoodsName_shucais;
    private String[] mGoodsName_shuiguos;
    private String[] mGoodsName_shuichanpins;
    private String[] mGoodsName_chuqins;
    private String[] mGoodsName_roudannais;
    private String[] mGoodsName_zaliangs;

    private String current_kind;

    private RecyclerView mGoodTextRecycler;
    private static ArrayList<String> mTextList;
    private GoodsTextAdapter mTextAdapter;
    private static StringBuilder sBuilder;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<SerializableGoods> mCurrentGoodsList;


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

        mGoodsName_shucais = getResources().getStringArray(R.array.science_array_shucai);
        mGoodsName_shuiguos = getResources().getStringArray(R.array.science_array_shuiguo);
        mGoodsName_shuichanpins = getResources().getStringArray(R.array.science_array_shuichanpin);
        mGoodsName_chuqins = getResources().getStringArray(R.array.science_array_chuqin);
        mGoodsName_roudannais = getResources().getStringArray(R.array.science_array_roudannai);
        mGoodsName_zaliangs = getResources().getStringArray(R.array.science_array_zaliang);

        mAsObject_all = (ArrayList<SerializableGoods>) ACache
                .get(getActivity()).getAsObject(ACache.GOODSACACHE_All);

        mAsObject_shucai = (ArrayList<SerializableGoods>) ACache
                .get(getActivity()).getAsObject(ACache.GOODSACACHE_SHUCAI);

        mAsObject_shuiguo = (ArrayList<SerializableGoods>) ACache
                .get(getActivity()).getAsObject(ACache.GOODSACACHE_SHUIGUO);

        mAsObject_shuichanpin = (ArrayList<SerializableGoods>) ACache
                .get(getActivity()).getAsObject(ACache.GOODSACACHE_SHUICHANPIN);

        mAsObject_chuqin = (ArrayList<SerializableGoods>) ACache
                .get(getActivity()).getAsObject(ACache.GOODSACACHE_CHUQIN);

        mAsObject_roudannai = (ArrayList<SerializableGoods>) ACache
                .get(getActivity()).getAsObject(ACache.GOODSACACHE_ROUDANNAI);


        mAsObject_zaliang = (ArrayList<SerializableGoods>) ACache
                .get(getActivity()).getAsObject(ACache.GOODSACACHE_ZALIANG);


        mGoodsAllList = new ArrayList<>();
        mGoodsShuCais = new ArrayList<>();
        mGoodsShuiGuos = new ArrayList<>();
        mGoodsShuiChanPins = new ArrayList<>();
        mGoodsChuQins = new ArrayList<>();
        mGoodsRouDanNais = new ArrayList<>();
        mGoodsZaLiangs = new ArrayList<>();

        mAssetManager = getContext().getAssets();
        try {
            mGoodsAsset_shucai = mAssetManager.list(GOODS_DIR_SHUCAI);
            mGoodsAsset_shuiguo = mAssetManager.list(GOODS_DIR_SHUIGUO);
            mGoodsAsset_shuichanpin = mAssetManager.list(GOODS_DIR_SHUICHANPIN);
            mGoodsAsset_chuqin = mAssetManager.list(GOODS_DIR_CHUQIN);
            mGoodsAsset_roudannai = mAssetManager.list(GOODS_DIR_ROUDANNAI);
            mGoodsAsset_zaliang = mAssetManager.list(GOODS_DIR_ZALIANG);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.i(e.getMessage());
            return;
        }
    }

    private void initGoodsData() {

        if (mTextList == null) {
            mTextList = new ArrayList<>();
        } else {
            mTextList.clear();
        }
        if (mGoods_i != null && mGoods_i.length() != 0) {

            String[] goodsName = mGoods_i.split(";");

            for (int i = 0; i < goodsName.length; i++) {
                mTextList.add(goodsName[i]);
            }
        }


        initGoodsJudet(mAsObject_shucai, mGoodsShuCais, mGoodsAsset_shucai, mGoodsName_shucais, GOODS_DIR_SHUCAI);
        initGoodsJudet(mAsObject_shuiguo, mGoodsShuiGuos, mGoodsAsset_shuiguo, mGoodsName_shuiguos, GOODS_DIR_SHUIGUO);
        initGoodsJudet(mAsObject_shuichanpin, mGoodsShuiChanPins, mGoodsAsset_shuichanpin, mGoodsName_shuichanpins, GOODS_DIR_SHUICHANPIN);
        initGoodsJudet(mAsObject_chuqin, mGoodsChuQins, mGoodsAsset_chuqin, mGoodsName_chuqins, GOODS_DIR_CHUQIN);
        initGoodsJudet(mAsObject_roudannai, mGoodsRouDanNais, mGoodsAsset_roudannai, mGoodsName_roudannais, GOODS_DIR_ROUDANNAI);
        initGoodsJudet(mAsObject_zaliang, mGoodsZaLiangs, mGoodsAsset_zaliang, mGoodsName_zaliangs, GOODS_DIR_ZALIANG);

        if (mGoodsAllList != null && mGoodsAllList.size() != 0) {
            mGoodsAllList.clear();
        }
        int all = mGoodsName_shucais.length + mGoodsName_shuiguos.length +
                mGoodsName_shuichanpins.length + mGoodsName_chuqins.length +
                mGoodsName_roudannais.length + mGoodsName_zaliangs.length;
        if (mAsObject_all != null && mAsObject_all.size() == (all)) {
            mGoodsAllList.addAll(mAsObject_all);
        } else {
            mGoodsAllList.addAll(mGoodsShuCais);
            mGoodsAllList.addAll(mGoodsShuiGuos);
            mGoodsAllList.addAll(mGoodsShuiChanPins);
            mGoodsAllList.addAll(mGoodsChuQins);
            mGoodsAllList.addAll(mGoodsRouDanNais);
            mGoodsAllList.addAll(mGoodsZaLiangs);
        }
    }

    private void initGoodsJudet(ArrayList<SerializableGoods> asObject_kind,
                                ArrayList<SerializableGoods> mGoodsList_kind,
                                String[] goodsAsset_kind, String[] goodsName_kind,
                                String goodsDirkind) {
        if (asObject_kind != null && asObject_kind.size() != 0) {
            if (asObject_kind.size() == goodsName_kind.length) {
                if (mGoodsList_kind != null && mGoodsList_kind.size() != 0) {
                    mGoodsList_kind.clear();
                }
                Logger.i("直接走的缓存");
                mGoodsList_kind.addAll(asObject_kind);
            } else {
                Logger.i("goods未加载完毕未缓存");
                getGoodsData(mGoodsList_kind, goodsAsset_kind, goodsName_kind, goodsDirkind);
            }
        } else {
            Logger.i("goods直接从头加载数据");
            getGoodsData(mGoodsList_kind, goodsAsset_kind, goodsName_kind, goodsDirkind);
        }
    }

    private ArrayList<SerializableGoods> getGoodsData(ArrayList<SerializableGoods> goodsList,
                                                      String[] goods_assets, String[] goods_names, String dir) {
        if (goodsList != null && goodsList.size() != 0) {
            goodsList.clear();
        }
        ArrayList<String> goodsImage_url = new ArrayList<>();
        ArrayList<String> goods_name = new ArrayList<>();
        for (int i = 0; i < goods_assets.length; i++) {
            goodsImage_url.add(goods_assets[i]);
        }
        for (int i = 0; i < goods_names.length; i++) {
            goods_name.add(goods_names[i]);
        }

        Collections.sort(goodsImage_url);
        Collections.sort(goods_name);
        for (int i = 0; i < goods_name.size(); i++) {

            String scientific_name = goods_name.get(i).substring(4);
            String bitmap_url = dir + "/" + goodsImage_url.get(i);

            SerializableGoods goods = new SerializableGoods();

            String sortLetters = Cn2Spell.getPinYinFirstLetter(scientific_name);
            //PingYinUtil.getInstance().getSortLetterBySortKey(scientific_name);
            String sortKey = PingYinUtil.format(scientific_name);
            String headChar = Cn2Spell.getPinYinHeadChar(sortKey);
            String pinYin = Cn2Spell.getPinYin(sortKey);

            //goods.setKind(KIND_SHUIGUO);
//            goods.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));
//            goods.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));

            goods.setScientific_name(scientific_name);
            goods.setBitmapUrl(bitmap_url);
            goods.setSortLetters(sortLetters);
            goods.setWholeSpell(pinYin);
            goods.setSimpleSpell(headChar);

            goodsList.add(goods);
        }
        return goodsList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);

        unbinder = ButterKnife.bind(this, view);


        if (mTextList == null) {
            mTextList = new ArrayList<>();
        }

        initGoodsData();
        initView(view);
        initRecyclerView();
        initGoodsTextRecycler();
        return view;
    }

    private void initView(View view) {
        mEditText = (EditText) view.findViewById(R.id.goods_edit_text);
        mEditText.setText("");
        mEditText.addTextChangedListener(new TextChangeWatcher(this));
        mGoodTextRecycler = (RecyclerView) view.findViewById(R.id.goods_text_recycler);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_goods);
        radioGroup.setOnCheckedChangeListener(this);

        RadioButton radioButton = (RadioButton) view.findViewById(R.id.goods_shucai);
        radioButton.setChecked(true);

    }

    /**
     * 改变RadioGroup时的点击事件
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.goods_shucai:
                closeQiTa();
                current_kind = KIND_SHUCAI;
                if (mCurrentGoodsList == null) {
                    mCurrentGoodsList = new ArrayList<>();
                }
                if (mGoodsShuCais != null) {
                    mCurrentGoodsList = mGoodsShuCais;
                }
                if (mGoodsShuCais != null && mGoodsShuCais.size() != 0) {
                    if (mGoodsAdapter != null) {
                        mGoodsAdapter.updateListView(mGoodsShuCais);
                    }
                }
                break;
            case R.id.goods_shuiguo:
                closeQiTa();
                if (mGoodsShuiGuos != null && mGoodsShuiGuos.size() != 0) {
                    mGoodsAdapter.updateListView(mGoodsShuiGuos);
                }
                current_kind = KIND_SHUIGUO;
                if (mGoodsShuiGuos != null) {
                    mCurrentGoodsList = mGoodsShuiGuos;
                }
                break;
            case R.id.goods_shuichanpin:
                closeQiTa();
                if (mGoodsShuiChanPins != null && mGoodsShuiChanPins.size() != 0) {
                    mGoodsAdapter.updateListView(mGoodsShuiChanPins);
                }
                current_kind = KIND_SHUICHANPIN;
                if (mGoodsShuiChanPins != null) {
                    mCurrentGoodsList = mGoodsShuiChanPins;
                }
                break;
            case R.id.goods_chuqin:
                closeQiTa();
                if (mGoodsChuQins != null && mGoodsChuQins.size() != 0) {
                    mGoodsAdapter.updateListView(mGoodsChuQins);
                }
                current_kind = KIND_CHUQIN;
                if (mGoodsChuQins != null) {
                    mCurrentGoodsList = mGoodsChuQins;
                }
                break;
            case R.id.goods_roudannai:
                closeQiTa();
                if (mGoodsRouDanNais != null && mGoodsRouDanNais.size() != 0) {
                    mGoodsAdapter.updateListView(mGoodsRouDanNais);
                }
                current_kind = KIND_ROUDANNAI;
                if (mGoodsRouDanNais != null) {
                    mCurrentGoodsList = mGoodsRouDanNais;
                }
                break;
            case R.id.goods_zaliang:
                closeQiTa();

                if (mGoodsZaLiangs != null && mGoodsZaLiangs.size() != 0) {
                    mGoodsAdapter.updateListView(mGoodsZaLiangs);
                }
                current_kind = KIND_ZALIANG;
                if (mGoodsZaLiangs != null) {
                    mCurrentGoodsList = mGoodsZaLiangs;
                }
                break;
            case R.id.goods_qita:
                if (mRlEditQita.getVisibility() == View.GONE) {
                    mRlEditQita.setVisibility(View.VISIBLE);
                }
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    mRecyclerView.setVisibility(View.GONE);
                }
                mBtnSureQita.setOnClickListener(v -> {
                    String goods_qita = mEditTextQita.getText().toString().trim();
                    mTextList.add(goods_qita);
                    updateTextListView(mTextList);
                    mEditTextQita.setText("");

                });
            default:
                break;
        }
    }

    private void closeQiTa() {
        if (mRecyclerView.getVisibility() == View.GONE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        if (mRlEditQita.getVisibility() == View.VISIBLE) {
            mRlEditQita.setVisibility(View.GONE);
        }
    }

    /**
     * 添加的货物信息
     */
    private void initGoodsTextRecycler() {
        mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mGoodTextRecycler.setLayoutManager(mLayoutManager);

        mTextAdapter = new GoodsTextAdapter(getContext(), mTextList, pos -> {
            mTextList.remove(pos);

            mTextAdapter.notifyDataSetChanged();
        });
        mGoodTextRecycler.setAdapter(mTextAdapter);
    }

    private void initRecyclerView() {

       /* if (mAsObject_shuiguo == null) {
            PinyinComparator pinyinComparator = new PinyinComparator();
            Collections.sort(mGoodsShuiGuos, pinyinComparator);// 根据a-z进行排序源数据
        }*/
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
//        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        //货物text的recyclerView
        mGoodsAdapter = new SureGoodsAdapter(getContext(), mCurrentGoodsList, new SureGoodsAdapter.onItemClick() {
            @Override
            public void itemClick(SerializableGoods serializableGoods, int position) {

                String scientificName = serializableGoods.getScientific_name();
                if (mTextList.contains(scientificName)) {
                    ToastUtils.singleToast("已经选择了此货物");
                } else {
                    mTextList.add(scientificName);
                }
                //  mGoodsBuilder.append(scientificName + ";");
//                mEditText.setText(mGoodsBuilder.toString());
//                mEditText.setSelection(mGoodsBuilder.length());
                mEditText.setText("");
                updateTextListView(mTextList);
                //进行置顶操作
                serializableGoods.setTop(1);
                serializableGoods.setTime(System.currentTimeMillis());
                refreshView(mCurrentGoodsList);
            }
        });
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(),
//                3));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(), 3));
        // mListView.setAdapter(mGoodsAdapter);
        mRecyclerView.setAdapter(mGoodsAdapter);
    }

    private void updateTextListView(ArrayList<String> mTextList) {
        mTextAdapter.updateListView(mTextList);
        if (mTextList.size() > 3) {
            mGoodTextRecycler.scrollToPosition(mTextList.size() - 1);
        }
    }

    private void refreshView(ArrayList<SerializableGoods> currentGoodsList) {
        collectAndUpdate(currentGoodsList);
       /* switch (current_kind) {
            case KIND_SHUCAI:
                break;
            case KIND_SHUIGUO:
                collectAndUpdate(currentGoodsList);
                break;
            case KIND_SHUICHANPIN:
                collectAndUpdate(currentGoodsList);
                break;
            case KIND_CHUQIN:
                collectAndUpdate(currentGoodsList);
                break;
            case KIND_ROUDANNAI:
                collectAndUpdate(currentGoodsList);
                break;
            case KIND_ZALIANG:
                collectAndUpdate(currentGoodsList);
                break;

            default:
                break;*/
    }
       /* if (KIND_SHUCAI.equals(current_kind) && mGoodsShuCais.size() != 0) {
            if (mGoodsShuCais != null) {
              *//*  mGoodsShuCais.clear();
                mGoodsShuCais.addAll(currentGoodsList);*//*
                Collections.sort(currentGoodsList);
                mGoodsAdapter.updateListView(currentGoodsList);
            }
        } else if (KIND_SHUIGUO.equals(current_kind)) {
            if (mGoodsShuiGuos != null && mGoodsShuiGuos.size() != 0) {
               *//* mGoodsShuiGuos.clear();
                mGoodsShuiGuos.addAll(currentGoodsList);*//*
                Collections.sort(currentGoodsList);
                mGoodsAdapter.updateListView(currentGoodsList);
            }
        } else if (KIND_ZALIANG.equals(current_kind)) {
            if (mGoodsZaLiangs != null && mGoodsZaLiangs.size() != 0) {
                *//*mGoodsZaLiangs.clear();
                mGoodsZaLiangs.addAll(currentGoodsList);*//*
                Collections.sort(currentGoodsList);
                mGoodsAdapter.updateListView(currentGoodsList);
            }
        } else if (KIND_SHUICHANPIN.equals(current_kind)) {
            if (mGoodsShuiChanPins != null && mGoodsZaLiangs.size() != 0) {
                *//*mGoodsZaLiangs.clear();
                mGoodsZaLiangs.addAll(currentGoodsList);*//*
                Collections.sort(currentGoodsList);
                mGoodsAdapter.updateListView(currentGoodsList);
            }
        } else if (KIND_ZALIANG.equals(current_kind)) {
            if (mGoodsZaLiangs != null && mGoodsZaLiangs.size() != 0) {
                *//*mGoodsZaLiangs.clear();
                mGoodsZaLiangs.addAll(currentGoodsList);*//*
                Collections.sort(currentGoodsList);
                mGoodsAdapter.updateListView(currentGoodsList);
            }
        } else if (KIND_ZALIANG.equals(current_kind)) {
            if (mGoodsZaLiangs != null && mGoodsZaLiangs.size() != 0) {
                *//*mGoodsZaLiangs.clear();
                mGoodsZaLiangs.addAll(currentGoodsList);*//*
                Collections.sort(currentGoodsList);
                mGoodsAdapter.updateListView(currentGoodsList);
            }
        }
*/

    private void collectAndUpdate(ArrayList<SerializableGoods> currentGoodsList) {
        Collections.sort(currentGoodsList);
        mGoodsAdapter.updateListView(currentGoodsList);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String goodString = mEditText.getText().toString().trim();

        if ("".equals(goodString)) {
            mGoodsAdapter.updateListView(mCurrentGoodsList);
        } else {
            Logger.i(goodString);
            List<SerializableGoods> fileterList = PingYinUtil.getInstance()
                    .search_goods(mGoodsAllList, goodString);
            for (int i = 0; i < fileterList.size(); i++) {
                Logger.i(fileterList.get(i).getScientific_name());
                Logger.i(fileterList.get(i).getSimpleSpell());
            }
            mGoodsAdapter.updateListView(fileterList);
        }
    }

    public static void setTextChangedFragment(TextFragmentListener listener) {

        //初始化sBuilder,将其归零
        if (sBuilder == null) {
            sBuilder = new StringBuilder();
        } else if (sBuilder.length() != 0) {
            sBuilder.delete(0, sBuilder.length());
        }
        if (mTextList != null && mTextList.size() != 0) {

            for (int i = 0; i < mTextList.size(); i++) {
                if (i == mTextList.size() - 1) {
                    sBuilder.append(mTextList.get(i));
                } else {
                    sBuilder.append(mTextList.get(i) + ";");
                }
            }
            listener.textChanged(sBuilder.toString().trim());
        } else {
            listener.textChanged("");
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onPause() {
        super.onPause();
        ACache.get(getActivity()).put(ACache.GOODSACACHE_All, mGoodsAllList);
        ACache.get(getActivity()).put(ACache.GOODSACACHE_SHUCAI, mGoodsShuCais);
        ACache.get(getActivity()).put(ACache.GOODSACACHE_SHUIGUO, mGoodsShuiGuos);
        ACache.get(getActivity()).put(ACache.GOODSACACHE_SHUICHANPIN, mGoodsShuiChanPins);
        ACache.get(getActivity()).put(ACache.GOODSACACHE_CHUQIN, mGoodsChuQins);
        ACache.get(getActivity()).put(ACache.GOODSACACHE_ROUDANNAI, mGoodsRouDanNais);
        ACache.get(getActivity()).put(ACache.GOODSACACHE_ZALIANG, mGoodsZaLiangs);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    /**
     * 当采集界面退出时,初始化numberfragment的数据
     */
    public static void notifyDataChange() {
        if (mTextList != null) {
            mTextList.clear();
        }
    }

    /**
     * 当采集界面退出时,初始化numberfragment的数据
     */
    public static void notifyDataChangeFromDraft(String goods) {
        if (mTextList == null) {
            mTextList = new ArrayList<>();
        } else {
            mTextList.clear();
        }
        if (goods != null && goods.length() != 0) {

            String[] goodsName = goods.split(";");

            for (int i = 0; i < goodsName.length; i++) {
                mTextList.add(goodsName[i]);
            }
        }
    }
}
