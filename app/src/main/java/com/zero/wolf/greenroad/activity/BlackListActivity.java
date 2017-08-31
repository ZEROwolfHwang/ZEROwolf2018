package com.zero.wolf.greenroad.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.BasePhotoAdapter;
import com.zero.wolf.greenroad.adapter.BasePhotoViewHolder;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.litepalbean.SupportBlack;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlackListActivity extends BaseActivity {

    @BindView(R.id.toolbar_black_list)
    Toolbar mToolbarBlackList;
    @BindView(R.id.recycler_black_list)
    RecyclerView mRecyclerBlackList;
    private List<SupportBlack> mSupportBlacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        ButterKnife.bind(this);

        initToolbar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecycleViewDivider divider = new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 5, Color.CYAN);
        mRecyclerBlackList.setLayoutManager(manager);
        mRecyclerBlackList.addItemDecoration(divider);

        BasePhotoAdapter<SupportBlack> adapter = new BasePhotoAdapter<SupportBlack>(this,
                R.layout.item_black_text, (ArrayList<SupportBlack>) mSupportBlacks) {
            @Override
            public void convert(BasePhotoViewHolder holder, int position, SupportBlack supportBlack) {
                ToastUtils.singleToast(supportBlack.getLicense().toString());
                TextView textView = holder.getView(R.id.text_item_black);
                textView.setText(supportBlack.getLicense().toString());
            }
        };
        mRecyclerBlackList.setAdapter(adapter);
    }

    private void initData() {
        mSupportBlacks = DataSupport.findAll(SupportBlack.class);

    }

    private void initToolbar() {
        setSupportActionBar(mToolbarBlackList);

        TextView title_text_view = ActionBarTool.getInstance(this, 991).getTitle_text_view();
        title_text_view.setText("黑名单列表");

        mToolbarBlackList.setNavigationIcon(R.drawable.back_up_logo);

        mToolbarBlackList.setNavigationOnClickListener(v -> finish());
    }
}
