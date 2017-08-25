package com.zero.wolf.greenroad.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.DividerGridItemDecoration;
import com.zero.wolf.greenroad.adapter.GoodsTextAdapter;
import com.zero.wolf.greenroad.adapter.SureGoodsAdapter;
import com.zero.wolf.greenroad.bean.SerializableGoods;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.interfacy.TextChangeWatcher;
import com.zero.wolf.greenroad.interfacy.TextFragmentListener;
import com.zero.wolf.greenroad.smartsearch.PinyinComparator;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.PingYinUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

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

    private static String[] alias = {"给骄傲;的行情;无限", "说;的侮辱大;家才能", "都无二;的勘测;机",
            "卖家说的法第五", ",设计费都;是对你是;否及时打开",
            "戛洒瓦斯鉴定表", "奥斯卡单位澳大马上", "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
            , "网上订餐爱打架"
    };
    private ArrayList<SerializableGoods> mGoodsArrayList;
    private String[] scientific_names;
    private ArrayList<SerializableGoods> mAsObject;

    private RecyclerView mGoodTextRecycler;
    private static ArrayList<String> mTextList;
    private GoodsTextAdapter mTextAdapter;
    private static StringBuilder sBuilder;
    private LinearLayoutManager mLayoutManager;


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

        mAsObject = (ArrayList<SerializableGoods>) ACache
                .get(getActivity()).getAsObject(ACache.GOODSACACHE);

//        Logger.i(mAsObject.size() + "");

        mGoodsArrayList = new ArrayList<>();

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

                Logger.i(mGoodsNames[i] + "");
                //Bitmap bitmap = getImageFromAssetsFile();

                String scientific_name = scientific_names[i];
                String bitmap_url = GOODS_DIR + "/" + mGoodsNames[i];

                SerializableGoods goods = new SerializableGoods();

//                if (alias.length < i) {
//                    alia = alias[i];
//                }
//                goods.setAlias(alia);
                goods.setScientific_name(scientific_name);
                goods.setBitmapUrl(bitmap_url);

                String sortLetters = PingYinUtil.getInstance().getSortLetterBySortKey(scientific_name);
//                if (sortLetters == null) {
//                    sortLetters = PingYinUtil.getInstance().getSortLetter(alia);
//                }
                goods.setSortLetters(sortLetters);

//                String sortKey = PingYinUtil.format(scientific_name + alia);
                String sortKey = PingYinUtil.format(scientific_name);
                goods.setSimpleSpell(PingYinUtil.getInstance().parseSortKeySimpleSpell(sortKey));
                goods.setWholeSpell(PingYinUtil.getInstance().parseSortKeyWholeSpell(sortKey));

                mGoodsArrayList.add(goods);


            }
            for (int i = 0; i < mGoodsArrayList.size(); i++) {
                Logger.i(mGoodsArrayList.get(i).getSimpleSpell());
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

        if (mAsObject == null) {
            PinyinComparator pinyinComparator = new PinyinComparator();
            Collections.sort(mGoodsArrayList, pinyinComparator);// 根据a-z进行排序源数据
        }
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
//        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        //货物text的recyclerView
        mGoodsAdapter = new SureGoodsAdapter(getContext(), mGoodsArrayList, new SureGoodsAdapter.onItemClick() {
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
                mTextAdapter.updateListView(mTextList);
                if (mTextList.size() > 3) {
                    scrollToPosition(mLayoutManager, mTextList.size() - 3);
                }
                //进行置顶操作
                serializableGoods.setTop(1);
                serializableGoods.setTime(System.currentTimeMillis());
                refreshView();
            }
        });
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(),
//                3));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(), 3));
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

        if ("".equals(goodString)) {
            mGoodsAdapter.updateListView(mGoodsArrayList);
        } else {
            Logger.i(goodString);
            List<SerializableGoods> fileterList = PingYinUtil.getInstance()
                    .search_goods(mGoodsArrayList, goodString);
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
        ACache.get(getActivity()).put(ACache.GOODSACACHE, mGoodsArrayList);
    }

    private void scrollToPosition(LinearLayoutManager manager, int index) {
        manager.scrollToPositionWithOffset(index,
                (int) manager.computeScrollVectorForPosition(index).y);
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
