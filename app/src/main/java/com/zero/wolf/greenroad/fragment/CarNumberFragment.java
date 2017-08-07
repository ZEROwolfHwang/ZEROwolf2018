package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.zero.wolf.greenroad.adapter.DividerGridItemDecoration;
import com.zero.wolf.greenroad.adapter.SureCarNumberAdapter;
import com.zero.wolf.greenroad.bean.SerializableNumber;
import com.zero.wolf.greenroad.interfacy.OnFragmentAttachListener;
import com.zero.wolf.greenroad.interfacy.TextChangeWatcher;
import com.zero.wolf.greenroad.interfacy.TextFragmentListener;
import com.zero.wolf.greenroad.litepalbean.SupportCarNumber;
import com.zero.wolf.greenroad.tools.ACache;
import com.zero.wolf.greenroad.tools.PingYinUtil;
import com.zero.wolf.greenroad.tools.ViewUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2017/7/17.
 */

public class CarNumberFragment extends Fragment implements TextChangeWatcher.AfterTextListener {
    Unbinder unbinder;
    private static EditText mEditText;
    @BindView(R.id.number_img_clear_text)
    ImageView mIvClearText_number;
    @BindView(R.id.recycler_view_number)
    RecyclerView mRecyclerView;
    private List<SerializableNumber> mNumberList = new ArrayList<>();
    private static CarNumberFragment sFragment;


    private SureCarNumberAdapter mNumberAdapter;
    private GridLayoutManager mManager;
    private ArrayList<SerializableNumber> mAcacheNumbers;

    private OnFragmentAttachListener mListener;

    public static CarNumberFragment newInstance() {
        if (sFragment == null) {
            sFragment = new CarNumberFragment();
        }
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /**
     * 加载并缓存车牌号头的数据
     */
    private void iniNumberData() {

        List<SupportCarNumber> headList = DataSupport.findAll(SupportCarNumber.class);

        //如果跟数据库长度相同则不作更改，不然则更新
        if (mAcacheNumbers != null) {
            if (mAcacheNumbers.size() == headList.size()) {
                if (mNumberList.size() != 0) {
                    mNumberList.clear();
                }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number, container, false);
        ButterKnife.bind(this, view);

        mAcacheNumbers = (ArrayList<SerializableNumber>) ACache
                .get(getActivity()).getAsObject("sessions");

        iniNumberData();
        mEditText = (EditText) view.findViewById(R.id.number_edit_text);
        initView(view);


        return view;
    }

    private void initView(View view) {


        if (mNumberList.size() == 0) {
            mEditText.setText("粤B");
        } else {
            mEditText.setText(mNumberList.get(0).getName());
            mEditText.setSelection(mNumberList.get(0).getName().length());
        }

        //找到清除text的控件

        mIvClearText_number.setOnClickListener((v -> mEditText.setText("")));


        mEditText.addTextChangedListener(new TextChangeWatcher(this));


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNumberAdapter = new SureCarNumberAdapter((AppCompatActivity) getActivity(), mNumberList, new SureCarNumberAdapter.onItemClick() {
            @Override
            public void itemClick(SerializableNumber serializableNumber) {

                mEditText.setText(serializableNumber.getName());
                mEditText.setSelection((serializableNumber.getName().length()));
                serializableNumber.setTop(1);
                serializableNumber.setTime(System.currentTimeMillis());
                refreshView();
            }
        });

        mManager = new GridLayoutManager(getContext(), 4);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);

        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(), 4));

        mRecyclerView.setAdapter(mNumberAdapter);

    }

    private void refreshView() {
        Collections.sort(mNumberList);
        mNumberAdapter.updateListView(mNumberList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void afterTextChanged(Editable editable) {
        String stationString = ViewUtils.showAndDismiss_clear_text(mEditText, mIvClearText_number);
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

    public static void setTextChangedFragment(TextFragmentListener listener) {
        if (mEditText != null) {
            String number = mEditText.getText().toString().trim();
            listener.textChanged(number);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ACache.get(getActivity()).put("sessions", (ArrayList<SerializableNumber>) mNumberList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentAttachListener) {
            mListener = (OnFragmentAttachListener) context;
        }/* else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }


}
